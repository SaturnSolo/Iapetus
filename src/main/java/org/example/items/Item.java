package org.example.items;

import net.dv8tion.jda.api.entities.emoji.Emoji;
import net.dv8tion.jda.api.events.interaction.command.SlashCommandInteractionEvent;
import org.example.types.ItemId;

public abstract class Item {
	private final ItemId itemId;
	private final String id;
	private final String name;
	private final Emoji icon;
	private final String description;

	protected Item(ItemId itemId, String name, String description, Emoji icon) {
		this.itemId = itemId;
		this.id = itemId.key();
		this.name = name;
		this.description = description;
		this.icon = icon;
	}

	/** For InvalidItem only — no ItemId, uses a raw string key. */
	protected Item(String id, String name, String description, Emoji icon) {
		this.itemId = null;
		this.id = id;
		this.name = name;
		this.description = description;
		this.icon = icon;
	}

	abstract public boolean use(SlashCommandInteractionEvent event);

	public String getString() {
		return getString(false);
	}

	public String getString(boolean reverse) {
		if (reverse)
			return "%s %s".formatted(name, getIcon());
		return "%s %s".formatted(getIcon(), name);
	}

	public String getName() {
		return name;
	}

	public String getDescription() {
		return description;
	}

	public Emoji getIconEmoji() {
		return icon;
	}

	public String getIcon() {
		return getIconEmoji().getFormatted();
	}

	public ItemId getItemId() {
		return itemId;
	}

	public String getId() {
		return id;
	}
}
