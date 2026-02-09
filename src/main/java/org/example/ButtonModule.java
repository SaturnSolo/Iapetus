package org.example;

import net.dv8tion.jda.api.events.interaction.component.ButtonInteractionEvent;

@FunctionalInterface
public interface ButtonModule {
	void onButton(String id, ButtonInteractionEvent event);
}
