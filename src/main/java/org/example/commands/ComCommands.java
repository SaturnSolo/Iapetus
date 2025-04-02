package org.example.commands;

import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.entities.MessageEmbed;
import net.dv8tion.jda.api.events.interaction.command.SlashCommandInteractionEvent;
import org.example.structures.IapetusCommand;

public class ComCommands extends IapetusCommand {
    public ComCommands() {
        super("commands", "shows you all of the bots commands");
    }

    @Override
    public boolean runCommand(SlashCommandInteractionEvent event) {
        EmbedBuilder embedBuilder5 = new EmbedBuilder();
        embedBuilder5.setTitle("**Iapetus Commands**");
        embedBuilder5.setDescription("Error 404: Message missing!");
        embedBuilder5.setDescription("**Need help with commands or wondering what the commands are and what they do? I am here to help you!** \n \n" + "**`Fun commands`** \n" +
                "**/adventure** takes you on an adventure who knows where it will take you \n"+
                "**/berries** is a command that will tell you the amount of berries you have \n" +
                "**/bonk** will let you bonk a user \n" +
               "**/daily** daily reward \n" +
                "**/commands** lists all bot commands \n" +
               "**/shop** opens the shop \n" +
                "**/inventory** opens your inventory \n" +
                "**/hatch** hatches a pet out of an egg \n" +
                "**/lootchest** opens a loot chest! \n" +
               "**/leaderboard** shows what user in the server has the most berries \n" +
               "**/use** uses an item \n" +
                "**/give** give other users berries \n" +
                "**/pets** gives a pet menu \n" +
               "**/random** gives a random compliment \n" +
               "**/ping** pong\n \n" +
                "**`Staff only commands`** \n" +
               "**/ignore-channel** ignores a certain channel for berry drops \n" +
               "**/list-ignored** lists ignored channels \n" +
                "**/remove-ignore** removes an ignored channel for berry drops \n");
        embedBuilder5.setColor(0xD4C2FC);
        embedBuilder5.setThumbnail("https://media.discordapp.net/attachments/1274910028888932402/1275871665603088465/iapetusnobackground.png?ex=66c77795&is=66c62615&hm=dd21eb064d66624006faed7bbab8435dbb030cdd2fa3847bcbe2557df9100ba8&=&format=webp&quality=lossless");
        MessageEmbed embed5 = embedBuilder5.build();
        event.replyEmbeds(embed5).queue();
        return true;
    }
}
