package org.example;

import org.example.database.Database;
import org.example.items.Item;

import java.util.*;

public class ItemManager {
    private final Map<String, Item> items = new HashMap<>();

    public void addItem(Item item) {
        items.put(item.getId(), item);
    }

    public Map<String, Item> getItems() {
        return this.items;
    }

    public Item getItem(String name) {
        return this.items.get(name);
    }

    /**
     * Give a user an item.
     * 
     * @param userId the discord id of the user.
     * @param id     the id of the item to give.
     */
    public void giveItem(String userId, String id) {
        Database.giveItem(userId, id);
    }

    /**
     * Give a user multiple of an item.
     * 
     * @param userId the discord id of the user.
     * @param id     the id of the item to give.
     * @param amount the amount of the item to give.
     */
    public void giveItem(String userId, String id, int amount) {
        for (int i = 0; i < amount; ++i) giveItem(userId, id);
    }

    /**
     * Takes an item from a user.
     * 
     * @param userId the discord id of the user.
     * @param id     the id of the item to take.
     */
    public void takeItem(String userId, String id) {
        Database.takeItem(userId, id);
    }

    /**
     * Check if a user has an item.
     * 
     * @param userId the discord id of the user.
     * @param id     the id of the item to check.
     * @return true if they have 1 or more.
     */
    public boolean hasItem(String userId, String id) {
        return hasItem(userId, id, 1);
    }

    /**
     * check if a user has multiple of an item.
     * 
     * @param userId the discord id of the user
     * @param id     the id of the item to check.
     * @param amount the amount of items needed return true.
     * @return true if it has {amount} or more.
     */
    public boolean hasItem(String userId, String id, int amount) {
        return Database.hasItem(userId, id, amount);
    }
}
