package org.example.commands;

import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.entities.MessageEmbed;
import net.dv8tion.jda.api.entities.User;
import net.dv8tion.jda.api.events.interaction.command.SlashCommandInteractionEvent;
import org.example.ItemManager;
import org.example.database.Database;
import org.example.structures.IapetusCommand;
import org.example.utils.IapetusColor;

import java.util.Random;

public class LootChestCommands extends IapetusCommand {
    private final ItemManager itemMgr;
    private final Random rng;

    public LootChestCommands(ItemManager itemMgr, Random rng) {
        super("lootchest", "open loot chest with a key");
        this.itemMgr = itemMgr;
        this.rng = rng;
    }

    @Override
    public boolean runCommand(SlashCommandInteractionEvent event) {
        User user = event.getUser();
        String userId = user.getId();

        if (itemMgr.hasItem(userId, "key")) {
            String loot = openRandomLoot();
            Database.logLootInDatabase(userId, loot);

            itemMgr.takeItem(userId, "key");
            MessageEmbed embed = new EmbedBuilder().setTitle("Opening a chest").setDescription("You opened a chest and got: 5 berries and %s".formatted(loot)).setColor(IapetusColor.RED).build();

            Database.giveBerries(userId, 5);
            itemMgr.giveItem(userId, loot);

            event.replyEmbeds(embed).queue();
        } else {
            event.reply("You don't have a key.").queue();
        }
        return true;
    }

    private String openRandomLoot() {
        String[] possibleLoot = {"gem", "rose", "dice", "shiny", "key"};
        return possibleLoot[rng.nextInt(possibleLoot.length)];
    }
}

