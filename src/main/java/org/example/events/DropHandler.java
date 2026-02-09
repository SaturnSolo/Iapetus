package org.example.events;

import net.dv8tion.jda.api.components.actionrow.ActionRow;
import net.dv8tion.jda.api.components.buttons.Button;
import net.dv8tion.jda.api.entities.Message;
import net.dv8tion.jda.api.entities.emoji.Emoji;
import net.dv8tion.jda.api.events.interaction.component.ButtonInteractionEvent;
import net.dv8tion.jda.api.events.message.MessageReceivedEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;
import org.example.ButtonModule;
import org.example.Economy;
import org.example.database.Database;
import org.example.types.UserId;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.time.Duration;
import java.time.OffsetDateTime;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.TimeUnit;

public class DropHandler extends ListenerAdapter implements ButtonModule {
	private static final Logger LOGGER = LoggerFactory.getLogger(DropHandler.class);
	private final Map<Long, Integer> perChannelMsgCount = new HashMap<>();
	private final Economy economy;

	public DropHandler(Economy economy) {
		this.economy = economy;
	}

	@Override
	public void onMessageReceived(MessageReceivedEvent event) {
		if (event.getAuthor().isBot() || event.getAuthor().isSystem())
			return;

		List<Long> ignoredChannels = Database.getIgnoredChannels(event.getGuild());

		if (ignoredChannels.contains(event.getChannel().getIdLong())) {
			LOGGER.info("Got a message in an ignored channel");
			return;
		}

		int channelMsgCount = perChannelMsgCount.getOrDefault(event.getChannel().getIdLong(), 0);
		LOGGER.info("Channel %d has %d messages".formatted(event.getChannel().getIdLong(), channelMsgCount));
		if (++channelMsgCount == 26) {
			event.getChannel().sendMessage("**Strawberry Drop!**")
					.addComponents(ActionRow.of(Button.primary("drop:strawberry", "🍓")))
					.queueAfter(1, TimeUnit.SECONDS);

			channelMsgCount = 0;
		}
		LOGGER.info("Now, it has %d messages".formatted(channelMsgCount));
		perChannelMsgCount.put(event.getChannel().getIdLong(), channelMsgCount);
	}

	@Override
	public void onButton(String id, ButtonInteractionEvent event) {
		if (!"strawberry".equals(id))
			return;

		Message message = event.getMessage();

		event.editButton(Button.secondary("drop:claimed", "✨").asDisabled()).queue();

		if (Duration.between(message.getTimeCreated(), OffsetDateTime.now()).toDays() == 0) {
			message.reply("🍓 **Has been claimed by** %s".formatted(event.getUser().getAsMention())).queue(replyMsg -> {
				replyMsg.delete().queueAfter(1, TimeUnit.SECONDS);
				message.delete().queueAfter(1, TimeUnit.SECONDS);
			});
		} else {
			message.addReaction(Emoji.fromUnicode("✅"))
					.queue(success -> message.delete().queueAfter(1, TimeUnit.SECONDS));
		}

		economy.reward(UserId.of(event.getUser()), 1);
	}
}
