package org.example.items;

import net.dv8tion.jda.api.entities.emoji.Emoji;
import net.dv8tion.jda.api.events.interaction.command.SlashCommandInteractionEvent;
import org.example.types.ItemId;

public class GemItem extends Item {
	public GemItem() {
		super(ItemId.GEM, "Gem", "Looks like a gem stone of some kind it's sparkly", Emoji.fromUnicode("💎"));
	}

	@Override
	public boolean use(SlashCommandInteractionEvent event) {
		event.reply("""
				💎✨
				**Gem sparkles**""").queue();
		return false;
	}
}
