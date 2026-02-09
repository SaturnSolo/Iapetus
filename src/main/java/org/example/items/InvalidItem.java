package org.example.items;

import net.dv8tion.jda.api.entities.emoji.Emoji;
import net.dv8tion.jda.api.events.interaction.command.SlashCommandInteractionEvent;

public class InvalidItem extends Item {
	public InvalidItem(String name) {
		super("invaliditem-" + name, name, "a mysterious item... it hasn't been registered", Emoji.fromUnicode("❓"));
	}

	@Override
	public boolean use(SlashCommandInteractionEvent event) {
		return false;
	}
}
