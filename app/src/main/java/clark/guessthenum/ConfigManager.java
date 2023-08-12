package clark.guessthenum;

import java.nio.charset.Charset;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.Map;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;

public class ConfigManager {
	private Charset utf8 = Charset.forName("UTF-8");
	private Gson gson;
	private TypeToken<Map<String, Config>> mapType = new TypeToken<>(){};

	public ConfigManager(){
		gson = new GsonBuilder()
			.setPrettyPrinting()
			.serializeNulls()
			.disableInnerClassSerialization()
			.create();
	}

	private String readJsonFile() throws Exception {
		return Files.readString(Paths.get("./config.json"), utf8);
	}

	private void writeContentsToFile(String content) throws Exception {
		Files.writeString(Paths.get("./config.json"), content, utf8);
	}

	public Config loadFromGuildId(String id) {
		try {
			String content = readJsonFile();
			return gson.fromJson(content, mapType).get(id);			
		} catch (Exception e){
			return null;
		}
	}

	public void initializeConfig(String id) {
		try {
			String content = readJsonFile();

			var config = gson.fromJson(content, mapType);
			config.putIfAbsent(id, Config.defaults());

			writeContentsToFile(gson.toJson(config));

		} catch (Exception e){
			e.printStackTrace();
		}
	}

	public boolean isInitialized(String id){
		try {
			String content = readJsonFile();
			return gson.fromJson(content, mapType).containsKey(id);
		} catch (Exception e){
			return false;
		}
	}
}
