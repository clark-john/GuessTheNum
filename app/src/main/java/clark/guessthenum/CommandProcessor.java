package clark.guessthenum;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.function.Predicate;
import java.util.stream.Stream;

import clark.guessthenum.commands.Command;
import clark.guessthenum.db.Database;
import net.dv8tion.jda.api.events.message.MessageReceivedEvent;

public class CommandProcessor {
	private String prefix;
	private ArrayList<Command> commands = new ArrayList<>();
	private ContextManager ctx;
	private Database db;

	public void process(MessageReceivedEvent event){
		String text = event.getMessage().getContentStripped();
		String msgmode = ctx.getContext("msgmode");
		Stream<Command> str = commands.stream();
		List<Command> ls;

		List<String> rawArgs = Arrays.asList(text.split(" "));

		List<String> args = null;

		if (rawArgs.size() > 1) {
			args = new ArrayList<String>(rawArgs).subList(1, rawArgs.size());
		}

		Command comm;

		Predicate<? super Command> pred;

		if (msgmode == null) {
			pred = x -> (prefix + x.name).equals(rawArgs.get(0));
		} else {
			pred = x -> x.name.equals(msgmode);
		}
		comm = str.filter(pred).findFirst().orElse(null);
		
		if (comm == null) {
			return;
		}

		comm.handle(event, args, db);
	}
	
	public CommandProcessor(String prefix){
		this.prefix = prefix;
		ctx = new ContextManager();
	}

	// pre process methods
	public void setCommands(Command... comms){
		for (Command comm : comms){
			commands.add(comm);
		}
	}
	public void provideDb(Database db){
		this.db = db;
	}
}
