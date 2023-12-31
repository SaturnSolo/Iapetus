package org.example.commands.channel;

import net.dv8tion.jda.api.entities.Guild;
import net.dv8tion.jda.api.entities.Member;
import net.dv8tion.jda.api.entities.channel.Channel;
import net.dv8tion.jda.api.events.interaction.command.SlashCommandInteractionEvent;
import net.dv8tion.jda.api.interactions.commands.OptionType;
import net.dv8tion.jda.api.interactions.commands.build.Commands;
import org.example.structures.IapetusCommand;
import org.example.utils.ChannelUtils;
import org.example.utils.MemberUtils;

public class IgnoreChannelCommand extends IapetusCommand {
    public IgnoreChannelCommand() {
        super(Commands.slash("ignore-channel", "ignore channels")
          .addOption(OptionType.CHANNEL, "channel-1", "choose channels for to ignore", true));
    }

    @Override
    public boolean runCommand(SlashCommandInteractionEvent event) {
        Member member = event.getMember();
        Guild guild = event.getGuild();
        Channel channel = event.getOption("channel-1").getAsChannel();
        String mention = channel.getAsMention();

        if (!MemberUtils.hasAdminPermission(member)) {
            event.reply("**You need administrator permission to use this command.**").setEphemeral(true).queue();
            return true;
        }

        ChannelUtils.addIgnoredChannel(guild, channel);
        event.reply(mention + " **has been ignored.**").setEphemeral(true).queue();
        return true;
    }
}
