package org.example.commands.shop;

import net.dv8tion.jda.api.entities.User;
import net.dv8tion.jda.api.events.interaction.command.SlashCommandInteractionEvent;
import net.dv8tion.jda.api.interactions.commands.OptionType;
import net.dv8tion.jda.api.interactions.commands.build.Commands;
import org.example.database.Database;
import org.example.structures.IapetusCommand;

public class BerriesCommand extends IapetusCommand {
	public BerriesCommand() {
		super(Commands.slash("berries", "shows the amount of berries a person has").addOption(OptionType.USER, "name", "who to check", false));
	}

	@Override
	public boolean runCommand(SlashCommandInteractionEvent event) {
		User target = event.getOption("name") == null ? event.getUser() : event.getOption("name").getAsUser();

		event.reply("**%s have %d berries** 🍓".formatted(target.getEffectiveName(), Database.getBerryAmount(target))).queue();
		return true;
	}
}
