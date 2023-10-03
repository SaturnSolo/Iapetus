package org.example.items.flowers;

import net.dv8tion.jda.api.entities.emoji.Emoji;
import net.dv8tion.jda.api.events.interaction.command.SlashCommandInteractionEvent;
import org.example.items.Item;

import java.lang.reflect.Member;

public class RoseItem extends Item {
    public RoseItem() {
        super("Rose", "surprisingly no thorns.", "rose", Emoji.fromUnicode("🌹"), 10);
    }

    @Override
    public void use(SlashCommandInteractionEvent event) {
        event.reply( event.getUser().getAsMention() + "** has eaten " + this.getString(true)+ "**").queue();
    }
}
