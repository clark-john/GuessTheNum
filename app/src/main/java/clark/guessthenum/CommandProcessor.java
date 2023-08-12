package clark.guessthenum;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Stream;

import clark.guessthenum.commands.Command;
import net.dv8tion.jda.api.events.message.MessageReceivedEvent;

public class CommandProcessor {
	private String prefix;
	private ArrayList<Command> commands = new ArrayList<>();
	private ContextManager ctx;

	public void process(MessageReceivedEvent event){
		String text = event.getMessage().getContentStripped();
		String msgmode = ctx.getContext("msgmode");
		Stream<Command> str = commands.stream();
		List<Command> ls;

		if (msgmode == null) {
			ls = str.filter(x -> (prefix + x.name).equals(text)).toList();
		} else {
			ls = str.filter(x -> x.name.equals(msgmode)).toList();
		}
		
		if (ls.size() == 0) {
			return;
		}

		ls.get(0).handle(event);
	}
	public CommandProcessor(String prefix){
		this.prefix = prefix;
		ctx = new ContextManager();
	}
	public void setCommands(Command... comms){
		for (Command comm : comms){
			commands.add(comm);
		}
	}
}
