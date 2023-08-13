package clark.guessthenum;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

public class GetGson {
	public static Gson get(){
		return new GsonBuilder()
			.setPrettyPrinting()
			.serializeNulls()
			.disableInnerClassSerialization()
			.create();
	}
}
