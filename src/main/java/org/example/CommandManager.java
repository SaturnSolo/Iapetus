package org.example;

import net.dv8tion.jda.api.JDA;
import net.dv8tion.jda.api.events.interaction.command.SlashCommandInteractionEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;
import net.dv8tion.jda.api.interactions.commands.build.SlashCommandData;
import org.example.structures.IapetusCommand;
import org.jetbrains.annotations.NotNull;

import java.util.*;

public class CommandManager extends ListenerAdapter {
	private final Map<String, IapetusCommand> commands = new HashMap<>();

	@Override
	public void onSlashCommandInteraction(@NotNull SlashCommandInteractionEvent event) {
		String commandName = event.getName();
		IapetusCommand command = getCommand(commandName);
		if (command == null)
			return;

		boolean success = command.runCommand(event);
		if (!success) {
			event.reply("An unhandled error occured.").queue();
		}
	}

	public void register(JDA jda) {
		List<SlashCommandData> commands = new ArrayList<>();
		this.commands.values().forEach(command -> commands.add(command.getSlash()));
		jda.updateCommands().addCommands(commands).queue();
	}

	public void addCommands(List<IapetusCommand> commands) {
		commands.forEach(command -> this.commands.put(command.getName(), command));
	}

	public void addCommands(IapetusCommand... commands) {
		addCommands(Arrays.asList(commands));
	}

	public Map<String, IapetusCommand> getCommands() {
		return Collections.unmodifiableMap(this.commands);
	}

	public IapetusCommand getCommand(String name) {
		return this.commands.get(name);
	}
}
