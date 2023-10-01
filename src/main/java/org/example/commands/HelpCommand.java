package org.example.commands;

import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.entities.MessageEmbed;
import net.dv8tion.jda.api.events.interaction.command.SlashCommandInteractionEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;

public class HelpCommand extends ListenerAdapter {
    @Override
    public void onSlashCommandInteraction(SlashCommandInteractionEvent event) {
        if (event.getName().equals("help")) {
            EmbedBuilder embedBuilder1 = new EmbedBuilder();
            embedBuilder1.setTitle("Iapetus");
            embedBuilder1.setDescription("**Iapetus is a strawberry economy bot with a pet menu and more fun commands. Was made by @saturnsolo. It is a bot made with Java. I also am a moss moon. :)**");
            embedBuilder1.setColor(0xD4C2FC);
            embedBuilder1.setThumbnail("https://media.discordapp.net/attachments/1092931260160167996/1156358375098040320/iapetus.png?ex=6514ae28&is=65135ca8&hm=a26cc3c473460c13a879d79020c4989878b1ce25384824744cb2ef4828588f01&=");
            MessageEmbed embed1 = embedBuilder1.build();
            event.replyEmbeds(embed1).queue();
        }
    }
}
