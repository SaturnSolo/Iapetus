package org.example.items;

import net.dv8tion.jda.api.entities.emoji.Emoji;
import net.dv8tion.jda.api.events.interaction.command.SlashCommandInteractionEvent;

public class GemItem extends Item {
    public GemItem() {
        super("Gem", "Sparkles like a gem", "gem", Emoji.fromUnicode("💎"), 0);
    }

    @Override
    public boolean use(SlashCommandInteractionEvent event) {
        event.reply("This item doesn't have a use yet").queue();
        return false;
    }
}
