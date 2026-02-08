package org.example.items;

import net.dv8tion.jda.api.entities.User;
import net.dv8tion.jda.api.entities.emoji.Emoji;
import net.dv8tion.jda.api.events.interaction.command.SlashCommandInteractionEvent;
import org.example.ItemManager;
import org.example.types.ItemId;
import org.example.types.UserId;

public class ShinyItem extends Item {
	private final ItemManager itemMgr;

	public ShinyItem(ItemManager itemMgr) {
		super(ItemId.SHINY, "Shiny", "Looks shiny", Emoji.fromUnicode("✨"));
		this.itemMgr = itemMgr;
	}

	@Override
	public boolean use(SlashCommandInteractionEvent event) {
		User user = event.getUser();
		UserId userId = UserId.of(user);

		if (itemMgr.hasItem(userId, ItemId.SHINY)) {

			itemMgr.giveItem(userId, ItemId.KEY);
			event.reply("**This item sparkles ✨ and explodes 💥 into a key!**").queue();
			return false;
		}
		return false;
	}
}
