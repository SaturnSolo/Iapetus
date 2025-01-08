package org.example.events;

import net.dv8tion.jda.api.events.interaction.command.SlashCommandInteractionEvent;
import net.dv8tion.jda.api.events.interaction.component.ButtonInteractionEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;
import org.example.database.SQLiteDataSource;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;

public class InteractionLogger extends ListenerAdapter {
public InteractionLogger() {

}

@Override
public void onSlashCommandInteraction(SlashCommandInteractionEvent event) {
    logUserGuild(event.getUser().getId(), event.getGuild().getId());
}

@Override
public void onButtonInteraction(ButtonInteractionEvent event) {
    logUserGuild(event.getUser().getId(), event.getGuild().getId());
}

private void logUserGuild(String userId, String guildId) {
    String query = "INSERT OR IGNORE INTO user_guilds (user_id, guild_id) VALUES (?, ?)";

    try (Connection connection = SQLiteDataSource.getConnection();
         PreparedStatement ps = connection.prepareStatement(query)) {
        ps.setString(1, userId);
        ps.setString(2, guildId);
        ps.executeUpdate();
    } catch (SQLException e) {
        e.printStackTrace();
    }
}
}
