package org.example.commands.shop;

import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.components.actionrow.ActionRow;
import net.dv8tion.jda.api.components.buttons.Button;
import net.dv8tion.jda.api.entities.MessageEmbed;
import net.dv8tion.jda.api.events.interaction.command.SlashCommandInteractionEvent;
import org.example.ButtonManager;
import org.example.ItemManager;
import org.example.buttons.ShopButton;
import org.example.structures.IapetusCommand;
import org.example.types.ItemId;
import org.example.utils.IapetusColor;

import java.util.Map;

public class ShopCommand extends IapetusCommand {
	private static final Map<ItemId, Integer> PRICES = Map.of(ItemId.EGG, 5, ItemId.ROSE, 10, ItemId.TULIP, 10,
			ItemId.CHERRY_BLOSSOM, 15);

	private final ButtonManager buttonMgr;

	public ShopCommand(ButtonManager buttonMgr, ItemManager itemMgr) {
		super("shop", "open the shop menu");
		this.buttonMgr = buttonMgr;

		// create the buttons, this lets the ButtonManager know what to run when a
		// button with the id is clicked.
		PRICES.forEach(
				(itemId, cost) -> buttonMgr.addButtons(new ShopButton(itemId.key(), itemMgr.getItem(itemId), cost)));
	}

	@Override
	public boolean runCommand(SlashCommandInteractionEvent event) {
		MessageEmbed embed = new EmbedBuilder().setTitle("Welcome to the Shop!").setDescription("""
				**`A shiny egg it seems to be glowing` **🥚 - 🍓5
				**`Surprisingly no thorns`** 🌹 - 🍓10
				**`A pretty tulip`** 🌷 - 🍓10
				**`Sakura Cherry Blossoms Pretty`** 🌸 - 🍓15""").setColor(IapetusColor.PINK).build();

		// fetch each button from the button manager.
		Button eggButton = buttonMgr.getButton("egg");
		Button roseButton = buttonMgr.getButton("rose");
		Button tulipButton = buttonMgr.getButton("tulip");
		Button cherryBlossomButton = buttonMgr.getButton("cherry_blossom");

		// Send the initial message with buttons
		event.replyEmbeds(embed).addComponents(ActionRow.of(eggButton, roseButton, tulipButton, cherryBlossomButton))
				.queue();
		return true;
	}
}
