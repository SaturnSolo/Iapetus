package org.example;

import org.example.database.SQLiteDataSource;
import org.example.items.Item;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.*;

public class ItemManager {
    private final Map<String, Item> items = new HashMap<>();

    public void addItems(List<Item> items) {
        items.forEach(item -> this.items.put(item.getId(), item));
    }
    public void addItems(Item... commands) {
        addItems(Arrays.asList(commands));
    }
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
     * @param userId the discord id of the user.
     * @param id the id of the item to give.
     */
    public void giveItem(String userId, String id) {
        try (
          final Connection connection = SQLiteDataSource.getConnection();
          final PreparedStatement ps = connection.prepareStatement("INSERT INTO inventory (user_id, item_name) VALUES (?, ?)")
        ) {
            ps.setString(1, userId);
            ps.setString(2, id);
            ps.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    /**
     * Give a user multiple of an item.
     * @param userId the discord id of the user.
     * @param id the id of the item to give.
     * @param amount the amount of the item to give.
     */
    public void giveItem(String userId, String id, Integer amount) {
        for(int i = 0; i < amount; ++i) giveItem(userId, id);
    }

    /**
     * Takes an item from a user.
     * @param userId the discord id of the user.
     * @param id the id of the item to take.
     */
    public void takeItem(String userId, String id) {
        try (
          final Connection connection = SQLiteDataSource.getConnection();
          final PreparedStatement ps = connection.prepareStatement("DELETE FROM inventory WHERE rowid = ( SELECT rowid from inventory where user_id = ? AND item_name = ? LIMIT 1 )") // "DELETE FROM inventory WHERE user_id = ? AND item_name = ? LIMIT ?"
        ) {
            ps.setString(1, userId);
            ps.setString(2, id);
            ps.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    /**
     * Takes multiple of an item from a user.
     * @param userId the discord id of the user.
     * @param id the id of the item to take.
     * @param amount the amount to take.
     */
    public void takeItem(String userId, String id, Integer amount) {
        for(int i = 0; i < amount; ++i) takeItem(userId, id);
    }

    /**
     * Check if a user has an item.
     * @param userId the discord id of the user.
     * @param id the id of the item to check.
     * @return true if they have 1 or more.
     */
    public boolean hasItem(String userId, String id) {
        return hasItem(userId,id,1);
    }

    /**
     * check if a user has multiple of an item.
     * @param userId the discord id of the user
     * @param id the id of the item to check.
     * @param amount the amount of items needed return true.
     * @return true if it has {amount} or more.
     */
    public boolean hasItem(String userId, String id, Integer amount) {
        try (
          final Connection connection = SQLiteDataSource.getConnection();
          final PreparedStatement ps = connection.prepareStatement("SELECT COUNT(*) FROM inventory WHERE user_id = ? AND item_name = ?")
        ) {
            ps.setString(1, userId);
            ps.setString(2, id);

            try (final ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    int count = rs.getInt(1);
                    return count >= amount;
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }
}
