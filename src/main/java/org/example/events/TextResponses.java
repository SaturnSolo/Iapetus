package org.example.events;

import net.dv8tion.jda.api.entities.channel.middleman.MessageChannel;
import net.dv8tion.jda.api.events.message.MessageReceivedEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;
import org.example.database.SQLiteDataSource;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;

public class TextResponses extends ListenerAdapter {
    @Override
    public void onMessageReceived(MessageReceivedEvent event) {
        if (event.getAuthor().isBot()) return;
        String content = event.getMessage().getContentDisplay();
        MessageChannel channel = event.getChannel();
        if (content.equals("ping")) {
            channel.sendMessage("Pong! 🌸").queue();
        }
        if (content.contains("🍓")) {
            channel.sendMessage("A Strawberry :D").queue();
        }
        if (content.contains("@Iapetus")) {
            channel.sendMessage("**Hello! If you are needing help, type /help!**").queue();
        }
        if (content.toLowerCase().contains("moss")) {
            channel.sendMessage("I love moss!").queue();
        }
            }
        }

