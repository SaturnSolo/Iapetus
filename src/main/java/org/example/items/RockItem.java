package org.example.items;

import net.dv8tion.jda.api.entities.emoji.Emoji;
import net.dv8tion.jda.api.events.interaction.command.SlashCommandInteractionEvent;
import net.dv8tion.jda.api.events.interaction.component.ButtonInteractionEvent;
import net.dv8tion.jda.api.interactions.components.buttons.Button;
import org.example.ButtonManager;
import org.example.Main;
import org.example.structures.IapetusButton;

import java.util.Arrays;
import java.util.List;
import java.util.Random;

public class RockItem extends Item {
    public RockItem() {
        super("Rock", "It's a rock", "rock", Emoji.fromUnicode("\uD83E\uDEA8"), 0);
    }

    @Override
    public boolean use(SlashCommandInteractionEvent event) {
        ButtonManager bm = Main.buttonManager;
        bm.addButtons(
                new RPSButton("rock", "\uD83E\uDEA8"),
                new RPSButton("paper", "🗞"),
                new RPSButton("scissors", "✂")
        );

        event.reply("**Rock, paper, or scissors?**").addActionRow(bm.getButton("rock"),bm.getButton("paper"),bm.getButton("scissors")).queue();
        return false;
    }
    private class RPSButton extends IapetusButton {
        private String selection;
        public RPSButton(String type, String emoji) {
            super(Button.primary(type, emoji));
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

            event.getMessage().editMessageComponents(event.getMessage().getActionRows().get(0).asDisabled()).queue(); // this disables the buttons so they can't be used again.

            // PLAYER DRAWS
            if (result == 0) {
            event.reply("**You both chose** " + selection + " **it's a draw!**").queue();
            }
            // PLAYER WINS
            if (result == 1) {
                event.reply("**You chose** " + selection + " **The bot chose** " + computer + "\n **You win!**").queue();
            }
            // PLAYER LOSES
            if (result == -1) {
                event.reply("**You chose** " + selection + " **The bot chose** " + computer + "\n **You loose!**").queue();
            }
        }

        public String selectRandom(String... strings) {
            List<String> list = Arrays.asList(strings);
            int index = new Random().nextInt(list.size());
            return list.get(index);
        }
    }
}