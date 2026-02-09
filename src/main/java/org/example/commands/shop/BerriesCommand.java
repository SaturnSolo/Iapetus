package org.example.commands.shop;

import net.dv8tion.jda.api.events.interaction.command.SlashCommandInteractionEvent;
import org.example.Economy;
import org.example.structures.IapetusCommand;
import org.example.types.UserId;

public class BerriesCommand extends IapetusCommand {
	private final Economy economy;

	public BerriesCommand(Economy economy) {
		super("berries", "shows the amount of berries you have");
		this.economy = economy;
	}

	@Override
	public boolean runCommand(SlashCommandInteractionEvent event) {
		event.reply("**You have %d berries** 🍓".formatted(economy.getBalance(UserId.of(event.getUser())))).queue();
		return true;
	}
}
