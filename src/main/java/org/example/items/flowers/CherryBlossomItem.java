package org.example.items.flowers;

import net.dv8tion.jda.api.entities.emoji.Emoji;
import org.example.items.Item;

import java.lang.reflect.Member;

public class CherryBlossomItem extends Item {

    public CherryBlossomItem() {
        super("Cherry Blossom", "very pretty.", "cherry_blossom", Emoji.fromUnicode("🌸"), 15);
    }

    @Override
    public void use(Member owner) {

    }
}
