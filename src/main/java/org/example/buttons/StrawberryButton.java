package org.example.buttons;

import net.dv8tion.jda.api.components.buttons.Button;
import net.dv8tion.jda.api.entities.Message;
import net.dv8tion.jda.api.entities.emoji.Emoji;
import net.dv8tion.jda.api.events.interaction.component.ButtonInteractionEvent;
import org.example.database.Database;
import org.example.structures.IapetusButton;

import java.time.Duration;
import java.time.OffsetDateTime;
import java.util.concurrent.TimeUnit;

public class StrawberryButton extends IapetusButton {
    public StrawberryButton() {
        super(Button.primary("strawberry","🍓"));
    }

    @Override
    public void run(ButtonInteractionEvent event) {
        Message message = event.getMessage();

        // edit first to avoid duplication (faster response!)
        // if we do decide to fix duplicated strawberries we can put both of these in the same action.
        event.editButton(Button.secondary("Strawberry claimed", "✨").asDisabled()).queue();

        if (Duration.between(message.getTimeCreated(), OffsetDateTime.now()).toDays() == 0) {
            message.reply("🍓 **Has been claimed by** %s".formatted(event.getUser().getAsMention()))
                    .queue(replyMsg -> {
                        replyMsg.delete().queueAfter(1, TimeUnit.SECONDS);
                        message.delete().queueAfter(1, TimeUnit.SECONDS);
                    });
        } else message.addReaction(Emoji.fromUnicode("✅")).queue(success -> message.delete().queueAfter(1, TimeUnit.SECONDS));

        Database.giveBerries(event.getUser(), 1);
    }
}
