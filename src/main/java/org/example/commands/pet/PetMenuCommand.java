package org.example.commands.pet;

import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.entities.MessageEmbed;
import net.dv8tion.jda.api.events.interaction.command.SlashCommandInteractionEvent;
import org.example.database.Database;
import org.example.structures.IapetusCommand;
import org.example.utils.IapetusColor;

import java.util.List;

public class PetMenuCommand extends IapetusCommand {
    public PetMenuCommand() {
        super("pets", "shows a list of your pets");
    }

    @Override
    public boolean runCommand(SlashCommandInteractionEvent event) {
        String userId = event.getUser().getId();
        List<String> petList = Database.getHatchedPets(userId);

        if (petList.isEmpty()) {
            event.reply("You don't have any hatched pets yet.").queue();
            return true;
        }

        MessageEmbed embed = new EmbedBuilder().setTitle("Hatched Pets").setDescription(String.join("\n", petList)).setColor(IapetusColor.RED).build();

        event.replyEmbeds(embed).queue();
        return true;
    }
}
