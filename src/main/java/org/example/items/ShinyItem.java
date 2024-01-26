package org.example.items;

import net.dv8tion.jda.api.entities.User;
import net.dv8tion.jda.api.entities.emoji.Emoji;
import net.dv8tion.jda.api.events.interaction.command.SlashCommandInteractionEvent;
import org.example.ItemManager;
import org.example.Main;

public class ShinyItem extends Item {
    private final ItemManager im = Main.itemManager;

    public ShinyItem() {
        super("Shiny", "Looks shiny", "shiny", Emoji.fromUnicode("✨"), 0);
    }

    @Override
    public boolean use(SlashCommandInteractionEvent event) {
        User user = event.getUser();
        String userId = user.getId();

        if (im.hasItem(userId, "shiny")) {

            im.giveItem(userId, "key");
            event.reply("**This item sparkles ✨ and explodes 💥 into a key!**").queue();
            return false;
        }
        return false;
    }
}
