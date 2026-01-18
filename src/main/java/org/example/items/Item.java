package org.example.items;

import net.dv8tion.jda.api.entities.emoji.Emoji;
import net.dv8tion.jda.api.events.interaction.command.SlashCommandInteractionEvent;
import org.example.ItemManager;
import org.example.buttons.ShopButton;

public abstract class Item {
    private static ItemManager itemMgr;
    // these values will show to the user.
    private final String name;
    private final Emoji icon; // usually an emoji
    private final String description;
    private final int cost;

    // something like "cherry_blossom" - not shown to user.
    private final String id;

    private final ShopButton buyButton;

    public static void init(ItemManager itemMgr) {
        Item.itemMgr = itemMgr;
    }

    public Item(String name, String description, String id, Emoji icon, int cost) {
        this.name = name;
        this.description = description;
        this.id = id;

        this.icon = icon;
        this.cost = cost;
        buyButton = new ShopButton(id, this, cost);

        Item.itemMgr.addItem(this);
    }

    public Item(String name, String description, String id, Emoji icon) {
        this(name, description, id, icon, 0);
    }

    public Item(String name, String description, String id) {
        this(name, description, id, null, 0);
    }


    abstract public boolean use(SlashCommandInteractionEvent event);

    public String getString() {
        return getString(false);
    }

    public String getString(boolean reverse) {
        if (reverse) return "%s %s".formatted(name, getIcon());
        return "%s %s".formatted(getIcon(), name);
    }

    public String getName() {
        return name;
    }

    public String getDescription() {
        return description;
    }

    public Emoji getIconEmoji() {
        return icon;
    }

    public String getIcon() {
        return getIconEmoji().getFormatted();
    }

    public String getId() {
        return id;
    }
}
