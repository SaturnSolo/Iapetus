package org.example.events;

import net.dv8tion.jda.api.components.actionrow.ActionRow;
import net.dv8tion.jda.api.components.buttons.Button;
import net.dv8tion.jda.api.events.message.MessageReceivedEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;
import org.example.database.Database;

import java.sql.*;
import java.util.List;
import java.util.concurrent.TimeUnit;

public class DropHandler extends ListenerAdapter {
	private int messageCount = 0;

	@Override
	public void onMessageReceived(MessageReceivedEvent event) {
		if (event.getAuthor().isBot())
			return;

		// Retrieve ignored channels from database
		List<Long> ignoredChannels = Database.getIgnoredChannels(event.getGuild());

		// Check if current channel is ignored
		if (ignoredChannels.contains(event.getChannel().getIdLong()))
			return;

		if (++messageCount == 26) {
			event.getChannel().sendMessage("**Strawberry Drop!**")
					.addComponents(ActionRow.of(Button.primary("strawberry", "🍓")))
					// .flatMap(message -> message.addReaction(Emoji.fromUnicode("🍓")))
					.queueAfter(1, TimeUnit.SECONDS);
			messageCount = 0; // reset message count
		}
	}
}
