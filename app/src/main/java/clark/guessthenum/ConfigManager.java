package clark.guessthenum;

import java.nio.charset.Charset;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Map;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

public class ConfigManager {
	private final Charset utf8 = Charset.forName("UTF-8");
	private final Gson gson;
	private final Path configFile = Paths.get("./config.json");
	private final TypeToken<Map<String, Config>> mapType = new TypeToken<>(){};

	public ConfigManager(){
		gson = GetGson.get();
	}

	private String readJsonFile() throws Exception {
		return Files.readString(configFile, utf8);
	}

	private void writeContentsToFile(String content) throws Exception {
		Files.writeString(configFile, content, utf8);
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

	public void modifyConfig(String id, Config conf){
		try {
			String content = readJsonFile();

			var config = gson.fromJson(content, mapType);
			config.replace(id, conf);

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
