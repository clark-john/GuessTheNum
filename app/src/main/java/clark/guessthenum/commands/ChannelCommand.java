package clark.guessthenum.commands;

import java.util.List;

import clark.guessthenum.ConfigManager;
import net.dv8tion.jda.api.entities.channel.concrete.TextChannel;
import net.dv8tion.jda.api.events.message.MessageReceivedEvent;

public class ChannelCommand extends AdminCommand {
	private ConfigManager config;
	public ChannelCommand(){
		super("channel");
		config = new ConfigManager();
	}
	@Override
	public void handle(MessageReceivedEvent event, List<String> args) {
		super.handle(event, args);
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
			conf.guessingChannelId = tchan.getId();
			config.modifyConfig(id, conf);
			sendMessage(event, "Guessing channel successfully set to " + tchan.getAsMention() + " :white_check_mark:");
		} else {
			sendMessage(event, "Please provide an argument for a channel name with a hashtag");
		}
	}
}
