package clark.guessthenum.commands;

import clark.guessthenum.ContextManager;
import net.dv8tion.jda.api.events.message.MessageReceivedEvent;

public abstract class Command {
	public final String name;
	private final ContextManager ctx;

	protected Command(String name){
		this.name = name;
		ctx = new ContextManager();
	}
	
	// abstract methods
	public abstract void handle(MessageReceivedEvent event);

	// instance methods
	public void setMessageMode(boolean b){
		ctx.setContext("msgmode", b ? name : null);
	}

	public boolean isMessageMode(){
		return ctx.getContext("msgmode") != null;
	}

	// shortcuts
	public void sendMessage(MessageReceivedEvent event, CharSequence content){
		event.getMessage().getChannel().sendMessage(content).queue();
	}
	public void sendMessage(MessageReceivedEvent event, CharSequence content, long channelId){
		event.getGuild().getTextChannelById(channelId).sendMessage(content).queue();
	}

	public String getMessageContent(MessageReceivedEvent event){
		return event.getMessage().getContentStripped();
	}

	// overriden toString()
	public String toString(){
		return String.format("Command{name=%s}", name);
	}
}
