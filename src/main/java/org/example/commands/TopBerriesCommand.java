package org.example.commands;

import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.entities.Member;
import net.dv8tion.jda.api.entities.User;
import net.dv8tion.jda.api.events.interaction.command.SlashCommandInteractionEvent;
import org.example.database.SQLiteDataSource;
import org.example.structures.IapetusCommand;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class TopBerriesCommand extends IapetusCommand {
    public TopBerriesCommand() {
        super("leaderboard", "Shows the user with the most berries in the server");
    }

    @Override
    public boolean runCommand(SlashCommandInteractionEvent event) {
        String guildId = event.getGuild().getId();
        String query = "SELECT ub.user_id, ub.berry_count " +
                "FROM user_berries ub " +
                "JOIN user_guilds ug ON ub.user_id = ug.user_id " +
                "WHERE ug.guild_id = ? " +
                "ORDER BY ub.berry_count DESC LIMIT 10";

        try (Connection connection = SQLiteDataSource.getConnection();
             PreparedStatement ps = connection.prepareStatement(query)) {

            ps.setString(1, guildId); // Set the guild ID for filtering

            try (ResultSet rs = ps.executeQuery()) {
                StringBuilder leaderboard = new StringBuilder();
                int rank = 1;
                boolean resultsFound = false;

                while (rs.next()) {
                    resultsFound = true;
                    String userId = rs.getString("user_id");
                    int berryCount = rs.getInt("berry_count");

                    // Verify if the user is part of the current guild
                    Member member = event.getGuild().getMemberById(userId);

                    if (member != null) {
                        leaderboard.append("**#").append(rank).append("** ")
                                .append(member.getAsMention())
                                .append(" - **").append(berryCount).append("** berries 🍓\n");
                    } else {
                        leaderboard.append("**#").append(rank).append("** <@").append(userId).append("> ")
                                .append(" - **").append(berryCount).append("** berries 🍓\n");
                    }
                    rank++;
                }

                if (!resultsFound) {
                    event.reply("No users found in the leaderboard for this server.").setEphemeral(true).queue();
                    return true;
                }

                // Build and send the leaderboard embed
                EmbedBuilder embedBuilder = new EmbedBuilder();
                embedBuilder.setTitle("Top 10 Berry Collectors in " + event.getGuild().getName());
                embedBuilder.setDescription(leaderboard.toString());
                embedBuilder.setColor(0x8cae68);

                event.replyEmbeds(embedBuilder.build()).queue();
            }

        } catch (SQLException e) {
            e.printStackTrace();
            event.reply("An error occurred while fetching the top berry users. Please try again later.").setEphemeral(true).queue();
        }

        return true;
    }
}