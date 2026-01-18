package org.example.commands;

import net.dv8tion.jda.api.entities.Guild;
import net.dv8tion.jda.api.entities.channel.unions.GuildChannelUnion;
import net.dv8tion.jda.api.events.interaction.command.SlashCommandInteractionEvent;
import net.dv8tion.jda.api.interactions.commands.OptionType;
import net.dv8tion.jda.api.interactions.commands.build.Commands;
import net.dv8tion.jda.api.interactions.commands.build.SubcommandData;
import org.example.database.Database;
import org.example.structures.IapetusCommand;
import org.example.utils.MemberUtils;

import java.util.List;

public class IgnoredChannels extends IapetusCommand {
	public IgnoredChannels() {
		super(Commands.slash("ignored-channels", "ignored channels").addSubcommands(
				new SubcommandData("add", "add a channel to ignorelist").addOption(OptionType.CHANNEL, "channel",
						"choose channels for to ignore", true),
				new SubcommandData("remove", "remove a channel from ignorelist").addOption(OptionType.CHANNEL,
						"channel", "choose channels for to ignore", true),
				new SubcommandData("list", "list all ignored channels")));
	}

	@Override
	public boolean runCommand(SlashCommandInteractionEvent event) {
		Guild guild = event.getGuild();
		if (guild == null) {
			event.reply("This command can only be used in a server.").queue();
			return true;
		}

		if (!MemberUtils.isAdmin(event.getMember())) {
			event.reply("**You need administrator permission to use this command.**").setEphemeral(true).queue();
			return true;
		}

		switch (event.getSubcommandName()) {
			case "add" : // Fall-through
			case "remove" : {
				GuildChannelUnion channel = event.getOption("channel").getAsChannel(); // NPE impossible

				if (event.getSubcommandName().equals("add")) {
					if (Database.getIgnoredChannels(guild).contains(channel.getIdLong())) {
						event.reply("**This channel is already ignored.**").setEphemeral(true).queue();
						return true;
					}

					Database.addIgnoredChannel(guild, channel);
					event.reply("%s **has been ignored.**".formatted(channel.getAsMention())).setEphemeral(true)
							.queue();
				} else {
					switch (Database.removeIgnoredChannel(guild, channel)) {
						case -1 -> {
							return false;
						}

						case 0 -> event.reply("**This channel wasn't ignored.**").setEphemeral(true).queue();
						default -> event.reply("%s **has been removed from ignored channels.**".formatted(channel.getAsMention())).setEphemeral(true).queue();
					}
				}

				return true;
			}
			case "list" : {
				List<Long> channels = Database.getIgnoredChannels(guild);
				StringBuilder stringList = new StringBuilder();
				channels.forEach(id -> stringList.append("- <#").append(id).append(">\n"));
				event.reply("**List of ignored channels:**\n%s".formatted(stringList)).setEphemeral(true).queue();
			}

			case null :
			default :
				break; // Neither can happen
		}

		return true;
	}
}
