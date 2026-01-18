package org.example.structures;

import org.example.ItemManager;
import org.example.items.Item;

import java.util.List;

public class Inventory {
    private final ItemManager itemMgr;
    private final List<Item> items;
    private final String userId;

    public Inventory(String userId, List<Item> items, ItemManager itemMgr) {
        this.userId = userId;
        this.items = items;
        this.itemMgr = itemMgr;
    }

    public Item get(int index) {
        return items.get(index);
    }

    public int size() {
        return items.size();
    }

    public Item remove(int index) {
        Item item = items.remove(index);
        itemMgr.takeItem(userId, item.getId());
        return item;
    }

    public boolean isEmpty() {
        return items.isEmpty();
    }

    public List<Item> getItems() {
        return items;
    }
}
