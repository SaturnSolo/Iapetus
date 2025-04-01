package org.example.commands.shop;

import net.dv8tion.jda.api.events.interaction.command.SlashCommandInteractionEvent;
import org.example.database.SQLiteDataSource;
import org.example.structures.IapetusCommand;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class BerriesCommand extends IapetusCommand {
    public BerriesCommand() {
        super("berries", "shows the amount of berries you have");
    }

    @Override
    public boolean runCommand(SlashCommandInteractionEvent event) {
        String userId = event.getMember().getId();
        try (final Connection connection = SQLiteDataSource.getConnection();
             final PreparedStatement ps = connection.prepareStatement("SELECT berry_count FROM user_berries WHERE user_id = ?")) {
            ps.setString(1, userId);
            try (final ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    int berryCount = rs.getInt("berry_count");
                    event.reply("**Error 404 " + berryCount + " message missing** 🍓").queue();
//                    event.reply("**You have " + berryCount + " berries** 🍓").queue();
                } else {
                    event.reply("**You have 0 berries** 🍓").queue();
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
        return true;
    }
}
