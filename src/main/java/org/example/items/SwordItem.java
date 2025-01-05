package org.example.items;

import net.dv8tion.jda.api.entities.emoji.Emoji;
import net.dv8tion.jda.api.events.interaction.command.SlashCommandInteractionEvent;

public class SwordItem extends Item{
    public SwordItem(String name, String description, String id) {
        super("Sword", "A nice silver sword.", "sword", Emoji.fromUnicode("⚔️"), 0);
    }

    @Override
    public boolean use(SlashCommandInteractionEvent event) {
        return false;
    }
}
