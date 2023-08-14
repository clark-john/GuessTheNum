package clark.guessthenum.commands;

import java.util.List;

import clark.guessthenum.db.Database;
import net.dv8tion.jda.api.Permission;
import net.dv8tion.jda.api.events.message.MessageReceivedEvent;

public class AdminCommand extends Command {
	protected boolean isAdmin = false;
	protected AdminCommand(String name){
		super(name);
	}

	@Override
	public void handle(MessageReceivedEvent event, List<String> args, Database db) {
		if (!event.getMember().hasPermission(Permission.ADMINISTRATOR)) {
			sendMessage(event, "You must be an admin to use this command.");
			return;
		}
		isAdmin = true;
	}
}
