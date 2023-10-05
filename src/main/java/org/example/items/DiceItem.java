package org.example.items;

import net.dv8tion.jda.api.entities.emoji.Emoji;
import net.dv8tion.jda.api.events.interaction.command.SlashCommandInteractionEvent;

import java.util.Arrays;
import java.util.List;
import java.util.Random;

public class DiceItem extends Item {
        public DiceItem() {
            super("Dice","Seems rollable.","dice", Emoji.fromUnicode("🎲"), 0);
        }

        @Override
        public boolean use(SlashCommandInteractionEvent event) {
            List<String> rolls = Arrays.asList(
                    "**You roll a 1**",
                    "**You roll a 2**",
                    "**You roll a 3**",
                    "**You roll a 4**",
                    "**You roll a 5**",
                    "**You roll a 6**",
                    "**You roll a 7**",
                    "**You roll a 8**",
                    "**You roll a 9**",
                    "**You roll a 10**",
                    "**You roll a 11**",
                    "**You roll a 12**",
                    "**You roll a 13**",
                    "**You roll a 14**",
                    "**You roll a 15**",
                    "**You roll a 16**",
                    "**You roll a 17**",
                    "**You roll a 18**",
                    "**You roll a 19**",
                    "**You roll a 20**"
            );
            Random random = new Random();
            String diceRoll = rolls.get(random.nextInt(rolls.size()));
            event.reply(diceRoll).setEphemeral(true).queue();
            return false; // item not consumed on use.
        }
    }

