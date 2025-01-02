package org.example.commands.give;

import net.dv8tion.jda.api.entities.User;
import net.dv8tion.jda.api.events.interaction.command.SlashCommandInteractionEvent;
import net.dv8tion.jda.api.interactions.commands.OptionType;
import net.dv8tion.jda.api.interactions.commands.build.Commands;
import org.example.structures.IapetusCommand;
import org.example.utils.BerryUtils;


public class GiveCommand extends IapetusCommand {
    public GiveCommand() {
        super(Commands.slash("give","give berries to user")
                .addOption(OptionType.MENTIONABLE, "mention", "who to give berries to", true)
                .addOption(OptionType.INTEGER, "give", "number of berries to give", true)
        );
    }
    @Override
    public boolean runCommand(SlashCommandInteractionEvent event) {
        String source_user_id = event.getMember().getId();
        User target_user_id = event.getOption("mention").getAsUser();
        int transfer_amount = event.getOption("give").getAsInt();

        if (transfer_amount <= 0) {
            event.reply("You must specify an amount greater than 0.").setEphemeral(true).queue();
            return false;
        }

        String targetUserId = target_user_id.getId();

        if (source_user_id.equals(targetUserId)) {
            event.reply("You cannot give berries to yourself.").setEphemeral(true).queue();
            return false;
        }

        var total_berries = BerryUtils.getUserBerries(source_user_id);
        if (total_berries < transfer_amount) {
            event.reply("You do not have enough berries to transfer.").setEphemeral(true).queue();
            return false;
        }
        BerryUtils.takeUserBerries(source_user_id, transfer_amount);
        BerryUtils.giveUserBerries(target_user_id, transfer_amount);

        event.reply("You have successfully transferred " + transfer_amount + " berries to " +
                target_user_id.getAsMention() + "!").queue();

        return true;
    }
}
