package org.example.commands;


import net.dv8tion.jda.api.events.interaction.command.SlashCommandInteractionEvent;
import net.dv8tion.jda.api.interactions.commands.OptionType;
import net.dv8tion.jda.api.interactions.commands.build.Commands;
import org.example.structures.IapetusCommand;

public class BonkCommand extends IapetusCommand {
    public BonkCommand() {
        super(Commands.slash("bonk", "bonk someone").addOption(OptionType.MENTIONABLE, "name", "who to bonk", true)
        );
    }

    @Override
    public boolean runCommand(SlashCommandInteractionEvent event) {
        // NPE is impossible
        event.reply("%s **got bonked!** 🥖".formatted(event.getOption("name").getAsMentionable().getAsMention())).queue(); // NPE is impossible
        return true;
    }
}