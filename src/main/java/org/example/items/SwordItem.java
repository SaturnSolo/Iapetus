package org.example.items;

import net.dv8tion.jda.api.entities.emoji.Emoji;
import net.dv8tion.jda.api.events.interaction.command.SlashCommandInteractionEvent;
import org.example.types.ItemId;

public class SwordItem extends Item {
	public SwordItem() {
		super(ItemId.SWORD, "Sword", "A nice silver sword.", Emoji.fromUnicode("⚔️"));
	}

	@Override
	public boolean use(SlashCommandInteractionEvent event) {
		return false;
	}
}
