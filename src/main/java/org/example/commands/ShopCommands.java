package org.example.commands;


import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.entities.MessageEmbed;
import net.dv8tion.jda.api.events.interaction.command.SlashCommandInteractionEvent;
import net.dv8tion.jda.api.events.interaction.component.ButtonInteractionEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;
import net.dv8tion.jda.api.interactions.components.buttons.Button;
import org.example.database.SQLiteDataSource;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class ShopCommands extends ListenerAdapter {

    @Override
    public void onSlashCommandInteraction(SlashCommandInteractionEvent event) {
        if (event.getName().equals("shop")) {
            EmbedBuilder embedBuilder = new EmbedBuilder();
            embedBuilder.setTitle("Welcome to the Shop!");
            embedBuilder.setDescription("A shiny egg it seems to be glowing 🥚 - 🍓5\n" +
                    "Surprisingly no thorns 🌹 - 🍓10\n" +
                    "A pretty tulip 🌷 - 🍓10\n" +
                    "Sakura Cherry Blossoms Pretty 🌸 - 🍓15");
            embedBuilder.setColor(0xCE6A85); // You can change the color to your preference

            // Create buttons for each item
            Button eggButton = Button.primary("egg", "🥚");
            Button roseButton = Button.primary("rose", "🌹");
            Button tulipButton = Button.primary("tulip", "🌷");
            Button cherryBlossomButton = Button.primary("cherry_blossom", "🌸");

            // Add buttons to the embed as components
            MessageEmbed embed = embedBuilder.build();

            // Send the initial message with buttons
            event.replyEmbeds(embed).addActionRow(eggButton, roseButton, tulipButton, cherryBlossomButton).queue();

        }
    }

    @Override
    public void onButtonInteraction(ButtonInteractionEvent event) {
        String componentId = event.getComponentId();
        String userId = event.getMember().getId();
        int berriesToDeduct = 0;
        String itemName = "";

        if (componentId.equals("egg")) {
            berriesToDeduct = 5;
            itemName = "🥚";
        } else if (componentId.equals("tulip")) {
            berriesToDeduct = 10;
            itemName = "🌷";
        } else if (componentId.equals("rose")) {
            berriesToDeduct = 10;
            itemName = "🌹";
        }
        else if (componentId.equals("cherry_blossom")) {
            berriesToDeduct = 15;
            itemName = "🌸";
        }

        if (berriesToDeduct > 0) {
            int userBerries = getUserBerries(userId);
            if (userBerries >= berriesToDeduct) {
                deductBerries(userId, berriesToDeduct);
                addToInventory(userId, itemName);
                event.reply("**You have purchased a " + itemName + " and added it to your inventory!**").setEphemeral(true).queue();
            } else {
                event.reply("**Insufficient berries!**").setEphemeral(true).queue();
            }
        }
    }

    private int getUserBerries(String userId) {
        int userBerries = 0;
        try (Connection connection = SQLiteDataSource.getConnection();
             PreparedStatement ps = connection.prepareStatement("SELECT berry_count FROM user_berries WHERE user_id = ?")) {
            ps.setString(1, userId);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    userBerries = rs.getInt("berry_count");
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return userBerries;
    }

    private void deductBerries(String userId, int berriesToDeduct) {
        try (Connection connection = SQLiteDataSource.getConnection();
             PreparedStatement ps = connection.prepareStatement("UPDATE user_berries SET berry_count = berry_count - ? WHERE user_id = ?")) {
            ps.setInt(1, berriesToDeduct);
            ps.setString(2, userId);
            ps.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    private void addToInventory(String userId, String itemName) {
        try (Connection connection = SQLiteDataSource.getConnection();
             PreparedStatement ps = connection.prepareStatement("INSERT INTO inventory (user_id, item_name) VALUES (?, ?)")) {
            ps.setString(1, userId);
            ps.setString(2, itemName);
            ps.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}

