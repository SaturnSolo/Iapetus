package org.example.commands;

import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.entities.User;
import net.dv8tion.jda.api.events.interaction.command.SlashCommandInteractionEvent;
import net.dv8tion.jda.api.interactions.commands.build.SlashCommandData;
import org.example.ItemManager;
import org.example.Main;
import org.example.database.SQLiteDataSource;
import org.example.structures.IapetusCommand;
import org.example.utils.BerryUtils;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Random;

public class LootChestCommands extends IapetusCommand {
    private final ItemManager im = Main.itemManager;

    public LootChestCommands() {
        super("lootchest", "open loot chest with a key");
            System.out.println("ran");
    }

    @Override
    public boolean runCommand(SlashCommandInteractionEvent event) {
        User user = event.getUser();
        String userId = user.getId();


        if (im.hasItem(userId, "key")) {

            String loot = openRandomLoot();


            logLootInDatabase(userId, loot);

            im.takeItem(userId, "key");


            EmbedBuilder embedBuilder = new EmbedBuilder();
            embedBuilder.setTitle("Opening a chest");
            embedBuilder.setDescription("You opened a chest and got: 5 berries and " + loot);
            embedBuilder.setColor(0x998FC7); // Green color

            im.giveItem(userId, loot);
            BerryUtils.giveUserBerries(userId, 5);


            event.replyEmbeds(embedBuilder.build()).queue();
        } else {
            event.reply("You don't have a key.").queue();
        }
        return true;
    }

    private String openRandomLoot() {
        String[] possibleLoot = {"gem", "rose", "dice", "shiny"};
        Random random = new Random();
        int index = random.nextInt(possibleLoot.length);
        return possibleLoot[index];
    }

    private void logLootInDatabase(String userId, String loot) {
        try (Connection connection = SQLiteDataSource.getConnection();
             PreparedStatement ps = connection.prepareStatement("INSERT INTO key_log (user_id, loot) VALUES (?, ?)")) {
            ps.setString(1, userId);
            ps.setString(2, loot);
            ps.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}

