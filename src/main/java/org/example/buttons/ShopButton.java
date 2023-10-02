package org.example.buttons;

import net.dv8tion.jda.api.events.interaction.component.ButtonInteractionEvent;
import net.dv8tion.jda.api.interactions.components.buttons.Button;
import org.example.database.SQLiteDataSource;
import org.example.structures.IapetusButton;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class ShopButton extends IapetusButton {
    private String item;
    private Integer cost;

    public ShopButton(String id, String item, Integer cost) {
        super(Button.primary(id, item));
        this.item = item;
        this.cost = cost;
    }

    @Override
    public void run(ButtonInteractionEvent event) {
        String componentId = event.getComponentId();
        String userId = event.getMember().getId();

        int userBerries = getUserBerries(userId);
        if (userBerries >= cost) {
            deductBerries(userId, cost);
            addToInventory(userId, item);
            event.reply("**You have purchased a " + item + " and added it to your inventory!**").setEphemeral(true).queue();
        } else {
            event.reply("**Insufficient berries!**").setEphemeral(true).queue();
        }
    }

    private int getUserBerries(String userId) {
        int userBerries = 0;
        try (Connection connection = SQLiteDataSource.getConnection();
             PreparedStatement ps = connection.prepareStatement("SELECT berry_count FROM user_berries WHERE user_id = ?")) {
            ps.setString(1, userId);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    userBerries = rs.getInt("berry_count");
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return userBerries;
    }

    private void deductBerries(String userId, int berriesToDeduct) {
        try (Connection connection = SQLiteDataSource.getConnection();
             PreparedStatement ps = connection.prepareStatement("UPDATE user_berries SET berry_count = berry_count - ? WHERE user_id = ?")) {
            ps.setInt(1, berriesToDeduct);
            ps.setString(2, userId);
            ps.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    private void addToInventory(String userId, String itemName) {
        try (Connection connection = SQLiteDataSource.getConnection();
             PreparedStatement ps = connection.prepareStatement("INSERT INTO inventory (user_id, item_name) VALUES (?, ?)")) {
            ps.setString(1, userId);
            ps.setString(2, itemName);
            ps.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}
