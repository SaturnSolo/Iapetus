package org.example.items;

import net.dv8tion.jda.api.entities.emoji.Emoji;
import org.example.ItemManager;
import org.example.Main;
import org.example.buttons.ShopButton;

import java.lang.reflect.Member;

public abstract class Item {
    final ItemManager manager = new ItemManager();

    // these values will show to the user.
    String name;
    Emoji icon; // usually an emoji
    String description;
    Integer cost;

    // something like "cherry_blossom" - not shown to user.
    String id;

    ShopButton buyButton;

    public Item(String name, String description, String id, Emoji icon, Integer cost) {
        this.name = name;
        this.description = description;
        this.id = id;

        this.icon = icon;
        this.cost = cost;
        buyButton = new ShopButton(id,this,cost);

        Main.itemManager.addItem(this);
    }

    public Item(String name, String description, String id, Emoji icon) {
        this(name,description,id,icon,0);
    }
    public Item(String name, String description, String id) {
        this(name,description,id,null,0);
    }


    abstract public void use(Member owner);

    public String getName() {
        return name;
    }
    public String getDescription() {
        return description;
    }
    public Emoji getIcon() {
        return icon;
    }
    public String getIconString() {
        return getIcon().getFormatted();
    }

    public Integer getCost() {
        return cost;
    }

    public String getId() {
        return id;
    }
}
