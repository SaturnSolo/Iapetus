package org.example;

import net.dv8tion.jda.api.events.interaction.component.ButtonInteractionEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;

import java.util.HashMap;
import java.util.Map;

public class ButtonRouter extends ListenerAdapter {
	private final Map<String, ButtonModule> modules = new HashMap<>();

	public void register(String prefix, ButtonModule module) {
		modules.put(prefix, module);
	}

	@Override
	public void onButtonInteraction(ButtonInteractionEvent event) {
		String customId = event.getButton().getCustomId();
		if (customId == null)
			return;

		int colon = customId.indexOf(':');
		if (colon < 0)
			return;

		String prefix = customId.substring(0, colon);
		String rest = customId.substring(colon + 1);

		ButtonModule module = modules.get(prefix);
		if (module != null)
			module.onButton(rest, event);
	}
}
