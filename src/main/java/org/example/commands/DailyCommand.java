package org.example.commands;

import net.dv8tion.jda.api.events.interaction.command.SlashCommandInteractionEvent;
import org.example.database.SQLiteDataSource;
import org.example.structures.IapetusCommand;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDate;

public class DailyCommand extends IapetusCommand {
    public DailyCommand() {
        super("daily", "Daily bonus");
    }

    @Override
    public boolean runCommand(SlashCommandInteractionEvent event) {
        String userId = event.getUser().getId();


        LocalDate currentDate = LocalDate.now();


        if (hasClaimedToday(userId, currentDate)) {
            event.reply("You've already claimed your daily bonus today!").queue();
            return true;
        }

        int baseReward = 6;
        int bonusAmount = calculateDailyBonus(userId, baseReward);

        giveUserBerries(userId, bonusAmount);
        updateLastClaimDate(userId, currentDate);

        event.reply("**Daily bonus claimed! You received " + bonusAmount + " berrie" + (bonusAmount == 1 ? "" : "s") + ".**").queue();
        return true;
    }

    private boolean hasClaimedToday(String userId, LocalDate currentDate) {
        try (Connection connection = SQLiteDataSource.getConnection();
             PreparedStatement ps = connection.prepareStatement("SELECT last_claim FROM daily_claims WHERE user_id = ?")) {
            ps.setString(1, userId);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    LocalDate lastClaimDate = LocalDate.ofEpochDay(rs.getLong("last_claim"));
                    return lastClaimDate.equals(currentDate);
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }

    private void updateLastClaimDate(String userId, LocalDate currentDate) {
        try (Connection connection = SQLiteDataSource.getConnection();
             PreparedStatement ps = connection.prepareStatement("INSERT INTO daily_claims (user_id, last_claim) VALUES (?, ?) " +
                     "ON CONFLICT(user_id) DO UPDATE SET last_claim = ?")) {
            ps.setString(1, userId);
            ps.setLong(2, currentDate.toEpochDay());  // Store the date as epoch day
            ps.setLong(3, currentDate.toEpochDay());
            ps.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public static void giveUserBerries(String userId, int amount) {
        try (Connection connection = SQLiteDataSource.getConnection();
             PreparedStatement ps = connection.prepareStatement("INSERT INTO user_berries (user_id, berry_count) VALUES (?, ?) " +
                     "ON CONFLICT(user_id) DO UPDATE SET berry_count = berry_count + ?")) {
            ps.setString(1, userId);
            ps.setInt(2, amount);
            ps.setInt(3, amount);
            ps.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    private int calculateDailyBonus(String userId, int baseReward) {
        try (Connection connection = SQLiteDataSource.getConnection();
             PreparedStatement ps = connection.prepareStatement("SELECT last_claim FROM daily_claims WHERE user_id = ?")) {
            ps.setString(1, userId);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    LocalDate lastClaimDate = LocalDate.ofEpochDay(rs.getLong("last_claim"));
                    LocalDate currentDate = LocalDate.now();

                    if (lastClaimDate.plusDays(1).equals(currentDate)) {
                        return baseReward * 2;
                    }
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return baseReward;
    }
}