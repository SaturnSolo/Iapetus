package org.example.commands.shop;

import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.events.interaction.command.SlashCommandInteractionEvent;
import org.example.ItemManager;
import org.example.database.Database;
import org.example.items.Item;
import org.example.structures.IapetusCommand;
import org.example.structures.Inventory;
import org.example.types.UserId;
import org.example.utils.IapetusColor;

import java.util.Map;
import java.util.stream.Collectors;

public class InventoryCommand extends IapetusCommand {
	private final ItemManager itemMgr;

	public InventoryCommand(ItemManager itemMgr) {
		super("inventory", "opens your inventory");
		this.itemMgr = itemMgr;
	}

	@Override
	public boolean runCommand(SlashCommandInteractionEvent event) {
		UserId userId = UserId.of(event.getUser());

		Inventory inventory = Database.getUserInventory(userId, itemMgr);
		EmbedBuilder embed = new EmbedBuilder().setTitle("Inventory").setColor(IapetusColor.BODY)
				.setDescription(inventory.isEmpty() ? "Your inventory is empty." : "");

		// Quantity map
		Map<Item, Integer> itemCounts = inventory.getItems().stream()
				.collect(Collectors.groupingBy(item -> item, Collectors.summingInt(item -> 1)));

		itemCounts.forEach((item, count) -> embed.addField(item.getString(),
				"> " + item.getDescription() + "\nQuantity: %d".formatted(count), true));
		event.replyEmbeds(embed.build()).queue();

		return true;
	}
}
