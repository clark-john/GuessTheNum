package clark.guessthenum.commands;

import java.awt.Color;

import clark.guessthenum.embed.EmbedCommandsBuilder;
import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.events.message.MessageReceivedEvent;

public class HelpCommand extends Command {
	public HelpCommand() {
		super("help");
	}

	@Override
	public void handle(MessageReceivedEvent event) {
		event.getMessage().getChannel().sendMessageEmbeds(
			new EmbedBuilder()
				.setColor(Color.decode("#80BAB7"))
				/*.addField(new EmbedCommandsBuilder("Admin Commands")
					.addCommand("hehe", "Command")
					.addCommand("uwu", "Another")
					.build()
				)*/
				.addField(new EmbedCommandsBuilder("User Commands")
					.addCommand("start", "Start the game")
					.build()
				)
				.build()
		).queue();
	}
}
