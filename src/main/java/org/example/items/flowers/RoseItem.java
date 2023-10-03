package org.example.items.flowers;

import net.dv8tion.jda.api.entities.emoji.Emoji;
import org.example.items.Item;

import java.lang.reflect.Member;

public class RoseItem extends Item {
    public RoseItem() {
        super("Rose", "surprisingly no thorns.", "tulip", Emoji.fromUnicode("🌹"), 10);
    }

    @Override
    public void use(Member owner) {

    }
}
