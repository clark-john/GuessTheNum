package clark.guessthenum.commands;

import java.util.List;
import java.util.Random;
import java.util.concurrent.atomic.AtomicInteger;

import clark.guessthenum.ConfigManager;
import net.dv8tion.jda.api.events.message.MessageReceivedEvent;

public class StartCommand extends Command {
	private final Random r;
	private final AtomicInteger attempts = new AtomicInteger(10);
	private final ConfigManager config;
	
	private int correctNumber;

	private Long channelId;

	private boolean numberGenerated = false;

	public StartCommand(){
		super("start");
		config = new ConfigManager();
		r = new Random();
	}

	@Override
	public void handle(MessageReceivedEvent event, List<String> args) {
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
			numberGenerated = true;
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

			if (attempts.get() > 0) {			
				if (number > correctNumber) {
					sendMessage(event, "Lower :arrow_down:", channelId);
				} else if (number < correctNumber) {
					sendMessage(event, "Higher :arrow_up:", channelId);
				} else {
					sendMessage(event, "Correct! You got it :white_check_mark:", channelId);
					finish();
					return;
				}
			} else {
				sendMessage(event, "Game over. Better luck next time. The number is " + correctNumber);
				finish();
				return;
			}

			attempts.decrementAndGet();
		}
	}

	private void finish(){
		setMessageMode(false);
		numberGenerated = false;
		channelId = null;
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
