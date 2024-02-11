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
            String user10 = event.getUser().getAsMention();
            Random random = new Random();
            Integer diceRoll = random.nextInt(20) +1;
            event.reply(user10 + " **you rolled a** " + diceRoll).queue();
            return false; // item not consumed on use.
        }
    }

