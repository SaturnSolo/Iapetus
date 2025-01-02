package org.example.utils;

import net.dv8tion.jda.api.entities.User;
import org.example.database.SQLiteDataSource;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class BerryUtils {


    public static void giveUserBerries(String userId, int amount) {
        try (final Connection connection = SQLiteDataSource.getConnection();
             final PreparedStatement ps = connection.prepareStatement("INSERT INTO user_berries (user_id, berry_count) VALUES (?, ?) ON CONFLICT(user_id) DO UPDATE SET berry_count = berry_count + ?")) {
            ps.setString(1, userId);
            ps.setInt(2, amount);
            ps.setInt(3, amount);
            ps.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
    public static void giveUserBerries(User user, int amount) {
        giveUserBerries(user.getId(),amount);
    }

    public static void takeUserBerries(String userId, int amount) {
        try (Connection connection = SQLiteDataSource.getConnection();
             PreparedStatement ps = connection.prepareStatement("UPDATE user_berries SET berry_count = berry_count - ? WHERE user_id = ?")) {
            ps.setInt(1, amount);
            ps.setString(2, userId);
            ps.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
    public static void takeUserBerries(User user, int amount) {
        takeUserBerries(user.getId(),amount);
    }

    public static int getUserBerries(String userId) {
        try (
          final Connection connection = SQLiteDataSource.getConnection();
          final PreparedStatement ps = connection.prepareStatement("SELECT berry_count FROM user_berries WHERE user_id = ?")
        ) {
            ps.setString(1, userId);
            try (final ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    return rs.getInt("berry_count");
                } else {
                    return 0;
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
            return 0;
        }
    }
    public static int getUserBerries(User user, int amount) {
        return getUserBerries(user.getId());
    }
}
