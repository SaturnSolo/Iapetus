package org.example.commands;

import net.dv8tion.jda.api.events.interaction.command.SlashCommandInteractionEvent;
import org.example.Economy;
import org.example.structures.IapetusCommand;
import org.example.types.UserId;

import java.time.LocalDate;

public class DailyCommand extends IapetusCommand {
	private final Economy economy;

	public DailyCommand(Economy economy) {
		super("daily", "Daily bonus");
		this.economy = economy;
	}

	@Override
	public boolean runCommand(SlashCommandInteractionEvent event) {
		UserId userId = UserId.of(event.getUser());
		LocalDate currentDate = LocalDate.now();
		LocalDate lastClaim = economy.getLastClaim(userId);

		if (currentDate.equals(lastClaim)) {
			event.reply("You've already claimed your daily bonus today!").queue();
			return true;
		}

		int baseReward = 6;
		int bonusAmount = lastClaim != null && lastClaim.plusDays(1).equals(currentDate) ? baseReward * 2 : baseReward;

		economy.reward(userId, bonusAmount);
		economy.recordClaim(userId, currentDate);

		event.reply("**Daily bonus claimed! You received %d berr".formatted(bonusAmount)
				+ (bonusAmount == 1 ? "y" : "ies") + ".**").queue();
		return true;
	}
}
