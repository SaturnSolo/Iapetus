package org.example.commands;


import net.dv8tion.jda.api.Permission;
import net.dv8tion.jda.api.entities.Guild;
import net.dv8tion.jda.api.entities.Member;
import net.dv8tion.jda.api.entities.channel.Channel;
import net.dv8tion.jda.api.events.interaction.command.SlashCommandInteractionEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;
import org.example.database.SQLiteDataSource;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;

public class IgnoredChannels extends ListenerAdapter {

    @Override
    public void onSlashCommandInteraction(SlashCommandInteractionEvent event) {
        String commandName = event.getName();
        Member member = event.getMember();
        Guild guild4 = event.getGuild();
        Channel channel4 = event.getChannel();
        Channel sayChannel1 = event.getOption("channel-1").getAsChannel();
        String sayChannel = sayChannel1.getAsMention();



        if (commandName.equals("ignore-channel") || commandName.equals("remove-ignore")) {
            if (!hasAdminPermission(member)) {
                event.reply("**You need administrator permission to use this command.**").setEphemeral(true).queue();
                return;
            }


            if (commandName.equals("ignore-channel")) {
                addIgnoredChannel(guild4, channel4);


                event.reply(sayChannel + " **has been ignored.**").setEphemeral(true).queue();
            } else if (commandName.equals("remove-ignore")) {
                removeIgnoredChannel(guild4, channel4);


                event.reply(sayChannel + "** has been removed from ignore channel list.**").setEphemeral(true).queue();
            }
        }
    }


    private boolean hasAdminPermission(Member member) {
        return member != null && member.hasPermission(Permission.ADMINISTRATOR);
    }

    private void addIgnoredChannel(Guild guild1, Channel channel1) {
        try (Connection connection = SQLiteDataSource.getConnection();
             PreparedStatement ps = connection.prepareStatement("INSERT INTO ignored_channels (guild_id, channel_id) VALUES (?, ?)")) {
            ps.setString(1, guild1.getId());
            ps.setString(2, channel1.getId());
            ps.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    private void removeIgnoredChannel(Guild guild2, Channel channel2) {
        try (Connection connection = SQLiteDataSource.getConnection();
             PreparedStatement ps = connection.prepareStatement("DELETE FROM ignored_channels WHERE guild_id = ? AND channel_id = ?")) {
            ps.setString(1, guild2.getId());
            ps.setString(2, channel2.getId());
            ps.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
    }