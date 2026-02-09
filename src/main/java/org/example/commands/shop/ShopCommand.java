package org.example.commands.shop;

import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.components.actionrow.ActionRow;
import net.dv8tion.jda.api.components.buttons.Button;
import net.dv8tion.jda.api.entities.MessageEmbed;
import net.dv8tion.jda.api.events.interaction.command.SlashCommandInteractionEvent;
import net.dv8tion.jda.api.events.interaction.component.ButtonInteractionEvent;
import org.example.ButtonModule;
import org.example.Economy;
import org.example.ItemManager;
import org.example.items.Item;
import org.example.structures.IapetusCommand;
import org.example.types.ItemId;
import org.example.types.UserId;
import org.example.utils.IapetusColor;

import java.util.Map;

public class ShopCommand extends IapetusCommand implements ButtonModule {
	private static final Map<ItemId, Integer> PRICES = Map.of(ItemId.EGG, 5, ItemId.ROSE, 10, ItemId.TULIP, 10,
			ItemId.CHERRY_BLOSSOM, 15);

	private final ItemManager itemMgr;
	private final Economy economy;

	public ShopCommand(ItemManager itemMgr, Economy economy) {
		super("shop", "open the shop menu");
		this.itemMgr = itemMgr;
		this.economy = economy;
	}

	@Override
	public boolean runCommand(SlashCommandInteractionEvent event) {
		MessageEmbed embed = new EmbedBuilder().setTitle("Welcome to the Shop!").setDescription("""
				**`A shiny egg it seems to be glowing` **🥚 - 🍓5
				**`Surprisingly no thorns`** 🌹 - 🍓10
				**`A pretty tulip`** 🌷 - 🍓10
				**`Sakura Cherry Blossoms Pretty`** 🌸 - 🍓15""").setColor(IapetusColor.PINK).build();

		Button eggButton = shopButton(ItemId.EGG);
		Button roseButton = shopButton(ItemId.ROSE);
		Button tulipButton = shopButton(ItemId.TULIP);
		Button cherryBlossomButton = shopButton(ItemId.CHERRY_BLOSSOM);

		event.replyEmbeds(embed).addComponents(ActionRow.of(eggButton, roseButton, tulipButton, cherryBlossomButton))
				.queue();
		return true;
	}

	@Override
	public void onButton(String id, ButtonInteractionEvent event) {
		ItemId itemId;
		try {
			itemId = ItemId.valueOf(id.toUpperCase());
		} catch (IllegalArgumentException e) {
			return;
		}

		Integer cost = PRICES.get(itemId);
		if (cost == null)
			return;

		boolean success = economy.purchase(UserId.of(event.getUser()), itemId, cost);
		if (success) {
			Item item = itemMgr.getItem(itemId);
			event.reply("**You have purchased a %s and added it to your inventory!**".formatted(item.getString()))
					.setEphemeral(true).queue();
		} else {
			event.reply("**Insufficient berries!**").setEphemeral(true).queue();
		}
	}

	private Button shopButton(ItemId itemId) {
		Item item = itemMgr.getItem(itemId);
		return Button.secondary("shop:" + itemId.key(), item.getName()).withEmoji(item.getIconEmoji());
	}
}
