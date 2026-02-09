package org.example.commands;

import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.entities.MessageEmbed;
import net.dv8tion.jda.api.events.interaction.command.SlashCommandInteractionEvent;
import org.example.Economy;
import org.example.ItemManager;
import org.example.database.Database;
import org.example.structures.IapetusCommand;
import org.example.types.ItemId;
import org.example.types.UserId;
import org.example.utils.IapetusColor;

import java.util.Random;

public class LootChestCommands extends IapetusCommand {
	private static final ItemId[] POSSIBLE_LOOT = {ItemId.GEM, ItemId.ROSE, ItemId.DICE, ItemId.SHINY, ItemId.KEY};

	private final ItemManager itemMgr;
	private final Economy economy;
	private final Random rng;

	public LootChestCommands(ItemManager itemMgr, Economy economy, Random rng) {
		super("lootchest", "open loot chest with a key");
		this.itemMgr = itemMgr;
		this.economy = economy;
		this.rng = rng;
	}

	@Override
	public boolean runCommand(SlashCommandInteractionEvent event) {
		UserId userId = UserId.of(event.getUser());

		if (itemMgr.hasItem(userId, ItemId.KEY)) {
			ItemId loot = openRandomLoot();
			Database.logLootInDatabase(userId.value(), loot.key());

			itemMgr.takeItem(userId, ItemId.KEY);
			MessageEmbed embed = new EmbedBuilder().setTitle("Opening a chest")
					.setDescription("You opened a chest and got: 5 berries and %s".formatted(loot.key()))
					.setColor(IapetusColor.RED).build();

			economy.reward(userId, 5);
			itemMgr.giveItem(userId, loot);

			event.replyEmbeds(embed).queue();
		} else {
			event.reply("You don't have a key.").queue();
		}
		return true;
	}

	private ItemId openRandomLoot() {
		return POSSIBLE_LOOT[rng.nextInt(POSSIBLE_LOOT.length)];
	}
}
