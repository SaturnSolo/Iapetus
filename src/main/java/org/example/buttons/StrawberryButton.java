package org.example.buttons;

import net.dv8tion.jda.api.entities.Message;
import net.dv8tion.jda.api.events.interaction.component.ButtonInteractionEvent;
import net.dv8tion.jda.api.interactions.components.buttons.Button;
import org.example.database.SQLiteDataSource;
import org.example.structures.IapetusButton;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;

public class StrawberryButton extends IapetusButton {
    public StrawberryButton() {
        super(Button.primary("strawberry","🍓"));
    }

    @Override
    public void run(ButtonInteractionEvent event) {
        String mention = event.getInteraction().getUser().getAsMention();
        String userId = event.getInteraction().getUser().getId();

        Message message = event.getMessage();

        // edit first to avoid duplication (faster response!)
        // if we do decide to fix duplicated strawberries we can put both of these in the same action.
        event.editButton(Button.secondary("Error 404: Missing Message", "✨").asDisabled()).queue();
        message.reply("🍓 **Error 404: Message Missing** " + mention).queue();


        try (final Connection connection = SQLiteDataSource.getConnection();
             final PreparedStatement ps = connection.prepareStatement("INSERT INTO user_berries (user_id, berry_count) VALUES (?, ?) ON CONFLICT(user_id) DO UPDATE SET berry_count = berry_count + 1")) {
            ps.setString(1, userId);
            ps.setInt(2, 1);
            ps.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}
