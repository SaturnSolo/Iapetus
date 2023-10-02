package org.example.utils;

import net.dv8tion.jda.api.entities.Guild;
import net.dv8tion.jda.api.entities.channel.Channel;
import org.example.database.SQLiteDataSource;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;

public class ChannelUtils {
    public static void addIgnoredChannel(Guild guild1, Channel channel1) {
        try (Connection connection = SQLiteDataSource.getConnection();
             PreparedStatement ps = connection.prepareStatement("INSERT INTO ignored_channels (guild_id, channel_id) VALUES (?, ?)")) {
            ps.setString(1, guild1.getId());
            ps.setString(2, channel1.getId());
            ps.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public static void removeIgnoredChannel(Guild guild2, Channel channel2) {
        try (Connection connection = SQLiteDataSource.getConnection();
             PreparedStatement ps = connection.prepareStatement("DELETE FROM ignored_channels WHERE guild_id = ? AND channel_id = ?")) {
            ps.setString(1, guild2.getId());
            ps.setString(2, channel2.getId());
            ps.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}