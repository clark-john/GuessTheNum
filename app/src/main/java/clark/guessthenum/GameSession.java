package clark.guessthenum;

import java.util.Arrays;

// import java.util.Arrays;
// import java.util.List;

public record GameSession(
	String userId, 
	String channelId, 
	int correctNumber, 
	int attempts
) {
	
	public static GameSession create(String userId, String channelId, int number){
		return new GameSession(userId, channelId, number, 10);
	}

	/*public static void parse(String str, String userId){
		String[] items = str.split(";");
		String[] vals = new String[]{};

		for (String x : items) {
			String[] y = x.split("=");
			vals[Arrays.asList(items).indexOf(x)] = y[1];
		}
	}*/
}