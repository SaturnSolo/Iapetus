package org.example.events;

import net.dv8tion.jda.api.events.interaction.command.SlashCommandInteractionEvent;
import net.dv8tion.jda.api.events.interaction.component.ButtonInteractionEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;
import org.example.database.Database;

public class InteractionLogger extends ListenerAdapter {
	@Override
	public void onSlashCommandInteraction(SlashCommandInteractionEvent event) {
		if (event.getGuild() == null) return;
		Database.logUserGuild(event.getUser().getId(), event.getGuild().getId());
	}

	@Override
	public void onButtonInteraction(ButtonInteractionEvent event) {
		if (event.getGuild() == null) return;
		Database.logUserGuild(event.getUser().getId(), event.getGuild().getId());
	}
}
