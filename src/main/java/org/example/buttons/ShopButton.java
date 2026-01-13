package org.example.buttons;

import net.dv8tion.jda.api.components.buttons.Button;
import net.dv8tion.jda.api.events.interaction.component.ButtonInteractionEvent;
import org.example.database.Database;
import org.example.items.Item;
import org.example.structures.IapetusButton;

public class ShopButton extends IapetusButton {
    private final Item item;
    private final Integer cost;

    public ShopButton(String id, Item item, Integer cost) {
        super(Button.secondary(id, item.getName()).withEmoji(item.getIconEmoji()));
        this.item = item;
        this.cost = cost;
    }

    @Override
    public void run(ButtonInteractionEvent event) {
        int userBerries = Database.getBerryAmount(event.getUser());
        if (userBerries >= cost) {
            Database.takeBerries(event.getUser(), cost);
            Database.addToInventory(event.getUser(), item.getId());
            event.reply("**You have purchased a %s and added it to your inventory!**".formatted(item.getString())).setEphemeral(true).queue();
        } else {
            event.reply("**Insufficient berries!**").setEphemeral(true).queue();
        }
    }
}
