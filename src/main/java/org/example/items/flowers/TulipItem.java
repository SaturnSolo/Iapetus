package org.example.items.flowers;

import net.dv8tion.jda.api.entities.emoji.Emoji;
import net.dv8tion.jda.api.events.interaction.command.SlashCommandInteractionEvent;
import org.example.items.Item;

import java.lang.reflect.Member;

public class TulipItem extends Item {
    public TulipItem() {
        super("Pink Tulip", "it is very pretty.", "tulip", Emoji.fromUnicode("🌷"), 10);
    }

    @Override
    public boolean use(SlashCommandInteractionEvent event) {
        event.reply(event.getUser().getAsMention() + "** has eaten " + this.getString(true) + "**").queue();
        return true;
    }
}
