package org.example.commands.give;

import net.dv8tion.jda.api.entities.User;
import net.dv8tion.jda.api.events.interaction.command.SlashCommandInteractionEvent;
import net.dv8tion.jda.api.interactions.commands.OptionType;
import net.dv8tion.jda.api.interactions.commands.build.Commands;
import org.example.database.Database;
import org.example.structures.IapetusCommand;

public class GiveCommand extends IapetusCommand {
	public GiveCommand() {
		super(Commands.slash("give", "give berries to user")
				.addOption(OptionType.MENTIONABLE, "mention", "who to give berries to", true)
				.addOption(OptionType.INTEGER, "give", "number of berries to give", true));
	}

	@Override
	public boolean runCommand(SlashCommandInteractionEvent event) {
		String sourceUserId = event.getUser().getId();
		User targetUserId = event.getOption("mention").getAsUser(); // NPE impossible
		int transferAmount = event.getOption("give").getAsInt(); // NPE impossible

		if (transferAmount <= 0) {
			event.reply("You must specify an amount greater than 0.").setEphemeral(true).queue();
			return false;
		}

		if (sourceUserId.equals(targetUserId.getId())) {
			event.reply("You cannot give berries to yourself.").setEphemeral(true).queue();
			return false;
		}

		int totalBerries = Database.getBerryAmount(sourceUserId);
		if (totalBerries < transferAmount) {
			event.reply("You do not have enough berries to transfer.").setEphemeral(true).queue();
			return false;
		}

		Database.takeBerries(sourceUserId, transferAmount);
		Database.giveBerries(targetUserId, transferAmount);

		event.reply("You have successfully transferred %d berries to %s!".formatted(transferAmount, targetUserId))
				.queue();

		return true;
	}
}
