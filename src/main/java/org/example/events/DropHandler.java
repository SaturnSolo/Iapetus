package org.example.events;

import net.dv8tion.jda.api.components.actionrow.ActionRow;
import net.dv8tion.jda.api.components.buttons.Button;
import net.dv8tion.jda.api.events.message.MessageReceivedEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;
import org.example.database.Database;

import java.sql.*;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.TimeUnit;

public class DropHandler extends ListenerAdapter {
    private final Map<Long, Integer> perChannelMsgCount = new HashMap<>();

	@Override
	public void onMessageReceived(MessageReceivedEvent event) {
		if (event.getAuthor().isBot() || event.getAuthor().isSystem())
			return;

		// Retrieve ignored channels from database
		List<Long> ignoredChannels = Database.getIgnoredChannels(event.getGuild());

		// Check if current channel is ignored
		if (ignoredChannels.contains(event.getChannel().getIdLong()))
			return;

        int channelMsgCount = perChannelMsgCount.getOrDefault(event.getChannel().getIdLong(), 0);
        if (++channelMsgCount == 26) {
            event.getChannel().sendMessage("**Strawberry Drop!**")
                .addComponents(ActionRow.of(Button.primary("strawberry", "🍓")))
                .queueAfter(1, TimeUnit.SECONDS);

            channelMsgCount = 0;
        }
        perChannelMsgCount.put(event.getChannel().getIdLong(), channelMsgCount);
	}
}
