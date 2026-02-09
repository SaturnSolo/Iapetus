package org.example.structures;

import org.example.database.Database;
import org.example.items.Item;
import org.example.types.UserId;

import java.util.List;

public class Inventory {
	private final UserId userId;
	private final List<Item> items;

	public Inventory(UserId userId, List<Item> items) {
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
		Database.takeItem(userId.value(), item.getId());
		return item;
	}

	public boolean isEmpty() {
		return items.isEmpty();
	}

	public List<Item> getItems() {
		return items;
	}
}
