package clark.guessthenum.commands;

import java.util.List;

import clark.guessthenum.ConfigManager;
import clark.guessthenum.db.Database;
import net.dv8tion.jda.api.entities.channel.concrete.TextChannel;
import net.dv8tion.jda.api.events.message.MessageReceivedEvent;

public class UnsetCommand extends AdminCommand {
	private ConfigManager config;
	public UnsetCommand(){
		super("unset");
		config = new ConfigManager();
	}

	@Override
	public void handle(MessageReceivedEvent event, List<String> args, Database db) {
		super.handle(event, args, db);
		if (isAdmin) {
			if (args != null) {
				String channel = args.get(0);
				List<TextChannel> channels = event.getGuild().getTextChannelsByName(channel.replace("#", ""), true);
				if (channels.size() == 0) {
					sendMessage(event, "Text channel not found");
					return;
				}
				String id = event.getGuild().getId();
				var conf = config.loadFromGuildId(id);
				TextChannel tchan = channels.get(0);
				conf.guessingChannelId = null;
				config.modifyConfig(id, conf);
				sendMessage(event, "Successfully unset " + tchan.getAsMention() + " as a guessing channel :white_check_mark:");
			} else {
				sendMessage(event, "Please provide an argument for a channel name with a hashtag");
			}			
		}
	}
}
