package org.example.commands.channel;

import net.dv8tion.jda.api.entities.Guild;
import net.dv8tion.jda.api.entities.Member;
import net.dv8tion.jda.api.entities.channel.Channel;
import net.dv8tion.jda.api.events.interaction.command.SlashCommandInteractionEvent;
import org.example.structures.IapetusCommand;
import org.example.utils.ChannelUtils;
import org.example.utils.MemberUtils;

import java.util.List;

public class ListIgnoredCommand extends IapetusCommand {
    public ListIgnoredCommand() {
        super("list-ignored", "view this guilds ignored commands");
    }

    @Override
    public boolean runCommand(SlashCommandInteractionEvent event) {
        Member member = event.getMember();
        Guild guild = event.getGuild();

        if (!MemberUtils.hasAdminPermission(member)) {
            event.reply("**You need administrator permission to use this command.**").setEphemeral(true).queue();
            return true;
        }

        if (guild == null) {
            event.reply("**You must be in a guild to run this command.**").setEphemeral(true).queue();
            return true;
        }

        List<String> channels = ChannelUtils.getIgnoredChannels(guild);
        StringBuilder stringList = new StringBuilder();
        channels.forEach(id -> stringList.append("- <#").append(id).append(">\n"));
        event.reply("**List of ignored channels:**\n"+stringList).setEphemeral(true).queue();
        return true;
    }
}
