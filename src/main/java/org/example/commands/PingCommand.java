package org.example.commands;

import net.dv8tion.jda.api.events.interaction.command.SlashCommandInteractionEvent;
import org.example.structures.IapetusCommand;

public class PingCommand extends IapetusCommand {
	public PingCommand() {
		super("ping", "pongs!");
	}

	@Override
	public boolean runCommand(SlashCommandInteractionEvent event) {
		event.reply("Pong! 🌺").queue();
		return true;
	}
}
