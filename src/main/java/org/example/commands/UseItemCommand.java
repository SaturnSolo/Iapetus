package org.example.commands;

import net.dv8tion.jda.api.events.interaction.command.SlashCommandInteractionEvent;
import net.dv8tion.jda.api.interactions.commands.Command;
import net.dv8tion.jda.api.interactions.commands.OptionType;
import net.dv8tion.jda.api.interactions.commands.build.Commands;
import net.dv8tion.jda.api.interactions.commands.build.OptionData;
import org.example.ItemManager;
import org.example.Main;
import org.example.items.Item;
import org.example.structures.IapetusCommand;

import java.util.ArrayList;
import java.util.Collection;

public class UseItemCommand extends IapetusCommand {
    final ItemManager im = Main.itemManager;
    public UseItemCommand() {
        super(Commands.slash("use", "to use an item")
            .addOptions(new OptionData(OptionType.STRING, "item", "use item", true)
              .addChoices(getChoices())
            )
        );
    }

    @Override
    public boolean runCommand(SlashCommandInteractionEvent event) {
        String choice = event.getOption("item").getAsString();
        String userId = event.getUser().getId();

        if (!im.hasItem(userId, choice)) {
            event.reply( "**You don't have this item, so instead you ate air.**").setEphemeral(true).queue();
            return true;
        }
        Item item = im.getItem(choice);
        if (item.use(event)) im.takeItem(userId, choice);
        return true;
    }

    private static Collection<Command.Choice> getChoices() {
        Collection<Command.Choice> choices = new ArrayList<>();
        Main.itemManager.getItems().forEach((id,item) -> choices.add(new Command.Choice(item.getString(true),id)));
        return choices;
    }
}
