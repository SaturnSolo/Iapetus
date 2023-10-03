package org.example.items;

import net.dv8tion.jda.api.entities.emoji.Emoji;

import java.lang.reflect.Member;

public class InvalidItem extends Item {
    public InvalidItem(String name) {
        super(name, "a mysterious item... it hasn't been registered", "invaliditem-"+name, Emoji.fromUnicode("❓"));
    }

    @Override
    public void use(Member owner) {
        // something later
    }
}
