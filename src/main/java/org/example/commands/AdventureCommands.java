package org.example.commands;


import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.entities.MessageEmbed;
import net.dv8tion.jda.api.events.interaction.command.SlashCommandInteractionEvent;
import net.dv8tion.jda.api.events.interaction.component.ButtonInteractionEvent;
import net.dv8tion.jda.api.interactions.commands.Command;
import net.dv8tion.jda.api.interactions.commands.build.Commands;
import net.dv8tion.jda.api.interactions.components.buttons.Button;
import net.dv8tion.jda.api.utils.TimeFormat;
import net.dv8tion.jda.api.utils.TimeUtil;
import net.dv8tion.jda.api.utils.Timestamp;
import org.example.ButtonManager;
import org.example.ItemManager;
import org.example.Main;
import org.example.buttons.AdventureButtons;
import org.example.structures.IapetusButton;
import org.example.structures.IapetusCommand;

import java.awt.*;
import java.util.*;
import java.util.List;
import java.util.concurrent.TimeUnit;

public class AdventureCommands extends IapetusCommand {
    ButtonManager bm = Main.buttonManager;
    public static Map<String, Date> adventureCooldowns;
    public static long adventureCooldownTime = TimeUnit.SECONDS.toMillis(5);

    public AdventureCommands() {
        super(Commands.slash("adventure", "go on an adventure somewhere"));
        new AdventureButtons();
        adventureCooldowns = new HashMap<>();


//        bm.addButtons(
//                new IapetusButton(Button.primary("yes", "✅")) {
//                    @Override
//                    public void run(ButtonInteractionEvent event) {
//                        String pfp1 = event.getUser().getAvatarUrl();
//                        String userName = event.getUser().getEffectiveName();
//                        EmbedBuilder embedBuilder5 = new EmbedBuilder();
//                        embedBuilder5.setColor(0x474B24);
//                        embedBuilder5.setThumbnail(pfp1);
//                        embedBuilder5.setTitle(userName + " wonders into");
//
//                        List<String> areas = Arrays.asList(
//                                "**a cave that seems to go down a long way \n What would you like to do? \n Investigate \uD83D\uDD0D or Leave \uD83D\uDCA8**",
//                                "**a forest that seems to be enchanted \n What would you like to do? \n Investigate \uD83D\uDD0D or Leave \uD83D\uDCA8**",
//                                "**a mountain that seems impossible to climb \n What would you like to do? \n Investigate \uD83D\uDD0D or Leave \uD83D\uDCA8**",
//                                "**a haunted house that seems dark and spooky \n What would you like to do? \n Investigate \uD83D\uDD0D or Leave \uD83D\uDCA8**",
//                                "**a barn that seems empty \n What would you like to do? \n Investigate \uD83D\uDD0D or Leave \uD83D\uDCA8**",
//                                "**an abandoned building that seems to be in ruin \n What would you like to do? \n Investigate \uD83D\uDD0D or Leave \uD83D\uDCA8**",
//                                "**a ball of yarn \n What would you like to do? \n Investigate \uD83D\uDD0D or Leave \uD83D\uDCA8**",
//                                "**a rainstorm that seems to never end \n What would you like to do? \n Investigate \uD83D\uDD0D or Leave \uD83D\uDCA8**",
//                                "**a tree that seems to hold an item of sorts \n What would you like to do?  \n Investigate \uD83D\uDD0D or Leave \uD83D\uDCA8**",
//                                "**a mossy fountain that seems magical \n What would you like to do? \n Investigate \uD83D\uDD0D or Leave \uD83D\uDCA8**",
//                                "**a statue of a mossy moon \n What would you like to do? \n investigate \uD83D\uDD0D or Leave \uD83D\uDCA8**",
//                                "**an old cabin in the middle of the woods that is covered in moss \n What would you like to do? \n Investigate \uD83D\uDD0D or Leave \uD83D\uDCA8**"
//                        );
//                        //add seasonal things here
//                        // Investigate 🔍 or Leave 💨
//
//
//                        // Select a random compliment
//                        Random random = new Random();
//                        String area = areas.get(random.nextInt(areas.size()));
//                        String ahhh = embedBuilder5.setDescription(area).toString();
//                        MessageEmbed embed6 = embedBuilder5.build();
//                        event.replyEmbeds(embed6).addActionRow(bm.getButton("investigate"), bm.getButton("leave")).queue();
//
//                        event.getInteraction().editButton(bm.getButton("yes").asDisabled()).queue();
//
//
//                    }
//                },
//                new IapetusButton(Button.primary("investigate", "🔍")) {
//
//                    @Override
//                    public void run(ButtonInteractionEvent event) {
//                        String userIdCard = event.getUser().getId();
//                        String pfp2 = event.getUser().getAvatarUrl();
//                        String userName = event.getUser().getEffectiveName();
//                        EmbedBuilder embedBuilder6 = new EmbedBuilder();
//                        embedBuilder6.setColor(0x474B24);
//                        embedBuilder6.setThumbnail(pfp2);
//                        embedBuilder6.setTitle(userName + " investigates the area");
//
//
//                        //Wow, there is a random comment here
//
//
//
//                        Map<String, String> investigations = new HashMap<>();
//                        investigations.put("**You find something shiny that you can't seem to put into words**","shiny");
//                        investigations.put("You found nothing but air",null);
//                        investigations.put("**You find a random dice sitting upon a stone**","dice");
//                        investigations.put("You kicked some dirt and found nothing",null);
//                        investigations.put("**You find a rock that you decide to pocket**","rock");
//                        investigations.put("You looked around an found nothing...",null);
//                        investigations.put("**You find a gem that seems to be sparkling**","gem");
//                        investigations.put("You did a backflip but nothing showed.",null);
//                        investigations.put("**You find a glowing pumpkin**","pumpkin");
//                        investigations.put("You opened a drawer and found nothing.",null);
//                        investigations.put("**You find an old rusty key**","key");
//                        investigations.put("You breathed, nothing happened.",null);
//
//
//
//                        int value = new Random().nextInt(investigations.size());
//                        String message = investigations.keySet().stream().toList().get(value);
//                        String item = investigations.get(message);
//                        String building = embedBuilder6.setDescription(message).toString();
//                        MessageEmbed embed6 = embedBuilder6.build();
//                        event.replyEmbeds(embed6).queue();
//
//                        ItemManager im = Main.itemManager;
//                        if (item != null) im.giveItem(userIdCard, item);
//
//                        event.getInteraction().editButton(bm.getButton("investigate").asDisabled()).queue();
//                    }
//                },
//                new IapetusButton(Button.primary("leave", "💨")) {
//
//                    @Override
//                    public void run(ButtonInteractionEvent event) {
//                        String userName1 = event.getUser().getEffectiveName();
//                        EmbedBuilder embedBuilder7 = new EmbedBuilder();
//                        embedBuilder7.setTitle(userName1 + " leaves the area");
//                        embedBuilder7.setColor(0x474B24);
//
//
//                        List<String> leave = Arrays.asList(
//                                "**You left the area finding nothing of interest, who knows what you left behind**",
//                                "**Endless possibles of what you could find**",
//                                "**Hopefully you didn't leave anything of value behind**",
//                                "**Hope you had a safe journey**",
//                                "**Did you have a safe journey?**",
//                                "**What a lovely adventure**"
//                        );
//
//                        Random random = new Random();
//                        String leaf = leave.get(random.nextInt(leave.size()));
//                        String building = embedBuilder7.setDescription(leaf).toString();
//                        MessageEmbed embed6 = embedBuilder7.build();
//                        event.replyEmbeds(embed6).queue();
//
//                        event.getInteraction().editButton(bm.getButton("leave").asDisabled()).queue();
//
//
//                    }
//
//                },
//
//                new IapetusButton(Button.secondary("no", "❌")) {
//                    @Override
//                    public void run(ButtonInteractionEvent event) {
//                        String pfp3 = event.getUser().getAvatarUrl();
//                        String userLeave = event.getUser().getEffectiveName();
//                    EmbedBuilder embedBuilder9 = new EmbedBuilder();
//                    embedBuilder9.setThumbnail(pfp3);
//                    embedBuilder9.setColor(0x474B24);
//                    embedBuilder9.setTitle(userLeave + "** isn't ready**");
//                        List<String> nope = Arrays.asList(
//                                "**Adventure isn't always for everybody it's okay**",
//                                "**Adventure when you are ready**",
//                                "**There is no rush to rush out into the adventure relax while you can**",
//                                "**Not ready for adventure yet? That's fine!**"
//
//                        );
//                        Random random = new Random();
//                        String no = nope.get(random.nextInt(nope.size()));
//                        String nop = embedBuilder9.setDescription(no).toString();
//                        MessageEmbed embed9 = embedBuilder9.build();
//                        event.replyEmbeds(embed9).setEphemeral(true).queue();
//
//                        event.getInteraction().editButton(bm.getButton("no").asDisabled()).queue();
//
//                    }
//                });
    }

    @Override
    public boolean runCommand(SlashCommandInteractionEvent event) {
        String userId = event.getUser().getId();
        String userMention = event.getUser().getAsMention();
        String pfp = event.getUser().getAvatarUrl();
        EmbedBuilder embedBuilder = new EmbedBuilder();
        embedBuilder.setTitle("**Welcome Adventurer**");
        embedBuilder.setDescription("Welcome"+ userMention +" to the adventure menu. Are you ready and prepared to go on an adventure?");
        embedBuilder.setColor(0x474B24);
        embedBuilder.setThumbnail(pfp);
        MessageEmbed embed = embedBuilder.build();

        Date cooldownEnd = adventureCooldowns.get(userId);
        if (cooldownEnd != null && new Date().before(cooldownEnd)) {
            event.reply("You need to rest! You can adventure again <t:"+ cooldownEnd.getTime()/1000 +":R>.").queue();
            return true;
        }
        adventureCooldowns.put(userId, new Date(System.currentTimeMillis()+adventureCooldownTime));
        event.replyEmbeds(embed).addActionRow(bm.getButton("confirm-adv"), bm.getButton("cancel-adv")).queue();
        return true;
    }
}
