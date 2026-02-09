package org.example.items;

import net.dv8tion.jda.api.entities.emoji.Emoji;
import net.dv8tion.jda.api.events.interaction.command.SlashCommandInteractionEvent;
import org.example.types.ItemId;

public class EggItem extends Item {
	public EggItem() {
		super(ItemId.EGG, "Egg", "it has an odd sparkle..", Emoji.fromUnicode("🥚"));
	}

	@Override
	public boolean use(SlashCommandInteractionEvent event) {
		event.reply("**The egg reflects the light of the sun. Try `/hatch`ing it?**").setEphemeral(true).queue();
		return false;
	}
}
