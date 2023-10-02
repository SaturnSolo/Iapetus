package org.example.commands.pet;

import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.entities.MessageEmbed;
import net.dv8tion.jda.api.events.interaction.command.SlashCommandInteractionEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;
import org.example.database.SQLiteDataSource;
import org.example.structures.IapetusCommand;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class PetMenuCommand extends IapetusCommand {
    public PetMenuCommand() {
        super("pets", "shows a list of your pets");
    }
    @Override
    public boolean runCommand(SlashCommandInteractionEvent event) {
        String userId = event.getUser().getId();
        String petList = getHatchedPets(userId);

        if (!petList.isEmpty()) {
            // Build a message to display the user's pets
            MessageEmbed embed = buildPetListEmbed(petList);

            // Check if the interaction is not already replied to
            if (!event.isAcknowledged()) {
                // Send the embed to the user
                event.replyEmbeds(embed).queue();
            }
        } else {
            // No hatched pets found
            event.reply("You don't have any hatched pets yet.").queue();
        }
        return true;
    }
    private String getHatchedPets(String userId) {
        // Implement logic to fetch hatched pets from the database
        // Replace this with your database retrieval logic
        StringBuilder petList = new StringBuilder();

        try (Connection connection = SQLiteDataSource.getConnection();
             PreparedStatement ps = connection.prepareStatement("SELECT pet FROM hatch_log WHERE user_id = ?")) {
            ps.setString(1, userId);

            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    String pet = rs.getString("pet");
                    petList.append(pet).append("\n");
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return petList.toString();
    }

    private MessageEmbed buildPetListEmbed(String petList) {
        // Create an embed to display the user's pets
        EmbedBuilder embedBuilder = new EmbedBuilder();
        embedBuilder.setTitle("Hatched Pets");
        embedBuilder.setDescription(petList);
        embedBuilder.setColor(0xDD2D4A); // Set the embed color to a suitable value

        return embedBuilder.build();
    }
}
