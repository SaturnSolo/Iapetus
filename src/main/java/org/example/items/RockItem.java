package org.example.items;

import net.dv8tion.jda.api.entities.emoji.Emoji;
import net.dv8tion.jda.api.events.interaction.command.SlashCommandInteractionEvent;

public class RockItem extends Item {
    public RockItem() {
        super("Rock", "It's a rock", "rock", Emoji.fromUnicode("\uD83E\uDEA8"), 0);
    }

    @Override
    public boolean use(SlashCommandInteractionEvent event) {
        event.reply("This item doesn't have a use yet").queue();
        return false;
    }
}
