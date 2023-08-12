package clark.guessthenum;

import java.util.Properties;

public class ContextManager {
	private static Properties ctx = new Properties();
	
	public void setContext(String key, String value){
		if (value == null) {
			ctx.remove(key);
		} else {
			ctx.setProperty(key, value);
		}
	}
	public String getContext(String key){
		return ctx.getProperty(key);
	}	
}
