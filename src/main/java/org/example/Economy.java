package org.example;

import org.example.database.Database;
import org.example.types.ItemId;
import org.example.types.UserId;

import java.time.LocalDate;
import java.util.Map;

public class Economy {
	private final ItemManager itemMgr;

	public Economy(ItemManager itemMgr) {
		this.itemMgr = itemMgr;
	}

	public enum TransferResult {
		OK, SELF_TRANSFER, BAD_AMOUNT, INSUFFICIENT_FUNDS
	}

	public int getBalance(UserId userId) {
		return Database.getBerryAmount(userId.value());
	}

	public Map<Long, Integer> getLeaderboard(long guildId, int limit) {
		return Database.getTopNBerryHolders(guildId, limit);
	}

	public LocalDate getLastClaim(UserId userId) {
		return Database.getLastClaimDate(userId.value());
	}

	public void reward(UserId userId, int amount) {
		Database.giveBerries(userId.value(), amount);
	}

	public TransferResult transfer(UserId from, UserId to, int amount) {
		if (amount <= 0)
			return TransferResult.BAD_AMOUNT;
		if (from.equals(to))
			return TransferResult.SELF_TRANSFER;
		if (getBalance(from) < amount)
			return TransferResult.INSUFFICIENT_FUNDS;

		Database.takeBerries(from.value(), amount);
		Database.giveBerries(to.value(), amount);
		return TransferResult.OK;
	}

	public void recordClaim(UserId userId, LocalDate date) {
		Database.updateLastClaimDate(userId.value(), date);
	}

	public boolean purchase(UserId userId, ItemId itemId, int cost) {
		if (getBalance(userId) < cost)
			return false;
		Database.takeBerries(userId.value(), cost);
		itemMgr.giveItem(userId, itemId);
		return true;
	}
}
