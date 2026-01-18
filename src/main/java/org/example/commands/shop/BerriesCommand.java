package org.example.commands.shop;

import net.dv8tion.jda.api.events.interaction.command.SlashCommandInteractionEvent;
import org.example.database.Database;
import org.example.structures.IapetusCommand;

public class BerriesCommand extends IapetusCommand {
	public BerriesCommand() {
		super("berries", "shows the amount of berries you have");
	}

	@Override
	public boolean runCommand(SlashCommandInteractionEvent event) {
		event.reply("**You have %d berries** 🍓".formatted(Database.getBerryAmount(event.getUser()))).queue();
		return true;
	}
}
