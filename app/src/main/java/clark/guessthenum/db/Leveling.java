package clark.guessthenum.db;

import java.io.IOException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import clark.guessthenum.GetGson;
import net.dv8tion.jda.api.entities.User;
import net.dv8tion.jda.api.events.message.MessageReceivedEvent;

public class Leveling {
	private final Gson gson;
	private final Database db;
	private final TypeToken<List<Level>> levelsType = new TypeToken<>(){};

	public Leveling(Database db){
		this.db = db;
		gson = GetGson.get();
	}

	private void initializeUser(String userId){
		Connection conn = db.getConnection();
		PreparedStatement stmt;
		try {
			stmt = conn.prepareStatement("insert into levels (userId, level, xp) values (?, 0, 0)");
			stmt.setString(1, userId);
			stmt.execute();
			stmt.close();
		} catch (SQLException e){
			if (e.getMessage().contains("UNIQUE constraint failed")) {
				return;
			}
			e.printStackTrace();
		}
	}

	// if leveled up returns true otherwise false
	public boolean increaseXp(String userId, int xp){
		initializeUser(userId);
		Connection conn = db.getConnection();
		PreparedStatement stmt;

		try {
			int previousXp;
			int previousLevel;
			int increasedXp;

			// fetch previous xp
			stmt = conn.prepareStatement("select level, xp from levels where userId = ?",
				ResultSet.TYPE_FORWARD_ONLY,
				ResultSet.CONCUR_READ_ONLY
			);
			stmt.setString(1, userId);
			
			// get result from executed query
			ResultSet r = stmt.executeQuery();
			r.next();
			previousLevel = r.getInt("level");
			previousXp = r.getInt("xp");
			r.close();

			Level lev = findLevel(previousLevel);

			increasedXp = previousXp += xp;

			// sql statements
			String updateXp = "update levels set xp = ? where userId = ?";
			String updateXpWithLevel = "update levels set xp = ?, level = ? where userId = ?";

			boolean isLeveledUp = false;

			// level up
			if (increasedXp >= lev.xp) {
				stmt = conn.prepareStatement(updateXpWithLevel);
				stmt.setInt(1, increasedXp - lev.xp);
				stmt.setInt(2, previousLevel += 1);
				stmt.setString(3, userId);
				stmt.execute();
				isLeveledUp = true;
			} else {
				// just increase xp
				stmt = conn.prepareStatement(updateXp);
				stmt.setInt(1, increasedXp);
				stmt.setString(2, userId);
				stmt.execute();
			}
		
			stmt.close();
			return isLeveledUp;

		} catch (SQLException e){
			e.printStackTrace();
			return false;
		}
	}

	private Level findLevel(int level){
		try {
			String levels = new String(getClass().getClassLoader().getResourceAsStream("levels.json").readAllBytes());
			return gson.fromJson(levels, levelsType).stream().filter(x -> x.level == level).findFirst().orElse(null);
		} catch (IOException e){
			return null;
		}
	}

	public void sendLevelupMessage(MessageReceivedEvent event, long channelId){
		User u = event.getAuthor();
		Connection conn = db.getConnection();
		PreparedStatement stmt;
		try {
			stmt = conn.prepareStatement("select level from levels where userId = ?",
				ResultSet.TYPE_FORWARD_ONLY,
				ResultSet.CONCUR_READ_ONLY
			);
			stmt.setString(1, u.getId());
			
			// get result from executed query
			ResultSet r = stmt.executeQuery();
			r.next();
			int level = r.getInt("level");
			r.close();
			event
				.getGuild()
				.getTextChannelById(channelId)
				.sendMessage(u.getAsMention() + " just reached level **" + level + "**. Sheesh!")
				.queue();
		} catch (SQLException e){
			e.printStackTrace();
		}
	}
}
