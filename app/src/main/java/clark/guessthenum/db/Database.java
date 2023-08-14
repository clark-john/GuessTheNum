package clark.guessthenum.db;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;

public class Database {
	private static boolean isInitialized = false;
	private String connUri = "jdbc:sqlite:app.db";
	private Connection conn;
	
	public void initialize(){
		try {
			if (!isInitialized) {
				conn = DriverManager.getConnection(connUri);
				Statement stmt = conn.createStatement();
				stmt.execute("CREATE TABLE IF NOT EXISTS levels (" +
					"  userId varchar(20) primary key unique not null," +
					"  level integer not null," +
					"  xp integer not null" +
					")"
				);
				stmt.close();
				isInitialized = true;
			}
		} catch (SQLException e){
			e.printStackTrace();
		}
	}

	public Connection getConnection(){
		try {
			return conn == null ? DriverManager.getConnection(connUri) : conn;
		} catch (SQLException e){
			return null;
		}
	}
}
