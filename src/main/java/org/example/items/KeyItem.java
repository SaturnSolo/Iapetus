package org.example.items;

import net.dv8tion.jda.api.entities.emoji.Emoji;
import net.dv8tion.jda.api.events.interaction.command.SlashCommandInteractionEvent;
import org.example.types.ItemId;

public class KeyItem extends Item {
	public KeyItem() {
		super(ItemId.KEY, "Key", "An old rusty key", Emoji.fromUnicode("🔑"));
	}

	@Override
	public boolean use(SlashCommandInteractionEvent event) {
		event.reply("Type /lootchest to use a key!").queue();
		return false;
	}
}
