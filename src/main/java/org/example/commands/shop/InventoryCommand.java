package org.example.commands.shop;

import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.events.interaction.command.SlashCommandInteractionEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;
import org.example.database.SQLiteDataSource;
import org.example.structures.IapetusCommand;

import java.awt.*;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class InventoryCommand extends IapetusCommand {
    public InventoryCommand() {
        super("inventory","opens your inventory");
    }

    @Override
    public boolean runCommand(SlashCommandInteractionEvent event) {
        String userId = event.getMember().getId();

        try (final Connection connection = SQLiteDataSource.getConnection();
             final PreparedStatement ps = connection.prepareStatement("SELECT item_name, COUNT(*) AS item_count FROM inventory WHERE user_id = ? GROUP BY item_name")) {
            ps.setString(1, userId);

            try (final ResultSet rs = ps.executeQuery()) {
                EmbedBuilder embedBuilder = new EmbedBuilder();
                embedBuilder.setTitle("Inventory");
                embedBuilder.setColor(0xFAA275);

                boolean hasItems = false;
                while (rs.next()) {
                    String itemName = rs.getString("item_name");
                    int itemCount = rs.getInt("item_count");

                    embedBuilder.addField(itemName, "Quantity: " + itemCount, true);
                    hasItems = true;
                }

                if (!hasItems) {
                    embedBuilder.setDescription("Your inventory is empty.");
                }

                event.replyEmbeds(embedBuilder.build()).queue();
            }
        } catch (SQLException e) {
            e.printStackTrace();
            event.reply("An error occurred while retrieving your inventory.").queue();
        }
        return true;
    }
}