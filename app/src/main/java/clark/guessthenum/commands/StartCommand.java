package clark.guessthenum.commands;

import java.util.List;
import java.util.Random;
import java.util.concurrent.atomic.AtomicInteger;

import clark.guessthenum.ConfigManager;
import clark.guessthenum.db.Database;
import clark.guessthenum.db.Leveling;
import net.dv8tion.jda.api.events.message.MessageReceivedEvent;

public class StartCommand extends Command {
	private final Random r;
	private final AtomicInteger attempts = new AtomicInteger();
	private final ConfigManager config;
	private Leveling level;

	private int correctNumber;

	private Long channelId;

	private boolean numberGenerated = false;
	private boolean isFinished = true;
	private boolean gotCorrect = false;

	public StartCommand(){
		super("start");
		config = new ConfigManager();
		r = new Random();
	}

	@Override
	public void handle(MessageReceivedEvent event, List<String> args, Database db) {
		level = new Leveling(db);
		var conf = config.loadFromGuildId(event.getGuild().getId());

		if (conf.guessingChannelId == null) {
			sendMessage(event, "You haven't set the main guessing channel. Set it by `g:channel [CHANNEL_NAME]`");
			return;
		}

		if (!conf.guessingChannelId.equals(event.getMessage().getChannel().getId())) {
			return;
		}

		if (!numberGenerated) {
			correctNumber = r.nextInt(conf.minimumValue, conf.maximumValue);
			attempts.set(conf.attempts);
			numberGenerated = true;
			isFinished = false;
		}

		if (!isMessageMode()) {
			sendMessage(event, 
				"The rules are simple, all you need to do is to guess a number from " + 
				conf.minimumValue + " to " + conf.maximumValue + ", the bot will guide you" +
				" whether if the number should be higher or lower from the correct number." +
				" You can now guess. You have " + conf.attempts  + " attempts to guess. " + 
				"Some users can intercept to guess."
			);
			setMessageMode(true);
			channelId = event.getMessage().getChannel().getIdLong();

		} else {
			String content = getMessageContent(event);

			if (!isNumber(content)) {
				sendMessage(event, "Must be a number. Not anything else.");
				return;
			}

			int number = Integer.parseInt(content);

			if (event.getChannel().getIdLong() != channelId) {
				return;
			}

			attempts.decrementAndGet();

			if (attempts.get() > 0) {			
				if (number > correctNumber) {
					sendMessage(event, "Lower :arrow_down:", channelId);
				} else if (number < correctNumber) {
					sendMessage(event, "Higher :arrow_up:", channelId);
				} else {
					sendMessage(event, "Correct! You got it :white_check_mark:", channelId);
					gotCorrect = true;
					isFinished = true;
				}
			} else {
				sendMessage(event, "Game over. Better luck next time. The number is " + correctNumber);
				isFinished = true;
			}

			// increase level of any user

			String userId = event.getAuthor().getId();
			boolean isLeveledUp = false;

			if (!gotCorrect) {
				isLeveledUp = level.increaseXp(userId, 3);
			} else {
				isLeveledUp = level.increaseXp(userId, 30 - attempts.get() > 8 ? 0 : 3 * ((conf.attempts - 1) - attempts.get()));
			}

			if (isLeveledUp) {
				level.sendLevelupMessage(event, channelId);
			}

			if (isFinished) {
				setMessageMode(false);
				numberGenerated = false;
				channelId = null;
				attempts.set(conf.attempts);
				gotCorrect = false;
				return;
			}
		}
	}

	private boolean isNumber(String s){
		try {
			Integer.parseInt(s);
			return true;
		} catch (NumberFormatException e){
			return false;
		}
	}
}
