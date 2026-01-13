package org.example.commands.channel;

import net.dv8tion.jda.api.entities.Guild;
import net.dv8tion.jda.api.entities.Member;
import net.dv8tion.jda.api.entities.channel.Channel;
import net.dv8tion.jda.api.events.interaction.command.SlashCommandInteractionEvent;
import net.dv8tion.jda.api.interactions.commands.OptionType;
import net.dv8tion.jda.api.interactions.commands.build.Commands;
import org.example.database.Database;
import org.example.structures.IapetusCommand;
import org.example.utils.MemberUtils;

public class IgnoreChannelCommand extends IapetusCommand {
    public IgnoreChannelCommand() {
        super(Commands.slash("ignore-channel", "ignore channels")
          .addOption(OptionType.CHANNEL, "channel", "choose channels for to ignore", true));
    }

    @Override
    public boolean runCommand(SlashCommandInteractionEvent event) {
        Guild guild = event.getGuild();
        if(guild == null) {
            event.reply("This command can only be used in a server.").queue();
            return true;
        }

        Member member = event.getMember();
        Channel channel = event.getOption("channel").getAsChannel(); // NPE impossible
        String mention = channel.getAsMention();

        if (!MemberUtils.isAdmin(member)) {
            event.reply("**You need administrator permission to use this command.**").setEphemeral(true).queue();
            return true;
        }

        Database.addIgnoredChannel(guild, channel);
        event.reply("%s **has been ignored.**".formatted(mention)).setEphemeral(true).queue();
        return true;
    }
}
