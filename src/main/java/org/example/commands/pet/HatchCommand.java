package org.example.commands.pet;
import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.entities.User;
import net.dv8tion.jda.api.events.interaction.command.SlashCommandInteractionEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;
import org.example.database.SQLiteDataSource;
import org.example.structures.IapetusCommand;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Random;


public class HatchCommand extends IapetusCommand {
    public HatchCommand() {
        super("hatch", "hatches a pet out of an egg");
    }

    @Override
    public boolean runCommand(SlashCommandInteractionEvent event) {
        User user = event.getUser();
        String userId = user.getId();

        // Check if the user has an egg in their inventory (You need to implement this logic)
        if (hasEggInInventory(userId)) {
            // If the user has an egg, hatch it into a random pet
            String hatchedPet = hatchRandomPet();

            // Log the hatching in the database
            logHatchInDatabase(userId, hatchedPet);

            // Remove the egg from the inventory
            removeEggFromInventory(userId); // Add this line

            // Create an embed for the hatched pet
            EmbedBuilder embedBuilder = new EmbedBuilder();
            embedBuilder.setTitle("Hatching a Pet");
            embedBuilder.setDescription("You hatched a new pet: " + hatchedPet);
            embedBuilder.setColor(0x998FC7); // Green color

            // Send the pet hatching embed
            event.replyEmbeds(embedBuilder.build()).queue();
        } else {
            event.reply("You don't have an egg to hatch.").queue();
        }
        return true;
    }

    private boolean hasEggInInventory(String userId) {
        try (final Connection connection = SQLiteDataSource.getConnection();
             final PreparedStatement ps = connection.prepareStatement("SELECT COUNT(*) FROM inventory WHERE user_id = ? AND item_name = '🥚'")) {
            ps.setString(1, userId);

            try (final ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    int count = rs.getInt(1);
                    return count > 0;
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }
    private void removeEggFromInventory(String userId) {
        try (final Connection connection = SQLiteDataSource.getConnection();
             final PreparedStatement ps = connection.prepareStatement("DELETE FROM inventory WHERE user_id = ? AND item_name = '🥚'")) {
            ps.setString(1, userId);
            ps.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    private String hatchRandomPet() {
            String[] possiblePets = {"🐢", "🦃", "🐈", "🐕","🐑","🦌","🐂","🐄","🦎","🐍","🐣","🦐", "🦩", "🐌", "🦢","🐊","🦙"};
            Random random = new Random();
            int index = random.nextInt(possiblePets.length);
            return possiblePets[index];
        }

        private void logHatchInDatabase(String userId, String pet) {
            // Implement logic to log the hatching in your SQLite database
            // Replace this with your database insertion logic
            try (Connection connection = SQLiteDataSource.getConnection();
                 PreparedStatement ps = connection.prepareStatement("INSERT INTO hatch_log (user_id, pet) VALUES (?, ?)")) {
                ps.setString(1, userId);
                ps.setString(2, pet);
                ps.executeUpdate();
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
    }



