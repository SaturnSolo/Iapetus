package org.example.events;

import net.dv8tion.jda.api.entities.Message;
import net.dv8tion.jda.api.entities.channel.concrete.TextChannel;
import net.dv8tion.jda.api.events.message.MessageReceivedEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;
import java.util.List;

public class ThreadMaker extends ListenerAdapter {
    private static final List<String> POLL_KEYWORDS = List.of("@Polls", "<@&894314069488701490>");
    private static final String TARGET_BOT_ID = "1028198032468082728";

    @Override
    public void onMessageReceived(MessageReceivedEvent event) {
        if (!event.getAuthor().getId().equals(TARGET_BOT_ID))
            return;

        Message message = event.getMessage();
        String content = message.getContentDisplay().toLowerCase();

        boolean isPoll = false;
        for (String keyword : POLL_KEYWORDS) {
            if (content.contains(keyword)) {
                isPoll = true;
                break;
            }
        }

        if (!isPoll)
            return;

        if (!(event.getChannel() instanceof TextChannel channel))
            return;

        if (message.getStartedThread() != null)
            return;

        String threadName = "Poll Discussion";


        channel.createThreadChannel(threadName, message.getId())
                .queue(thread -> {
                    thread.sendMessage("Discuss the poll here!").queue();
                });
    }
}