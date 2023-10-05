package org.example.commands;


import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.entities.MessageEmbed;
import net.dv8tion.jda.api.events.interaction.command.SlashCommandInteractionEvent;
import net.dv8tion.jda.api.events.interaction.component.ButtonInteractionEvent;
import net.dv8tion.jda.api.interactions.commands.build.Commands;
import net.dv8tion.jda.api.interactions.components.buttons.Button;
import org.example.ButtonManager;
import org.example.ItemManager;
import org.example.Main;
import org.example.buttons.AdventureButtons;
import org.example.structures.IapetusButton;
import org.example.structures.IapetusCommand;

import java.awt.*;
import java.util.*;
import java.util.List;

public class AdventureCommands extends IapetusCommand {
    ButtonManager bm = Main.buttonManager;

    public AdventureCommands() {
        super(Commands.slash("adventure", "go on an adventure somewhere"));
        new AdventureButtons();

        bm.addButtons(
                new IapetusButton(Button.primary("yes", "✅")) {
                    @Override
                    public void run(ButtonInteractionEvent event) {
                        String pfp1 = event.getUser().getAvatarUrl();
                        String userName = event.getUser().getEffectiveName();
                        EmbedBuilder embedBuilder5 = new EmbedBuilder();
                        embedBuilder5.setColor(0x474B24);
                        embedBuilder5.setThumbnail(pfp1);
                        embedBuilder5.setTitle(userName + " wonders into");

                        List<String> areas = Arrays.asList(
                                "**a cave that seems to go down a long way \n What would you like to do? \n Investigate \uD83D\uDD0D or Leave \uD83D\uDCA8**",
                                "**a forest that seems to be enchanted \n What would you like to do? \n Investigate \uD83D\uDD0D or Leave \uD83D\uDCA8**",
                                "**a mountain that seems impossible to climb \n What would you like to do? \n Investigate \uD83D\uDD0D or Leave \uD83D\uDCA8**",
                                "**a haunted house that seems dark and spooky \n What would you like to do? \n Investigate \uD83D\uDD0D or Leave \uD83D\uDCA8**",
                                "**a barn that seems empty \n What would you like to do? \n Investigate \uD83D\uDD0D or Leave \uD83D\uDCA8**",
                                "**an abandoned building that seems to be in ruin \n What would you like to do? \n Investigate \uD83D\uDD0D or Leave \uD83D\uDCA8**",
                                "**a ball of yarn \n What would you like to do? \n Investigate \uD83D\uDD0D or Leave \uD83D\uDCA8**",
                                "**a rainstorm that seems to never end \n What would you like to do? \n Investigate \uD83D\uDD0D or Leave \uD83D\uDCA8**",
                                "**a tree that seems to hold an item of sorts \n What would you like to do?  \n Investigate \uD83D\uDD0D or Leave \uD83D\uDCA8**",
                                "**a mossy fountain that seems magical \n What would you like to do? \n Investigate \uD83D\uDD0D or Leave \uD83D\uDCA8**",
                                "**a statue of a mossy moon \n What would you like to do? \n investigate \uD83D\uDD0D or Leave \uD83D\uDCA8**",
                                "**an old cabin in the middle of the woods that is covered in moss \n What would you like to do? \n Investigate \uD83D\uDD0D or Leave \uD83D\uDCA8**"
                        );
                        //add seasonal things here
                        // Investigate 🔍 or Leave 💨


                        // Select a random compliment
                        Random random = new Random();
                        String area = areas.get(random.nextInt(areas.size()));
                        String ahhh = embedBuilder5.setDescription(area).toString();
                        MessageEmbed embed6 = embedBuilder5.build();
                        event.replyEmbeds(embed6).addActionRow(bm.getButton("investigate"), bm.getButton("leave")).queue();

                        event.getInteraction().editButton(bm.getButton("yes").asDisabled()).queue();
                        

                    }
                },
                new IapetusButton(Button.primary("investigate", "🔍")) {

                    @Override
                    public void run(ButtonInteractionEvent event) {
                        String userIdCard = event.getUser().getId();
                        String pfp2 = event.getUser().getAvatarUrl();
                        String userName = event.getUser().getEffectiveName();
                        EmbedBuilder embedBuilder6 = new EmbedBuilder();
                        embedBuilder6.setColor(0x474B24);
                        embedBuilder6.setThumbnail(pfp2);
                        embedBuilder6.setTitle(userName + " investigates the area");


                        //Wow, there is a random comment here



                        Map<String, String> investigations = new HashMap<>();
                        investigations.put("shiny","**You find something shiny that you can't seem to put into words**");
                        investigations.put(null, "You found nothing but air");
                        investigations.put("dice","**You find a random dice sitting upon a stone**");
                        investigations.put(null,"You kicked some dirt and found nothing");
                        investigations.put("rock","**You find a rock that you decide to pocket**");
                        investigations.put(null,"You kicked some dirt and found nothing");
                        investigations.put("gem","**You find a gem that seems to be sparkling**");
                        investigations.put(null, "You found nothing but air");
                        investigations.put("pumpkin","**You find a glowing pumpkin**");
                        investigations.put(null,"You kicked some dirt and found nothing");
                        investigations.put("key","**You find an old rusty key**");
                        investigations.put(null, "You found nothing but air");



                        int value = new Random().nextInt(investigations.size());
                        String selected = investigations.keySet().stream().toList().get(value);
                        String message = investigations.get(selected);
                        String building = embedBuilder6.setDescription(message).toString();
                        MessageEmbed embed6 = embedBuilder6.build();
                        event.replyEmbeds(embed6).queue();

                        ItemManager im = Main.itemManager;
                        im.giveItem(userIdCard, selected);

                        event.getInteraction().editButton(bm.getButton("investigate").asDisabled()).queue();
                    }
                },
                new IapetusButton(Button.primary("leave", "💨")) {

                    @Override
                    public void run(ButtonInteractionEvent event) {
                        String userName1 = event.getUser().getEffectiveName();
                        EmbedBuilder embedBuilder7 = new EmbedBuilder();
                        embedBuilder7.setTitle(userName1 + " leaves the area");
                        embedBuilder7.setColor(0x474B24);


                        List<String> leave = Arrays.asList(
                                "**You left the area finding nothing of interest, who knows what you left behind**",
                                "**Endless possibles of what you could find**",
                                "**Hopefully you didn't leave anything of value behind**",
                                "**Hope you had a safe journey**",
                                "**Did you have a safe journey?**",
                                "**What a lovely adventure**"
                        );

                        Random random = new Random();
                        String leaf = leave.get(random.nextInt(leave.size()));
                        String building = embedBuilder7.setDescription(leaf).toString();
                        MessageEmbed embed6 = embedBuilder7.build();
                        event.replyEmbeds(embed6).queue();

                        event.getInteraction().editButton(bm.getButton("leave").asDisabled()).queue();


                    }

                },

                new IapetusButton(Button.primary("no", "❌")) {

                    @Override
                    public void run(ButtonInteractionEvent event) {
                        String pfp3 = event.getUser().getAvatarUrl();
                        String userLeave = event.getUser().getEffectiveName();
                    EmbedBuilder embedBuilder9 = new EmbedBuilder();
                    embedBuilder9.setThumbnail(pfp3);
                    embedBuilder9.setColor(0x474B24);
                    embedBuilder9.setTitle(userLeave + "** isn't ready**");
                        List<String> nope = Arrays.asList(
                                "**Adventure isn't always for everybody it's okay**",
                                "**Adventure when you are ready**",
                                "**There is no rush to rush out into the adventure relax while you can**",
                                "**Not ready for adventure yet? That's fine!**"

                        );
                        Random random = new Random();
                        String no = nope.get(random.nextInt(nope.size()));
                        String nop = embedBuilder9.setDescription(no).toString();
                        MessageEmbed embed9 = embedBuilder9.build();
                        event.replyEmbeds(embed9).setEphemeral(true).queue();

                        event.getInteraction().editButton(bm.getButton("no").asDisabled()).queue();

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

        event.replyEmbeds(embed4).addActionRow(bm.getButton("yes"), bm.getButton("no")).queue();


        return true;
    }
}
