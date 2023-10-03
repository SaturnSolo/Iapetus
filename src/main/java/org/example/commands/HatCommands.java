package org.example.commands;

import net.dv8tion.jda.api.events.interaction.command.SlashCommandInteractionEvent;
import net.dv8tion.jda.api.interactions.commands.OptionMapping;
import net.dv8tion.jda.api.interactions.commands.OptionType;
import net.dv8tion.jda.api.interactions.commands.build.Commands;
import net.dv8tion.jda.api.interactions.commands.build.OptionData;
import org.example.ItemManager;
import org.example.Main;
import org.example.items.Item;
import org.example.structures.IapetusCommand;

public class HatCommands extends IapetusCommand {
    final ItemManager im = Main.itemManager;
    public HatCommands() {
        super(Commands.slash("use", "to use an item")
                .addOptions(new OptionData(OptionType.STRING, "item", "use item")
                        .addChoice("🌹","rose")
                        .addChoice("🌷", "tulip")
                        .addChoice("🌸", "cherry_blossom"))

        );
    }

    @Override
    public boolean runCommand(SlashCommandInteractionEvent event) {
        String choice = event.getOption("item").getAsString();
        String user = event.getUser().getAsMention();
        String userId = event.getUser().getId();

        if (!im.hasItem(userId, choice)) {
            event.reply( "**you ate air. you don't have the item.**").setEphemeral(true).queue();
            return true;
        }
        Item item = im.getItem(choice);
        im.takeItem(userId, choice);
        event.reply( user + "** has eaten **" + item.getString(true)).queue();
        return true;
    }
}
