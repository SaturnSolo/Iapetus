package org.example.items;

import net.dv8tion.jda.api.entities.emoji.Emoji;

import java.lang.reflect.Member;

public class EggItem extends Item {
    public EggItem() {
        super("Egg", "it has an odd sparkle..", "egg", Emoji.fromUnicode("🥚"), 5);
    }

    @Override
    public void use(Member member) {
        // hatch or throw it?
    }
}
