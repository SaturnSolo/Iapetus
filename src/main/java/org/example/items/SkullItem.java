package org.example.items;

import net.dv8tion.jda.api.entities.emoji.Emoji;
import net.dv8tion.jda.api.events.interaction.command.SlashCommandInteractionEvent;

public class SkullItem extends Item {
	public SkullItem() {
		super("Skull", "Wow a decorative skull", "skull", Emoji.fromUnicode("💀"));
	}

	@Override
	public boolean use(SlashCommandInteractionEvent event) {
		return false;
	}
}
