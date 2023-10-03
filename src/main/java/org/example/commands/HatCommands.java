package org.example.commands;

import net.dv8tion.jda.api.events.interaction.command.SlashCommandInteractionEvent;
import net.dv8tion.jda.api.interactions.commands.OptionMapping;
import net.dv8tion.jda.api.interactions.commands.OptionType;
import net.dv8tion.jda.api.interactions.commands.build.Commands;
import net.dv8tion.jda.api.interactions.commands.build.OptionData;
import org.example.structures.IapetusCommand;

public class HatCommands extends IapetusCommand {
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
        event.reply( user + "** has eaten **" + choice ).queue();
        return true;
    }
}
