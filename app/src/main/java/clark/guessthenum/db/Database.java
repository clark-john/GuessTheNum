package clark.guessthenum.db;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;

public class Database {
	private static boolean isInitialized = false;
	private Connection conn;
	public void initialize(){
		try {
			if (!isInitialized) {
				conn = DriverManager.getConnection("jdbc:sqlite:app.db");
				Statement stmt = conn.createStatement();
				stmt.execute("CREATE TABLE IF NOT EXISTS levels (" +
					"  userId varchar(20) primary key unique," +
					"  level integer," +
					"  xp integer" +
					")"
				);
				stmt.close();
				isInitialized = true;
			}
		} catch (SQLException e){
			e.printStackTrace();
		}
	}
}
