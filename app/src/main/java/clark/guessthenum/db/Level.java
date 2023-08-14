package clark.guessthenum.db;

public class Level {
	public int level;
	public int xp;
	public String toString(){
		return String.format("Level{level=%d,xp=%d}", level, xp);
	}
}
