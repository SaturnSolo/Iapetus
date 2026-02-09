package org.example.commands.give;

import net.dv8tion.jda.api.entities.User;
import net.dv8tion.jda.api.events.interaction.command.SlashCommandInteractionEvent;
import net.dv8tion.jda.api.interactions.commands.OptionType;
import net.dv8tion.jda.api.interactions.commands.build.Commands;
import org.example.Economy;
import org.example.Economy.TransferResult;
import org.example.structures.IapetusCommand;
import org.example.types.UserId;

public class GiveCommand extends IapetusCommand {
	private final Economy economy;

	public GiveCommand(Economy economy) {
		super(Commands.slash("give", "give berries to user")
				.addOption(OptionType.MENTIONABLE, "mention", "who to give berries to", true)
				.addOption(OptionType.INTEGER, "give", "number of berries to give", true));
		this.economy = economy;
	}

	@Override
	public boolean runCommand(SlashCommandInteractionEvent event) {
		UserId sourceUserId = UserId.of(event.getUser());
		User targetUser = event.getOption("mention").getAsUser(); // NPE impossible
		int transferAmount = event.getOption("give").getAsInt(); // NPE impossible

		TransferResult result = economy.transfer(sourceUserId, UserId.of(targetUser), transferAmount);
		switch (result) {
			case BAD_AMOUNT -> event.reply("You must specify an amount greater than 0.").setEphemeral(true).queue();
			case SELF_TRANSFER -> event.reply("You cannot give berries to yourself.").setEphemeral(true).queue();
			case INSUFFICIENT_FUNDS ->
				event.reply("You do not have enough berries to transfer.").setEphemeral(true).queue();
			case OK ->
				event.reply("You have successfully transferred %d berries to %s!".formatted(transferAmount, targetUser))
						.queue();
		}

		return true;
	}
}
