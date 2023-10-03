package org.example.items.flowers;

import net.dv8tion.jda.api.entities.emoji.Emoji;
import org.example.items.Item;

import java.lang.reflect.Member;

public class TulipItem extends Item {
    public TulipItem() {
        super("Pink Tulip", "it is very pretty.", "tulip", Emoji.fromUnicode("🌷"), 10);
    }

    @Override
    public void use(Member owner) {

    }
}
