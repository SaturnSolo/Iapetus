package org.example.items;

import net.dv8tion.jda.api.entities.emoji.Emoji;
import net.dv8tion.jda.api.events.interaction.command.SlashCommandInteractionEvent;
import org.example.types.ItemId;

public class SkullItem extends Item {
	public SkullItem() {
		super(ItemId.SKULL, "Skull", "Wow a decorative skull", Emoji.fromUnicode("💀"));
	}

	@Override
	public boolean use(SlashCommandInteractionEvent event) {
		return false;
	}
}
