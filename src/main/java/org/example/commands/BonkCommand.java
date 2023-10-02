package org.example.commands;


import net.dv8tion.jda.api.entities.IMentionable;
import net.dv8tion.jda.api.events.interaction.command.SlashCommandInteractionEvent;
import net.dv8tion.jda.api.interactions.commands.OptionType;
import net.dv8tion.jda.api.interactions.commands.build.Commands;
import net.dv8tion.jda.api.interactions.commands.build.SlashCommandData;
import org.example.structures.IapetusCommand;

public class BonkCommand extends IapetusCommand {
    public BonkCommand() {
        super(Commands.slash("bonk","bonk someone")
          .addOption(OptionType.MENTIONABLE, "name", "who to bonk")
        );
    }

    @Override
    public boolean runCommand(SlashCommandInteractionEvent event) {
        IMentionable mention = event.getOption("name").getAsMentionable();
        String user = mention.getAsMention();
        event.reply(user + " **got bonked!** 🥖").queue();
        return true;
    }
}