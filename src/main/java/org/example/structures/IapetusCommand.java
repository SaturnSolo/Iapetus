package org.example.structures;

import net.dv8tion.jda.api.events.interaction.command.SlashCommandInteractionEvent;
import net.dv8tion.jda.api.interactions.commands.build.Commands;
import net.dv8tion.jda.api.interactions.commands.build.SlashCommandData;

public abstract class IapetusCommand {
	private final String name;
	private final String description;

	private final SlashCommandData data;

	public IapetusCommand(String name, String description, SlashCommandData data) {
		this.name = name;
		this.description = description;
		this.data = data;
	}

	public IapetusCommand(String name, String description) {
		this.name = name;
		this.description = description;
		this.data = getSlash();
	}

	public IapetusCommand(SlashCommandData data) {
		this.name = data.getName();
		this.description = data.getDescription();
		this.data = data;
	}

	public abstract boolean runCommand(SlashCommandInteractionEvent event);

	public String getName() {
		return this.name;
	}

	public String getDescription() {
		return this.description;
	}

	public SlashCommandData getSlash() {
		if (this.data == null) {
			if (getName() == null || getDescription() == null) {
				throw new IllegalArgumentException();
			}
			return Commands.slash(getName(), getDescription());
		}
		return data;
	}
}
