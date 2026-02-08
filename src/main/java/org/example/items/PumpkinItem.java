package org.example.items;

import net.dv8tion.jda.api.entities.emoji.Emoji;
import net.dv8tion.jda.api.events.interaction.command.SlashCommandInteractionEvent;
import org.example.types.ItemId;

public class PumpkinItem extends Item {
	public PumpkinItem() {
		super(ItemId.PUMPKIN, "Pumpkin", "It is a pumpkin", Emoji.fromUnicode("🎃"));
	}

	@Override
	public boolean use(SlashCommandInteractionEvent event) {
		event.reply("This item is a collectable!").queue();
		return false;
	}
}
