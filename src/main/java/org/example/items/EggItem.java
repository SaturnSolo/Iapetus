package org.example.items;

import net.dv8tion.jda.api.entities.emoji.Emoji;
import net.dv8tion.jda.api.events.interaction.command.SlashCommandInteractionEvent;

import java.lang.reflect.Member;

public class EggItem extends Item {
    public EggItem() {
        super("Egg", "it has an odd sparkle..", "egg", Emoji.fromUnicode("🥚"), 5);
    }

    @Override
    public void use(SlashCommandInteractionEvent event) {
        // hatch or throw it?
    }
}
