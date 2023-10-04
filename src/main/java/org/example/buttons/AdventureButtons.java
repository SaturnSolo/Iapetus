package org.example.buttons;

import net.dv8tion.jda.api.entities.emoji.Emoji;
import net.dv8tion.jda.api.events.interaction.component.ButtonInteractionEvent;
import net.dv8tion.jda.api.interactions.components.buttons.Button;
import org.example.ButtonManager;
import org.example.Main;
import org.example.structures.IapetusButton;

public class AdventureButtons {
    ButtonManager bm = Main.buttonManager;
    public AdventureButtons() {
        bm.addButtons(
          new ConfirmButton()
        );
    }

    private static String getRandom() {
        return "random-event";
    }

    public static class ConfirmButton extends IapetusButton {
        public ConfirmButton() {
            super(Button.success("confirm-adv", "Confirm").withEmoji(Emoji.fromUnicode("✅")));
        }

        @Override
        public void run(ButtonInteractionEvent event) {
            event.reply(getRandom()).queue();
        }
    }
}
