package org.example.commands;

import net.dv8tion.jda.api.entities.User;
import net.dv8tion.jda.api.events.interaction.command.SlashCommandInteractionEvent;
import org.example.database.Database;
import org.example.structures.IapetusCommand;

import java.time.LocalDate;
import java.util.Objects;

public class DailyCommand extends IapetusCommand {
    public DailyCommand() {
        super("daily", "Daily bonus");
    }

    @Override
    public boolean runCommand(SlashCommandInteractionEvent event) {
        String userId = event.getUser().getId();
        LocalDate currentDate = LocalDate.now();

        if (Objects.equals(Database.getLastClaimDate(event.getUser()), LocalDate.now())) {
            event.reply("You've already claimed your daily bonus today!").queue();
            return true;
        }

        int baseReward = 6;
        int bonusAmount = calculateDailyBonus(event.getUser(), baseReward);

        Database.giveBerries(event.getUser(), bonusAmount);
        Database.updateLastClaimDate(userId, currentDate);

        event.reply("**Daily bonus claimed! You received %d berr".formatted(bonusAmount) + (bonusAmount == 1 ? "y" : "ies") + ".**").queue();
        return true;
    }

    private int calculateDailyBonus(User user, int baseReward) {
        return Database.getLastClaimDate(user).plusDays(1).equals(LocalDate.now()) ? baseReward * 2 : baseReward;
    }
}