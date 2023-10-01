package org.example.commands;

import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.entities.MessageEmbed;
import net.dv8tion.jda.api.events.interaction.command.SlashCommandInteractionEvent;
import net.dv8tion.jda.api.events.interaction.component.ButtonInteractionEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;
import net.dv8tion.jda.api.interactions.components.buttons.Button;
import net.dv8tion.jda.api.requests.restaction.interactions.ReplyCallbackAction;

public class HelpCommand extends ListenerAdapter {
    @Override
    public void onSlashCommandInteraction(SlashCommandInteractionEvent event) {
        if (event.getName().equals("help")) {
            EmbedBuilder embedBuilder1 = new EmbedBuilder();
            embedBuilder1.setTitle("**Iapetus**");
            embedBuilder1.setDescription("**`Basic info`** \n" +
                    "**Iapetus is a strawberry economy bot with pet commands, fun commands and a lot more! It developed by @saturnsolo, and @msvae.** \n \n" +
                    "**`More info`** \n" +
                    "**The 💚 will take you to the commands menu, the ❔ will take you to the more info tab!**");
            embedBuilder1.setColor(0xD4C2FC);
            embedBuilder1.setThumbnail("https://media.discordapp.net/attachments/1092931260160167996/1156358375098040320/iapetus.png?ex=6514ae28&is=65135ca8&hm=a26cc3c473460c13a879d79020c4989878b1ce25384824744cb2ef4828588f01&=");
            MessageEmbed embed1 = embedBuilder1.build();
            event.replyEmbeds(embed1).addActionRow(Button.primary("commands", "💚"), Button.primary("info", "❔")).queue();

        }
    }

    @Override
    public void onButtonInteraction(ButtonInteractionEvent event) {
        if (event.getComponentId().equals("commands")) {
            EmbedBuilder embedBuilder2 = new EmbedBuilder();
            embedBuilder2.setTitle("**`Commands`**");
            embedBuilder2.setDescription("**Need help with commands or wondering what the commands are and what they do? I am here to help you!** \n \n" + "**`Fun commands`** \n" +
                    "**/berries** is a command that will tell you the amount of berries you have \n" +
                    "**/bonk** will let you bonk a user \n" +
                    "**/shop** opens the shop \n" +
                    "**/inventory** opens your inventory \n" +
                    "**/hatch** hatches a pet out of an egg \n" +
                    "**/pets** gives a pet menu \n" +
                    "**/random** gives a random compliment \n \n" +
                    "**`Admin only commands`** \n" +
                    "**/ignore-channel** ignores a certain channel for berry drops \n" +
                    "**/remove-ignore** removes an ignored channel for berry drops \n");

            embedBuilder2.setColor(0xD4C2FC);
            MessageEmbed embed2 = embedBuilder2.build();
            event.getInteraction().replyEmbeds(embed2).queue();
        }
        if (event.getComponentId().equals("info")) {
            EmbedBuilder embedBuilder3 = new EmbedBuilder();
            embedBuilder3.setTitle("**`More Info`**");
            embedBuilder3.setDescription("**Iapetus is coded using Java and the jda api. It is 100% java (according to github). Iapetus is based off of the moon Iapetus for it's mossy look, it isn't actual moss but looks like it.**");
            embedBuilder3.setColor(0xD4C2FC);
            MessageEmbed embed3 = embedBuilder3.build();
            event.getInteraction().replyEmbeds(embed3).queue();
        }

    }
}
