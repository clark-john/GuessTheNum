package clark.guessthenum;

import clark.guessthenum.commands.ChannelCommand;
import clark.guessthenum.commands.HelpCommand;
import clark.guessthenum.commands.StartCommand;
import clark.guessthenum.commands.UnsetCommand;

import clark.guessthenum.db.Database;
// import io.github.cdimascio.dotenv.Dotenv;
import net.dv8tion.jda.api.JDA;
import net.dv8tion.jda.api.JDABuilder;
import net.dv8tion.jda.api.entities.Activity;
import net.dv8tion.jda.api.events.message.MessageReceivedEvent;
import net.dv8tion.jda.api.requests.GatewayIntent;
import net.dv8tion.jda.api.utils.cache.CacheFlag;
import net.dv8tion.jda.api.hooks.ListenerAdapter;

public class App extends ListenerAdapter implements Runnable {
	private CommandProcessor pr;
	private ConfigManager config;
	private Database db;

	public static void main(String[] args) {
		new Thread(new App()).start();
	}

	public void run(){
		db = new Database();
		try {
			// Dotenv dotenv = Dotenv.load();
			// String token = dotenv.get("BOT_TOKEN");
			String token = System.getenv("BOTTEST_TOKEN");
			JDA jda = JDABuilder
				.create(token, 
					GatewayIntent.GUILD_MESSAGES,
					GatewayIntent.MESSAGE_CONTENT 
				)
				.addEventListeners(this)
				.setActivity(Activity.playing("g:help"))
				.disableCache(
					CacheFlag.ACTIVITY, CacheFlag.VOICE_STATE, CacheFlag.EMOJI, 
					CacheFlag.STICKER, CacheFlag.CLIENT_STATUS, CacheFlag.ONLINE_STATUS, 
					CacheFlag.SCHEDULED_EVENTS
				)
				.build();

	 		pr = new CommandProcessor("g:");
	 		
	 		db.initialize();

			jda.awaitReady();
	 		
	 		pr.setCommands(
	 			new StartCommand(),
	 			new HelpCommand(),
	 			new ChannelCommand(),
	 			new UnsetCommand()
	 		);

	 		pr.provideDb(db);

	 		config = new ConfigManager();
		} catch (Exception e) {
			e.printStackTrace();
		}	
	}

 	@Override
 	public void onMessageReceived(MessageReceivedEvent event) {
 		String guildId = event.getGuild().getId();

 		if (!config.isInitialized(guildId)) {
 			config.initializeConfig(guildId);
 		}

 		// only process commands/messages received from user
 		if (!event.getAuthor().isBot()) {
 			pr.process(event);
 		}
 	}
}
