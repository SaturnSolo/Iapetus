package org.example.utils;

import org.example.ItemManager;
import org.example.Main;
import org.example.database.SQLiteDataSource;
import org.example.items.InvalidItem;
import org.example.items.Item;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class InventoryUtils {
    public static Inventory getUserInventory(String userId) {
        final ItemManager im = Main.itemManager;
        List<Item> itemList = new ArrayList<>();
        try (
          final Connection connection = SQLiteDataSource.getConnection();
          final PreparedStatement ps = connection.prepareStatement("SELECT item_name, COUNT(*) AS item_count FROM inventory WHERE user_id = ? GROUP BY item_name")
        ) {
            ps.setString(1, userId);
            try (final ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    String itemName = rs.getString("item_name");
                    int itemCount = rs.getInt("item_count");

                    Item item = im.getItem(itemName);
                    if (item == null) item = new InvalidItem(itemName);
                    for (int i = 0; i < itemCount; i++) {
                        itemList.add(item);
                    }
                }

            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return new Inventory(userId, itemList);
    }


    public static class Inventory {
        private final ItemManager im = Main.itemManager;
        public List<Item> items;
        public final String userId;

        public Inventory(String userId, List<Item> items) {
            this.userId = userId;
            this.items = items;
        }

        public Item get(int index) {
            return items.get(index);
        }
        public int size() {
            return items.size();
        }
        public Item remove(int index) {
            Item item = items.remove(index);
            im.takeItem(userId, item.getId());
            return item;
        }

        public void takeItem(String itemId) {
            im.takeItem(userId,itemId);
        }
        public void takeItem(Item item) {
            im.takeItem(userId,item.getId());
        }
        public void giveItem(String itemId) {
            im.giveItem(userId,itemId);
        }


    }
}
