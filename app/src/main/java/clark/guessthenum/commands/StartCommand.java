package clark.guessthenum.commands;

import java.util.Random;
import java.util.concurrent.atomic.AtomicInteger;

import clark.guessthenum.ConfigManager;
import clark.guessthenum.GameSessionManager;
import net.dv8tion.jda.api.events.message.MessageReceivedEvent;

public class StartCommand extends Command {
	private final AtomicInteger attempts = new AtomicInteger(10);
	private final ConfigManager config;
	private final GameSessionManager gsesman;
	private final Random r;

	private int correctNumber;

	// private String channelId;

	private boolean numberGenerated = false;
	private boolean isFinished = true;
	private boolean initAttempts = false;

	public StartCommand(){
		super("start");
		config = new ConfigManager();
		r = new Random();
		gsesman = new GameSessionManager();
	}

	private void initAttemptsNumber(MessageReceivedEvent event){
		int num = config.loadFromGuildId(event.getGuild().getId()).attempts;
		if (num != attempts.get()) {
			attempts.set(num);
		}
		initAttempts = true;
	}

	@Override
	public void handle(MessageReceivedEvent event) {
		if (!initAttempts) {
			initAttemptsNumber(event);
		}

		String userId = event.getAuthor().getId();
		String channelId = event.getMessage().getChannel().getId();

		// create session for a user
		if (!gsesman.hasSession(userId)) {
			gsesman.create(userId, channelId, correctNumber, attempts.get());
		} else {
			processGuess(event, userId, channelId);
		}
	}

	private void processGuess(MessageReceivedEvent event, String userId, String channelId){
		var conf = config.loadFromGuildId(event.getGuild().getId());
		/*
		if (!numberGenerated) {
			correctNumber = r.nextInt(conf.minimumValue, conf.maximumValue);
			numberGenerated = true;
		}*/

		if (!isMessageMode()) {
			sendMessage(event, 
				"<@"+ userId + ">" + " The rules are simple, all you need to do is to guess a number from " + 
				conf.minimumValue + " to " + conf.maximumValue + ", the bot will guide you" +
				" whether if the number should be higher or lower from the correct number." +
				" You can now guess. You have " + conf.attempts  + " attempts to guess."
			);
			setMessageMode(true);
			isFinished = false;

		} else {
			String content = getMessageContent(event);

			if (!isNumber(content)) {
				sendMessage(event, "Must be a number. Not anything else.");
				return;
			}

			int number = Integer.parseInt(content);

			if (!event.getChannel().getId().equals(channelId)) {
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
				}
			} else {
				sendMessage(event, "Game over. Better luck next time. The number is " + correctNumber);
				finish();
			}

			if (isFinished) {
				isFinished = false;
				return;
			}

			attempts.decrementAndGet();
		}
	}

	private void finish(){
		setMessageMode(false);
		numberGenerated = false;
		// channelId = null;
		isFinished = true;
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
