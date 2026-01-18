package org.example.commands.shop;

import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.components.actionrow.ActionRow;
import net.dv8tion.jda.api.components.buttons.Button;
import net.dv8tion.jda.api.entities.MessageEmbed;
import net.dv8tion.jda.api.events.interaction.command.SlashCommandInteractionEvent;
import org.example.ButtonManager;
import org.example.buttons.ShopButton;
import org.example.items.EggItem;
import org.example.items.flowers.CherryBlossomItem;
import org.example.items.flowers.RoseItem;
import org.example.items.flowers.TulipItem;
import org.example.structures.IapetusCommand;
import org.example.utils.IapetusColor;

public class ShopCommand extends IapetusCommand {
    private final ButtonManager buttonMgr;

    public ShopCommand(ButtonManager buttonMgr) {
        super("shop", "open the shop menu");
        this.buttonMgr = buttonMgr;

        // create the buttons, this lets the ButtonManager know what to run when a button with the id is clicked.
        buttonMgr.addButtons(
                new ShopButton("egg", new EggItem(), 5), new ShopButton("rose", new RoseItem(), 10), new ShopButton("tulip", new TulipItem(), 10), new ShopButton("cherry_blossom", new CherryBlossomItem(), 15)
        );

    }

    @Override
    public boolean runCommand(SlashCommandInteractionEvent event) {
        MessageEmbed embed = new EmbedBuilder().setTitle("Welcome to the Shop!").setDescription("""
                **`A shiny egg it seems to be glowing` **🥚 - 🍓5
                **`Surprisingly no thorns`** 🌹 - 🍓10
                **`A pretty tulip`** 🌷 - 🍓10
                **`Sakura Cherry Blossoms Pretty`** 🌸 - 🍓15"""
        ).setColor(IapetusColor.PINK).build();

        // fetch each button from the button manager.
        Button eggButton = buttonMgr.getButton("egg");
        Button roseButton = buttonMgr.getButton("rose");
        Button tulipButton = buttonMgr.getButton("tulip");
        Button cherryBlossomButton = buttonMgr.getButton("cherry_blossom");

        // Send the initial message with buttons
        event.replyEmbeds(embed).addComponents(ActionRow.of(eggButton, roseButton, tulipButton, cherryBlossomButton)).queue();
        return true;
    }
}
