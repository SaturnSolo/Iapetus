package org.example.items;

import net.dv8tion.jda.api.entities.emoji.Emoji;
import net.dv8tion.jda.api.events.interaction.command.SlashCommandInteractionEvent;

public class GemItem extends Item {
    public GemItem() {
        super("Gem", "Looks like a gem stone of some kind it's sparkly", "gem", Emoji.fromUnicode("💎"), 0);
    }

    @Override
    public boolean use(SlashCommandInteractionEvent event) {
        event.reply("💎✨ \n" +
                "**Gem sparkles**").queue();
        return false;
    }
}
