package org.example.items;

import net.dv8tion.jda.api.entities.User;
import net.dv8tion.jda.api.entities.emoji.Emoji;
import net.dv8tion.jda.api.events.interaction.command.SlashCommandInteractionEvent;
import org.example.ItemManager;

public class ShinyItem extends Item {
    private final ItemManager itemMgr;

    public ShinyItem(ItemManager itemMgr) {
        super("Shiny", "Looks shiny", "shiny", Emoji.fromUnicode("✨"), 0);
        this.itemMgr = itemMgr;
    }

    @Override
    public boolean use(SlashCommandInteractionEvent event) {
        User user = event.getUser();
        String userId = user.getId();

        if (itemMgr.hasItem(userId, "shiny")) {

            itemMgr.giveItem(userId, "key");
            event.reply("**This item sparkles ✨ and explodes 💥 into a key!**").queue();
            return false;
        }
        return false;
    }
}
