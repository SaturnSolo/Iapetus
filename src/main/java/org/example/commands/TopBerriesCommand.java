package org.example.commands;

import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.entities.Guild;
import net.dv8tion.jda.api.entities.MessageEmbed;
import net.dv8tion.jda.api.events.interaction.command.SlashCommandInteractionEvent;
import org.example.database.Database;
import org.example.structures.IapetusCommand;
import org.example.utils.IapetusColor;

import java.util.Map;

public class TopBerriesCommand extends IapetusCommand {
    public TopBerriesCommand() {
        super("leaderboard", "Shows the user with the most berries in the server");
    }

    @Override
    public boolean runCommand(SlashCommandInteractionEvent event) {
        Guild guild = event.getGuild();
        if (guild == null) {
            event.reply("You're alone here in DMs, of course you are #1. You have %d berries.".formatted(Database.getBerryAmount(event.getUser()))).setEphemeral(true).queue();
            return true;
        }

        Map<Long, Integer> topBerries = Database.getTopNBerryHolders(guild.getIdLong(), 10);
        StringBuilder leaderboard = new StringBuilder();

        if (topBerries.isEmpty()) {
            event.reply("No users found in the leaderboard for this server.").setEphemeral(true).queue();
            return true;
        }

        int rank = 1;
        for (Map.Entry<Long, Integer> entry : topBerries.entrySet()) {
            String mention = "<@%d>".formatted(entry.getKey());
            leaderboard.append("**#%d** %s - **%d** berries 🍓".formatted(rank, mention, entry.getValue())).append("\n");
            rank++;
        }

        MessageEmbed embed = new EmbedBuilder().setTitle("Top 10 Berry Collectors in %s".formatted(event.getGuild().getName())).setDescription(leaderboard.toString()).setColor(IapetusColor.RED).build();

        event.replyEmbeds(embed).queue();
        return true;
    }
}