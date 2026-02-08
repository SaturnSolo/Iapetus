package org.example;

import org.example.database.Database;
import org.example.items.Item;
import org.example.types.ItemId;
import org.example.types.UserId;

import java.util.*;

public class ItemManager {
	private final Map<ItemId, Item> items = new EnumMap<>(ItemId.class);

	public void register(Item item) {
		items.put(item.getItemId(), item);
	}

	public Map<ItemId, Item> getItems() {
		return Collections.unmodifiableMap(this.items);
	}

	public Item getItem(ItemId id) {
		return this.items.get(id);
	}

	/** Lookup by string key (for DB/Discord values). Returns null if not found. */
	public Item getItem(String key) {
		try {
			return items.get(ItemId.valueOf(key.toUpperCase()));
		} catch (IllegalArgumentException e) {
			return null;
		}
	}

	/**
	 * Give a user an item.
	 *
	 * @param userId
	 *            the discord id of the user.
	 * @param id
	 *            the id of the item to give.
	 */
	public void giveItem(UserId userId, ItemId id) {
		Database.giveItem(userId.value(), id.key());
	}

	/**
	 * Give a user multiple of an item.
	 *
	 * @param userId
	 *            the discord id of the user.
	 * @param id
	 *            the id of the item to give.
	 * @param amount
	 *            the amount of the item to give.
	 */
	public void giveItem(UserId userId, ItemId id, int amount) {
		for (int i = 0; i < amount; ++i)
			giveItem(userId, id);
	}

	/**
	 * Takes an item from a user.
	 *
	 * @param userId
	 *            the discord id of the user.
	 * @param id
	 *            the id of the item to take.
	 */
	public void takeItem(UserId userId, ItemId id) {
		Database.takeItem(userId.value(), id.key());
	}

	/**
	 * Check if a user has an item.
	 *
	 * @param userId
	 *            the discord id of the user.
	 * @param id
	 *            the id of the item to check.
	 * @return true if they have 1 or more.
	 */
	public boolean hasItem(UserId userId, ItemId id) {
		return hasItem(userId, id, 1);
	}

	/**
	 * check if a user has multiple of an item.
	 *
	 * @param userId
	 *            the discord id of the user
	 * @param id
	 *            the id of the item to check.
	 * @param amount
	 *            the amount of items needed return true.
	 * @return true if it has {amount} or more.
	 */
	public boolean hasItem(UserId userId, ItemId id, int amount) {
		return Database.hasItem(userId.value(), id.key(), amount);
	}
}
