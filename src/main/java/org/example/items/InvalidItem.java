package org.example.items;

import net.dv8tion.jda.api.entities.emoji.Emoji;
import net.dv8tion.jda.api.events.interaction.command.SlashCommandInteractionEvent;

import java.lang.reflect.Member;

public class InvalidItem extends Item {
    public InvalidItem(String name) {
        super(name, "a mysterious item... it hasn't been registered", "invaliditem-"+name, Emoji.fromUnicode("❓"));
    }

    @Override
    public void use(SlashCommandInteractionEvent event) {
        // something later
    }
}
