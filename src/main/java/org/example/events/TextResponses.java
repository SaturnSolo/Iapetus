package org.example.events;

import net.dv8tion.jda.api.entities.channel.middleman.MessageChannel;
import net.dv8tion.jda.api.events.message.MessageReceivedEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;
import org.example.database.SQLiteDataSource;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;

public class TextResponses extends ListenerAdapter {
    @Override
    public void onMessageReceived(MessageReceivedEvent event) {
        if (event.getAuthor().isBot()) return;
        String content = event.getMessage().getContentDisplay();
        MessageChannel channel = event.getChannel();
        if (content.equals("ping")) {
            channel.sendMessage("Pong! 🌸").queue();
        }
        if (content.contains("🍓")) {
            channel.sendMessage("A Strawberry :D").queue();
        }
        if (content.contains("@Iapetus")) {
            channel.sendMessage("**Hello! If you are needing help, type /help!").queue();
        }
        if (content.toLowerCase().contains("moss")) {
            channel.sendMessage("I love moss!").queue();
        }

        String authorId = event.getAuthor().getId();
        boolean isDev = authorId.equals("951957003486519350") || authorId.equals("955184658637783050");
        if (content.equals("teehee :3") && isDev) {
            try (final Connection connection = SQLiteDataSource.getConnection();
                 final PreparedStatement ps = connection.prepareStatement("INSERT INTO user_berries (user_id, berry_count) VALUES (?, ?) ON CONFLICT(user_id) DO UPDATE SET berry_count = berry_count + 100")) {
                ps.setString(1, event.getAuthor().getId());
                ps.setInt(2, 1);
                ps.executeUpdate();
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
    }
}
