package org.example.commands;

import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.entities.MessageEmbed;
import net.dv8tion.jda.api.events.interaction.command.SlashCommandInteractionEvent;
import org.example.structures.IapetusCommand;
import org.example.utils.IapetusColor;

public class ComCommands extends IapetusCommand {
    public ComCommands() {
        super("commands", "shows you all of the bots commands");
    }

    @Override
    public boolean runCommand(SlashCommandInteractionEvent event) {
        MessageEmbed embed = new EmbedBuilder()
            .setTitle("**Iapetus Commands**")
            .setDescription("Error 404: Message missing!")
            .setDescription("""
                **Need help with commands or wondering what the commands are and what they do? I am here to help you!**\s
                \s
                **`Fun commands`**\s
                **/adventure** takes you on an adventure who knows where it will take you\s
                **/berries** is a command that will tell you the amount of berries you have\s
                **/bonk** will let you bonk a user\s
                **/daily** daily reward\s
                **/commands** lists all bot commands\s
                **/shop** opens the shop\s
                **/inventory** opens your inventory\s
                **/hatch** hatches a pet out of an egg\s
                **/lootchest** opens a loot chest!\s
                **/leaderboard** shows what user in the server has the most berries\s
                **/use** uses an item\s
                **/give** give other users berries\s
                **/pets** gives a pet menu\s
                **/random** gives a random compliment\s
                **/ping** pong
                \s
                **`Staff only commands`**\s
                **/ignore-channel** ignores a certain channel for berry drops\s
                **/list-ignored** lists ignored channels\s
                **/remove-ignore** removes an ignored channel for berry drops\s"""
            )
            .setColor(IapetusColor.LILA)
            .setThumbnail("https://media.discordapp.net/attachments/1274910028888932402/1275871665603088465/iapetusnobackground.png?ex=66c77795&is=66c62615&hm=dd21eb064d66624006faed7bbab8435dbb030cdd2fa3847bcbe2557df9100ba8&=&format=webp&quality=lossless")
            .build();

        event.replyEmbeds(embed).queue();
        return true;
    }
}
