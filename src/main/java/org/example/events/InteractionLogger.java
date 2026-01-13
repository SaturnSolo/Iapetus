package org.example.events;

import net.dv8tion.jda.api.entities.Guild;
import net.dv8tion.jda.api.events.interaction.command.SlashCommandInteractionEvent;
import net.dv8tion.jda.api.events.interaction.component.ButtonInteractionEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;
import org.example.database.Database;

public class InteractionLogger extends ListenerAdapter {
    @Override
    public void onSlashCommandInteraction(SlashCommandInteractionEvent event) {
        Guild guild = event.getGuild();
        String id = guild == null ? "DM" : guild.getId();
        Database.logUserGuild(event.getUser().getId(), id);
    }

    @Override
    public void onButtonInteraction(ButtonInteractionEvent event) {
        Guild guild = event.getGuild();
        String id = guild == null ? "DM" : guild.getId();
        Database.logUserGuild(event.getUser().getId(), id);
    }
}
