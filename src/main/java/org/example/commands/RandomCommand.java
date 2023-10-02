package org.example.commands;

import net.dv8tion.jda.api.entities.IMentionable;
import net.dv8tion.jda.api.entities.channel.middleman.MessageChannel;
import net.dv8tion.jda.api.events.interaction.command.SlashCommandInteractionEvent;
import net.dv8tion.jda.api.events.message.MessageReceivedEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;
import org.example.structures.IapetusCommand;

import java.util.Arrays;
import java.util.List;
import java.util.Random;

public class RandomCommand extends IapetusCommand {
    public RandomCommand() {
        super("random", "sends something random");
    }
    @Override
    public boolean runCommand(SlashCommandInteractionEvent event) {
        List<String> compliments = Arrays.asList(
                "**🍓**",
                "**Sending all the hugs**",
                "**Don't forget to eat**",
                "**The world may be hard but keep going**",
                "**Send love to the moss!**",
                "**I am but a moss moon**",
                "**You are amazing**",
                "**You are doing great**",
                "**I hope you are having a lovely day**",
                "**You're more helpful than you realize**",
                "**Your kindness is a balm to all who encounter it**",
                "**You're great at figuring stuff out**",
                "**Your creative potential seems limitless**",
                "**You are making a difference**",
                "**Our community is better because you're in it**",
                "**I’m proud of how far you have come and for never giving up**",
                "**I love that you can always find that silver lining in bad situations**",
                "**You are always so thoughtful and considerate**",
                "**You have a real talent for writing and communicating**",
                "**You radiate compassion**"
        );

        // Select a random compliment
        Random random = new Random();
        String compliment = compliments.get(random.nextInt(compliments.size()));

        // Send the compliment as a message
        event.reply(compliment).queue();
        return true;
    }

}


