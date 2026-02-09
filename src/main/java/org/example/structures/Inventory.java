package org.example.structures;

import org.example.types.ItemId;

import java.util.Map;

public record Inventory(Map<ItemId, Integer> items) {
	public boolean has(ItemId item) {
		return items.getOrDefault(item, 0) > 0;
	}

	public int count(ItemId item) {
		return items.getOrDefault(item, 0);
	}

	public int totalCount() {
		return items.values().stream().mapToInt(i -> i).sum();
	}

	public boolean isEmpty() {
		return items.isEmpty();
	}
}
