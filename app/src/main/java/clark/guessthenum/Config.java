package clark.guessthenum;

public class Config {
	public Integer minimumValue;
	public Integer maximumValue;
	public Integer attempts;
	public String guessingChannelId;

	public Config(Integer min, Integer max, Integer attempts, String channelId){
		minimumValue = min;
		maximumValue = max;
		this.attempts = attempts;
		guessingChannelId = channelId;
	}

	public static Config defaults(){
		return new Config(1, 100, 10, null);
	}

	public String toString(){
		return String.format(
			"Config{min=%d,max=%d,attempts=%d}", 
			minimumValue, maximumValue, attempts
		);
	}
}
