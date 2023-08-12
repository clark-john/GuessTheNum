package clark.guessthenum;

public class Config {
	public Integer minimumValue;
	public Integer maximumValue;
	public Integer attempts;

	public Config(Integer min, Integer max, Integer attempts){
		minimumValue = min;
		maximumValue = max;
		this.attempts = attempts;
	}

	public static Config defaults(){
		return new Config(1, 100, 10);
	}

	public String toString(){
		return String.format(
			"Config{min=%d,max=%d,attempts=%d}", 
			minimumValue, maximumValue, attempts
		);
	}
}
