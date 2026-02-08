package org.example.types;

import net.dv8tion.jda.api.entities.Guild;

public record GuildId(String value) {
	public static GuildId of(Guild guild) {
		return new GuildId(guild.getId());
	}
}
