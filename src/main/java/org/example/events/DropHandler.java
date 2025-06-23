package org.example.events;



import net.dv8tion.jda.api.events.interaction.command.SlashCommandInteractionEvent;
import net.dv8tion.jda.api.events.message.MessageReceivedEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;
import net.dv8tion.jda.api.interactions.components.buttons.Button;
import org.example.database.SQLiteDataSource;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;

public class DropHandler extends ListenerAdapter {

    private int messageCount = 0;

    @Override
    public void onMessageReceived(MessageReceivedEvent event) {
        if (event.getAuthor().isBot()) return;
        String timed = event.getMessage().getContentDisplay();

        // Retrieve ignored channels from database
        List<Long> ignoredChannels = new ArrayList<>();
        try (final Connection connection = SQLiteDataSource.getConnection();
             final PreparedStatement ps = connection.prepareStatement("SELECT channel_id FROM ignored_channels")) {
            try (final ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    ignoredChannels.add(rs.getLong("channel_id"));
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        // Check if current channel is ignored
        if (ignoredChannels.contains(event.getChannel().getIdLong())) {
            return;
        }

        if (++messageCount == 26) {
            String sent = event.getChannel().sendMessage("**Strawberry Drop!**")
                    .addActionRow(
                            Button.primary("strawberry", "🍓"))
                    //.flatMap(message -> message.addReaction(Emoji.fromUnicode("🍓")))
                    .queueAfter(1, TimeUnit.SECONDS).toString();
            messageCount = 0; // reset message count
            if (event.getAuthor().isBot()) {
                return;
            }
        }
    }
}





