package clark.guessthenum;

import java.util.concurrent.ConcurrentHashMap;

public class GameSessionManager {
	public ConcurrentHashMap<String, String> hashMap = new ConcurrentHashMap<>();

	public void create(String userId, String channelId, int number, int attempts){
		hashMap.putIfAbsent(
			userId, 
			String.format(
				"channelid=%s;correctNumber=%d;attempts=%d", 
				channelId, number, attempts
			)
		);
	}

	public boolean hasSession(String userId){
		return hashMap.get(userId) != null;
	}
}
