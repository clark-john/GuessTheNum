package clark.guessthenum.commands;

import java.awt.Color;
import java.util.List;

import clark.guessthenum.db.Database;
import clark.guessthenum.embed.EmbedCommandsBuilder;
import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.entities.MessageEmbed.Field;
import net.dv8tion.jda.api.events.message.MessageReceivedEvent;

public class HelpCommand extends Command {
	public HelpCommand() {
		super("help");
	}

	@Override
	public void handle(MessageReceivedEvent event, List<String> args, Database db) {
		event.getMessage().getChannel().sendMessageEmbeds(
			new EmbedBuilder()
				.setTitle("Prefix - g:")
				.setColor(Color.decode("#80BAB7"))
				.addField(new Field(
 					"Setup", 
 					"1. Create/find a channel that you would like to guess in.\n" + 
 					"2. (Admin only) Run this command and add an argument where to set the main guessing channel: `g:channel [#CHANNEL_NAME]`\n" + 
 					"3. Type `g:start` to begin guessing",
 					false
 				))
				.addField(new EmbedCommandsBuilder("Admin Commands")
					.addCommand("channel", "Set a guessing channel")
					.addCommand("unset", "Unset a guessing channel")
					.build()
				)
				.addField(new EmbedCommandsBuilder("User Commands")
					.addCommand("start", "Start the game")
					.build()
				)
				.build()
		).queue();
	}
}
