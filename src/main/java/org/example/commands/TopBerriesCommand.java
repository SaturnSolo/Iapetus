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
        String query = "SELECT user_id, berry_count FROM user_berries ORDER BY berry_count DESC LIMIT 10";


        try (Connection connection = SQLiteDataSource.getConnection();
             PreparedStatement ps = connection.prepareStatement(query);
             ResultSet rs = ps.executeQuery()) {


            StringBuilder leaderboard = new StringBuilder();
            int rank = 1;


            boolean resultsFound = false;


            while (rs.next()) {
                resultsFound = true;
                String userId = rs.getString("user_id");
                int berryCount = rs.getInt("berry_count");


                User user = event.getJDA().getUserById(userId);

                // If the user exists, append to leaderboard
                if (user != null) {
                    leaderboard.append("**#").append(rank).append("** ")
                            .append(user.getAsMention())
                            .append(" - **").append(berryCount).append("** berries 🍓\n");
                    rank++;
                } else {
                    // If user not found in cache, display a generic mention
                    leaderboard.append("**#").append(rank).append("** <@").append(userId).append(">")
                            .append(" - **").append(berryCount).append("** berries 🍓\n");
                    rank++;
                }
            }

            if (!resultsFound) {
                event.reply("No users found in the leaderboard.").setEphemeral(true).queue();
                return true;
            }


            EmbedBuilder embedBuilder = new EmbedBuilder();
            embedBuilder.setTitle("Top 10 Berry Collectors in " + event.getGuild().getName());
            embedBuilder.setDescription(leaderboard.toString());
            embedBuilder.setColor(0x00FF00);

            event.replyEmbeds(embedBuilder.build()).queue();

        } catch (SQLException e) {
            e.printStackTrace();
            event.reply("An error occurred while fetching the top berry users. Please try again later.").setEphemeral(true).queue();
        }

        return true;
    }
}