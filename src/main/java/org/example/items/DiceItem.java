package org.example.items;

import net.dv8tion.jda.api.entities.emoji.Emoji;
import net.dv8tion.jda.api.events.interaction.command.SlashCommandInteractionEvent;
import org.example.types.ItemId;

import java.util.Random;

public class DiceItem extends Item {
	private final Random rng;

	public DiceItem(Random rng) {
		super(ItemId.DICE, "Dice", "Seems rollable.", Emoji.fromUnicode("🎲"));
		this.rng = rng;
	}

	@Override
	public boolean use(SlashCommandInteractionEvent event) {
		event.reply("%s **you rolled a** %d".formatted(event.getUser().getAsMention(), rng.nextInt(20) + 1)).queue();
		return false; // item not consumed on use.
	}
}
