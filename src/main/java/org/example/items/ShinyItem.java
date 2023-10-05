package org.example.items;

import net.dv8tion.jda.api.entities.emoji.Emoji;
import net.dv8tion.jda.api.events.interaction.command.SlashCommandInteractionEvent;

public class ShinyItem extends Item {
    public ShinyItem() {
        super("Shiny", "Looks shiny", "shiny", Emoji.fromUnicode("✨"), 0);
    }

    @Override
    public boolean use(SlashCommandInteractionEvent event) {
        return false;
    }
}
