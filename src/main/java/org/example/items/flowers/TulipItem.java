package org.example.items.flowers;

import net.dv8tion.jda.api.entities.emoji.Emoji;
import net.dv8tion.jda.api.events.interaction.command.SlashCommandInteractionEvent;
import org.example.items.Item;
import org.example.types.ItemId;

public class TulipItem extends Item {
	public TulipItem() {
		super(ItemId.TULIP, "Pink Tulip", "it is very pretty.", Emoji.fromUnicode("🌷"));
	}

	@Override
	public boolean use(SlashCommandInteractionEvent event) {
		event.reply(event.getUser().getAsMention() + "** has eaten " + this.getString(true) + "**").queue();
		return true;
	}
}
