package org.example.commands;


import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.entities.MessageEmbed;
import net.dv8tion.jda.api.events.interaction.command.SlashCommandInteractionEvent;
import net.dv8tion.jda.api.events.interaction.component.ButtonInteractionEvent;
import net.dv8tion.jda.api.interactions.commands.build.Commands;
import net.dv8tion.jda.api.interactions.components.buttons.Button;
import org.example.ButtonManager;
import org.example.Main;
import org.example.structures.IapetusButton;
import org.example.structures.IapetusCommand;

import java.awt.*;
import java.util.Arrays;
import java.util.List;
import java.util.Random;

public class AdventureCommands extends IapetusCommand {
    ButtonManager bm = Main.buttonManager;

    public AdventureCommands() {
        super(Commands.slash("adventure", "go on an adventure somewhere"));
        bm.addButtons(
                new IapetusButton(Button.primary("yes", "✅")) {
                    @Override
                    public void run(ButtonInteractionEvent event) {
                        String pfp1 = event.getUser().getAvatarUrl();
                        String userName = event.getUser().getName();
                EmbedBuilder embedBuilder5 = new EmbedBuilder();
                embedBuilder5.setColor(0x474B24);
                embedBuilder5.setThumbnail(pfp1);
                embedBuilder5.setTitle(userName +" wonders into");

                        List<String> compliments = Arrays.asList(
                                "bonk", "testing"
                        );

                        // Select a random compliment
                        Random random = new Random();
                        String compliment = compliments.get(random.nextInt(compliments.size()));
                        String ahhh = embedBuilder5.setDescription(compliment).toString();
                        MessageEmbed embed6 = embedBuilder5.build();
                        event.replyEmbeds(embed6).queue();



        }
        });
    }

    @Override
    public boolean runCommand(SlashCommandInteractionEvent event) {
        String userId4 = event.getUser().getId();
        String user4 = event.getUser().getAsMention();
        String pfp = event.getUser().getAvatarUrl();
        EmbedBuilder embedBuilder4 = new EmbedBuilder();
        embedBuilder4.setTitle("**Welcome Adventurer**");
        embedBuilder4.setDescription(user4 + "\n **Welcome to the adventure menu. Are you ready and prepared to go on an adventure?**");
        embedBuilder4.setColor(0x474B24);
        embedBuilder4.setThumbnail(pfp);
        MessageEmbed embed4 = embedBuilder4.build();
        event.replyEmbeds(embed4).addActionRow(bm.getButton("yes")).queue();
        return true;
    }
}
