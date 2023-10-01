package org.example.commands;



import net.dv8tion.jda.api.events.interaction.command.SlashCommandInteractionEvent;
import net.dv8tion.jda.api.events.interaction.component.ButtonInteractionEvent;
import net.dv8tion.jda.api.events.message.MessageReceivedEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;
import net.dv8tion.jda.api.interactions.components.buttons.Button;
import org.example.database.SQLiteDataSource;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;

public class CommandHandler extends ListenerAdapter {

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

        if (++messageCount == 74) {
            String sent = event.getChannel().sendMessage("**Strawberry drop!**")
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


    @Override
    public void onButtonInteraction(ButtonInteractionEvent event) {
        if (event.getComponentId().equals("strawberry")) {
            String user = event.getInteraction().getUser().getAsMention();
            String user2 = event.getInteraction().getUser().getId();
            event.reply("🍓 **Has been claimed by** " + user).queue();
            event.editButton(Button.primary("Strawberry claimed", "✨").asDisabled()).queue();

            try (final Connection connection = SQLiteDataSource.getConnection();
                 final PreparedStatement ps = connection.prepareStatement("INSERT INTO user_berries (user_id, berry_count) VALUES (?, ?) ON CONFLICT(user_id) DO UPDATE SET berry_count = berry_count + 1")) {
                ps.setString(1, user2);
                ps.setInt(2, 1);
                ps.executeUpdate();
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
    }

    @Override
    public void onSlashCommandInteraction(SlashCommandInteractionEvent event) {
        if (event.getName().equals("berries")) {
            String userId = event.getMember().getId();
            try (final Connection connection = SQLiteDataSource.getConnection();
                 final PreparedStatement ps = connection.prepareStatement("SELECT berry_count FROM user_berries WHERE user_id = ?")) {
                ps.setString(1, userId);
                try (final ResultSet rs = ps.executeQuery()) {
                    if (rs.next()) {
                        int berryCount = rs.getInt("berry_count");
                        event.reply("**You have " + berryCount + " berries** 🍓").queue();
                    } else {
                        event.reply("**You have 0 berries** 🍓").queue();
                    }
                }
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
    }
}





