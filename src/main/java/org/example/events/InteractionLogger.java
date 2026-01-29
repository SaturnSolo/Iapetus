package org.example.events;

import net.dv8tion.jda.api.events.interaction.GenericInteractionCreateEvent;
import net.dv8tion.jda.api.events.interaction.command.SlashCommandInteractionEvent;
import net.dv8tion.jda.api.events.interaction.component.ButtonInteractionEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;
import org.example.database.Database;

public class InteractionLogger extends ListenerAdapter {
    private String getGuildId(GenericInteractionCreateEvent event) {
        return event.getGuild() == null ? "DM" : event.getGuild().getId();
    }

	@Override
	public void onSlashCommandInteraction(SlashCommandInteractionEvent event) {
		Database.logUserGuild(event.getUser().getId(), getGuildId(event));
	}

	@Override
	public void onButtonInteraction(ButtonInteractionEvent event) {
		Database.logUserGuild(event.getUser().getId(), getGuildId(event));
	}
}
