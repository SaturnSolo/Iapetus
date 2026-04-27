package org.example.events;

import net.dv8tion.jda.api.entities.Message;
import net.dv8tion.jda.api.entities.channel.concrete.TextChannel;
import net.dv8tion.jda.api.entities.channel.concrete.ThreadChannel;
import net.dv8tion.jda.api.events.message.MessageReceivedEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;
import java.util.List;

public class ThreadMaker extends ListenerAdapter {

        private final List<String> POLL_KEYWORDS = List.of(
               "@Polls"
        );

        private final String TARGET_BOT_ID;

        public ThreadMaker() {
            this.TARGET_BOT_ID = "1028198032468082728";
        }

        @Override
        public void onMessageReceived(MessageReceivedEvent event) {
            // Only process messages from the target poll bot
            if (!event.getAuthor().isBot() || !event.getAuthor().getId().equals(TARGET_BOT_ID))
                return;

            Message message = event.getMessage();
            boolean isPoll = isIsPoll(message);

            if (!isPoll)
                return;

            // Ensure channel supports threads
            if (!(event.getChannel() instanceof TextChannel channel))
                return;

            // Prevent duplicate thread creation
            if (message.getStartedThread() != null)
                return;

            String threadName = "Poll Discussion";

            // Create thread from message
            channel.createThreadChannel(threadName, message.getId())
                    .queue(thread -> {
                        thread.sendMessage("Discuss the poll here!").queue();
                    });
        }

    private boolean isIsPoll(Message message) {
        String content = message.getContentDisplay().toLowerCase();

        // Check if message looks like a poll
        boolean isPoll = false;
        for (String keyword : POLL_KEYWORDS) {
            if (content.contains(keyword)) {
                isPoll = true;
                break;
            }
        }

        // Also check embeds (many poll bots use embeds)
        if (!isPoll && !message.getEmbeds().isEmpty()) {
            isPoll = true;
        }
        return isPoll;
    }
}