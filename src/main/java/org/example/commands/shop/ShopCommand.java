package org.example.commands.shop;

import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.entities.MessageEmbed;
import net.dv8tion.jda.api.events.interaction.command.SlashCommandInteractionEvent;
import net.dv8tion.jda.api.interactions.components.buttons.Button;
import org.example.ButtonManager;
import org.example.Main;
import org.example.buttons.ShopButton;
import org.example.structures.IapetusCommand;

public class ShopCommand extends IapetusCommand {
    ButtonManager bm = Main.buttonManager;

    public ShopCommand() {
        super("shop", "open the shop menu");

        // create the buttons, this lets the ButtonManager know what to run when a button with the id is clicked.
        bm.addButtons(
          new ShopButton("egg", "🥚", 5),
          new ShopButton("rose", "🌹", 10),
          new ShopButton("tulip", "🌷", 10),
          new ShopButton("cherry_blossom", "🌸", 15)
        );

    }

    @Override
    public boolean runCommand(SlashCommandInteractionEvent event) {
        EmbedBuilder embedBuilder = new EmbedBuilder();
        embedBuilder.setTitle("Welcome to the Shop!");
        embedBuilder.setDescription("A shiny egg it seems to be glowing 🥚 - 🍓5\n" +
          "Surprisingly no thorns 🌹 - 🍓10\n" +
          "A pretty tulip 🌷 - 🍓10\n" +
          "Sakura Cherry Blossoms Pretty 🌸 - 🍓15");
        embedBuilder.setColor(0xCE6A85); // You can change the color to your preference

        // fetch each button from the button manager.
        Button eggButton = bm.getButton("egg");
        Button roseButton = bm.getButton("rose");
        Button tulipButton = bm.getButton("tulip");
        Button cherryBlossomButton = bm.getButton("cherry_blossom");

        // Add buttons to the embed as components
        MessageEmbed embed = embedBuilder.build();

        // Send the initial message with buttons
        event.replyEmbeds(embed).addActionRow(eggButton, roseButton, tulipButton, cherryBlossomButton).queue();
        return true;
    }
}
