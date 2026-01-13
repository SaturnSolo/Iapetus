package org.example.items;

import net.dv8tion.jda.api.components.actionrow.ActionRow;
import net.dv8tion.jda.api.components.buttons.Button;
import net.dv8tion.jda.api.entities.emoji.Emoji;
import net.dv8tion.jda.api.entities.emoji.UnicodeEmoji;
import net.dv8tion.jda.api.events.interaction.command.SlashCommandInteractionEvent;
import net.dv8tion.jda.api.events.interaction.component.ButtonInteractionEvent;
import org.example.ButtonManager;
import org.example.structures.IapetusButton;

import java.util.Random;

public class RockItem extends Item {
    private final ButtonManager buttonMgr;
    private final Random rng;
    public RockItem(ButtonManager buttonMgr, Random rng) {
        super("Rock", "It's a rock", "rock", Emoji.fromUnicode("\uD83E\uDEA8"), 0);
        this.buttonMgr = buttonMgr;
        this.rng = rng;
    }

    @Override
    public boolean use(SlashCommandInteractionEvent event) {
        buttonMgr.addButtons(
            new RPSButton("rock", "\uD83E\uDEA8"),
            new RPSButton("paper", "🗞"),
            new RPSButton("scissors", "✂")
        );

        event.reply("**Rock, paper, or scissors?**").addComponents(ActionRow.of(buttonMgr.getButton("rock"), buttonMgr.getButton("paper"), buttonMgr.getButton("scissors"))).queue();
        return false;
    }
    private class RPSButton extends IapetusButton {
        private final String selection;

        private enum Emojis {
            ROCK(Emoji.fromUnicode("\uD83E\uDEA8")),
            PAPER(Emoji.fromUnicode("🗞")),
            SCISSORS(Emoji.fromUnicode("✂"));

            private final Emoji emoji;
            Emojis(UnicodeEmoji emoji) {
                this.emoji = emoji;
            }

            public String get() {
                return emoji.getFormatted();
            }
        }
        public RPSButton(String type, String emoji) {
            super(Button.primary(type, Emoji.fromUnicode(emoji)));
            selection = type;
        }
        @Override
        public void run(ButtonInteractionEvent event) {
            String computer = selectRandom("rock", "paper", "scissors");

            int result = 2;

            if (selection.equals(computer)) {
                result = 0;
            } else if (computer.equals("rock")) {
                if (selection.equals("paper")) result = 1;
                else result = -1;
            } else if (computer.equals("paper")) {
                if (selection.equals("scissors")) result = 1;
                else result = -1;
            } else if (computer.equals("scissors")) {
                if (selection.equals("rock")) result = 1;
                else result = -1;
            }

            if (result == 2) {
                event.reply("an error occured processing this request, please try again.").queue();
                return;
            }

            event.getMessage().editMessageComponents(event.getMessage().getComponents().getFirst().asActionRow().asDisabled()).queue(); // this disables the buttons so they can't be used again.

            // PLAYER DRAWS
            if (result == 0) {
            event.reply("**You both chose** " + Emojis.valueOf(selection.toUpperCase()).get() + "\n **it's a draw!**").queue();
            }
            // PLAYER WINS
            if (result == 1) {
                event.reply("**You chose** " + Emojis.valueOf(selection.toUpperCase()).get() + " **The bot chose** " + Emojis.valueOf(computer.toUpperCase()).get() + "\n **You win!**").queue();
            }
            // PLAYER LOSES
            if (result == -1) {
                event.reply("**You chose** " + Emojis.valueOf(selection.toUpperCase()).get() + " **The bot chose** " + Emojis.valueOf(computer.toUpperCase()).get() + "\n **You lose!**").queue();
            }
        }

        public String selectRandom(String... strings) {
            return strings[rng.nextInt(strings.length)];
        }
    }
}