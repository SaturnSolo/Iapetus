package org.example.types;

import net.dv8tion.jda.api.entities.User;

public record UserId(String value) {
	public static UserId of(User user) {
		return new UserId(user.getId());
	}
}
