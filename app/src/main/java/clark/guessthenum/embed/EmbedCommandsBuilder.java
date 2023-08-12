package clark.guessthenum.embed;

import net.dv8tion.jda.api.entities.MessageEmbed.Field;

public class EmbedCommandsBuilder {
	private String name;
	private String value = "";
	public EmbedCommandsBuilder(String n){
		name = n;
	}
	public EmbedCommandsBuilder addCommand(String name, String description){
		value += String.format("`%s`  -  %s\n", name, description);
		return this;
	}
	public Field build(){
		return new Field(name, value.stripTrailing(), false);
	}
}
