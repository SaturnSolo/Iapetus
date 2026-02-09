package org.example.database;

import net.dv8tion.jda.api.entities.Guild;
import net.dv8tion.jda.api.entities.channel.Channel;
import org.example.ItemManager;
import org.example.structures.Inventory;
import org.example.types.ItemId;
import org.example.types.UserId;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDate;
import java.util.*;
import java.util.stream.Collectors;

@SuppressWarnings("SqlNoDataSourceInspection")
public class Database {

	// Inventory + items
	public static Inventory getUserInventory(UserId userId, ItemManager itemMgr) {
		Map<ItemId, Integer> items = new EnumMap<>(ItemId.class);
		try (final Connection connection = SQLiteDataSource.getConnection();
				final PreparedStatement ps = connection.prepareStatement(
						"SELECT item_name, COUNT(*) AS item_count FROM inventory WHERE user_id = ? GROUP BY item_name")) {
			ps.setString(1, userId.value());
			try (final ResultSet rs = ps.executeQuery()) {
				while (rs.next()) {
					String itemName = rs.getString("item_name");
					int itemCount = rs.getInt("item_count");

					if (itemMgr.getItem(itemName) != null) {
						ItemId id = ItemId.valueOf(itemName.toUpperCase());
						items.put(id, itemCount);
					}
				}
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return new Inventory(items);
	}

	public static boolean hasItem(String userId, String id, int amount) {
		try (final Connection connection = SQLiteDataSource.getConnection();
				final PreparedStatement ps = connection
						.prepareStatement("SELECT COUNT(*) FROM inventory WHERE user_id = ? AND item_name = ?")) {
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

	public static void giveItem(String userId, String id) {
		try (final Connection connection = SQLiteDataSource.getConnection();
				final PreparedStatement ps = connection
						.prepareStatement("INSERT INTO inventory (user_id, item_name) VALUES (?, ?)")) {
			ps.setString(1, userId);
			ps.setString(2, id);
			ps.executeUpdate();
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}

	public static void takeItem(String userId, String id) {
		try (final Connection connection = SQLiteDataSource.getConnection();
				final PreparedStatement ps = connection.prepareStatement(
						"DELETE FROM inventory WHERE rowid = ( SELECT rowid from inventory where user_id = ? AND item_name = ? LIMIT 1 )")) {
			ps.setString(1, userId);
			ps.setString(2, id);
			ps.executeUpdate();
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}

	// Pets
	public static List<String> getHatchedPets(String userId) {
		List<String> petList = new ArrayList<>();

		try (Connection connection = SQLiteDataSource.getConnection();
				PreparedStatement ps = connection.prepareStatement("SELECT pet FROM hatch_log WHERE user_id = ?")) {
			ps.setString(1, userId);

			try (ResultSet rs = ps.executeQuery()) {
				while (rs.next()) {
					petList.add(rs.getString("pet"));
				}
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}

		return petList;
	}

	// Berries
	public static void giveBerries(String userId, int amount) {
		try (final Connection connection = SQLiteDataSource.getConnection();
				final PreparedStatement ps = connection.prepareStatement(
						"INSERT INTO user_berries (user_id, berry_count) VALUES (?, ?) ON CONFLICT(user_id) DO UPDATE SET berry_count = berry_count + ?")) {
			ps.setString(1, userId);
			ps.setInt(2, amount);
			ps.setInt(3, amount);
			ps.executeUpdate();
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}

	public static void takeBerries(String userId, int amount) {
		giveBerries(userId, -amount);
	}

	public static int getBerryAmount(String userId) {
		try (final Connection connection = SQLiteDataSource.getConnection();
				final PreparedStatement ps = connection
						.prepareStatement("SELECT berry_count FROM user_berries WHERE user_id = ?")) {
			ps.setString(1, userId);
			try (final ResultSet rs = ps.executeQuery()) {
				if (rs.next()) {
					return rs.getInt("berry_count");
				} else {
					return 0;
				}
			}
		} catch (SQLException e) {
			e.printStackTrace();
			return 0;
		}
	}

	public static LocalDate getLastClaimDate(String userId) {
		try (Connection connection = SQLiteDataSource.getConnection();
				PreparedStatement ps = connection
						.prepareStatement("SELECT last_claim FROM daily_claims WHERE user_id = ?")) {
			ps.setString(1, userId);
			try (ResultSet rs = ps.executeQuery()) {
				if (rs.next()) {
					return LocalDate.ofEpochDay(rs.getLong("last_claim"));
				}
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return null;
	}

	public static void updateLastClaimDate(String userId, LocalDate currentDate) {
		try (Connection connection = SQLiteDataSource.getConnection();
				PreparedStatement ps = connection
						.prepareStatement("INSERT INTO daily_claims (user_id, last_claim) VALUES (?, ?) "
								+ "ON CONFLICT(user_id) DO UPDATE SET last_claim = ?")) {
			ps.setString(1, userId);
			ps.setLong(2, currentDate.toEpochDay()); // Store the date as epoch day
			ps.setLong(3, currentDate.toEpochDay());
			ps.executeUpdate();
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}

	private static Map<Long, Integer> getUserBerryMap(long guildId) {
		Map<Long, Integer> result = new LinkedHashMap<>();
		try (Connection connection = SQLiteDataSource.getConnection();
				PreparedStatement ps = connection.prepareStatement("SELECT ub.user_id, ub.berry_count "
						+ "FROM user_berries ub " + "JOIN user_guilds ug ON ub.user_id = ug.user_id "
						+ "WHERE ug.guild_id = ? " + "ORDER BY ub.berry_count DESC")) {

			ps.setLong(1, guildId);

			try (ResultSet rs = ps.executeQuery()) {
				while (rs.next()) {
					result.put(Long.parseLong(rs.getString("user_id")), rs.getInt("berry_count"));
				}
			}

		} catch (SQLException e) {
			e.printStackTrace();
		}

		return result;
	}

	public static Map<Long, Integer> getTopNBerryHolders(long guildId, int n) {
		return getUserBerryMap(guildId).entrySet().stream().limit(n)
				.collect(Collectors.toMap(Map.Entry::getKey, Map.Entry::getValue, (a, b) -> a, LinkedHashMap::new));
	}

	// Ignored channels
	public static void addIgnoredChannel(Guild guild, Channel channel) {
		try (Connection connection = SQLiteDataSource.getConnection();
				PreparedStatement ps = connection
						.prepareStatement("INSERT INTO ignored_channels (guild_id, channel_id) VALUES (?, ?)")) {
			ps.setString(1, guild.getId());
			ps.setString(2, channel.getId());
			ps.executeUpdate();
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}

	public static int removeIgnoredChannel(Guild guild, Channel channel) {
		try (Connection connection = SQLiteDataSource.getConnection();
				PreparedStatement ps = connection
						.prepareStatement("DELETE FROM ignored_channels WHERE guild_id = ? AND channel_id = ?")) {
			ps.setString(1, guild.getId());
			ps.setString(2, channel.getId());
			return ps.executeUpdate();
		} catch (SQLException e) {
			e.printStackTrace();
			return -1;
		}
	}

	public static List<Long> getIgnoredChannels(Guild guild) {
		List<Long> channels = new ArrayList<>();
		try (Connection connection = SQLiteDataSource.getConnection();
				PreparedStatement ps = connection
						.prepareStatement("SELECT channel_id FROM ignored_channels WHERE guild_id = ?");) {
			ps.setString(1, guild.getId());
			try (ResultSet rs = ps.executeQuery()) {
				while (rs.next()) {
					channels.add(Long.parseLong(rs.getString("channel_id")));
				}
			}
		} catch (SQLException e) {
			throw new RuntimeException(e);
		}
		return channels;
	}

	// Logging
	public static void logUserGuild(String userId, String guildId) {
		String query = "INSERT OR IGNORE INTO user_guilds (user_id, guild_id) VALUES (?, ?)";

		try (Connection connection = SQLiteDataSource.getConnection();
				PreparedStatement ps = connection.prepareStatement(query)) {
			ps.setString(1, userId);
			ps.setString(2, guildId);
			ps.executeUpdate();
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}

	public static void logLootInDatabase(String userId, String loot) {
		try (Connection connection = SQLiteDataSource.getConnection();
				PreparedStatement ps = connection
						.prepareStatement("INSERT INTO key_log (user_id, loot) VALUES (?, ?)")) {
			ps.setString(1, userId);
			ps.setString(2, loot);
			ps.executeUpdate();
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}

	public static void logHatchInDatabase(String userId, String pet) {
		try (Connection connection = SQLiteDataSource.getConnection();
				PreparedStatement ps = connection
						.prepareStatement("INSERT INTO hatch_log (user_id, pet) VALUES (?, ?)")) {
			ps.setString(1, userId);
			ps.setString(2, pet);
			ps.executeUpdate();
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}
}
