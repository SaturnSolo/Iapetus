package org.example.commands;

import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.components.actionrow.ActionRow;
import net.dv8tion.jda.api.components.buttons.Button;
import net.dv8tion.jda.api.entities.MessageEmbed;
import net.dv8tion.jda.api.entities.emoji.Emoji;
import net.dv8tion.jda.api.events.interaction.command.SlashCommandInteractionEvent;
import net.dv8tion.jda.api.interactions.commands.build.Commands;
import org.example.Economy;
import org.example.ItemManager;
import org.example.buttons.AdventureButtons;
import org.example.structures.IapetusCommand;
import org.example.utils.IapetusColor;

import java.util.*;
import java.util.concurrent.TimeUnit;

public class AdventureCommands extends IapetusCommand {
	private final AdventureButtons adventureButtons;
	private final Map<String, Date> adventureCooldowns = new HashMap<>();
	private final long adventureCooldownTime = TimeUnit.SECONDS.toMillis(5);

	public AdventureCommands(ItemManager itemMgr, Economy economy, Random rng) {
		super(Commands.slash("adventure", "go on an adventure somewhere"));
		this.adventureButtons = new AdventureButtons(itemMgr, economy, rng, adventureCooldowns);
	}

	public AdventureButtons getAdventureButtons() {
		return adventureButtons;
	}

	@Override
	public boolean runCommand(SlashCommandInteractionEvent event) {
		String userId = event.getUser().getId();

		MessageEmbed embed = new EmbedBuilder().setTitle("**Welcome Adventurer**")
				.setDescription("Welcome %s to the adventure menu. Are you ready and prepared to go on an adventure?"
						.formatted(event.getUser().getAsMention()))
				.setColor(IapetusColor.DARK_GREEN).setThumbnail(event.getUser().getAvatarUrl()).build();

		Date cooldownEnd = adventureCooldowns.get(userId);
		if (cooldownEnd != null && new Date().before(cooldownEnd)) {
			event.reply("You need to rest! You can adventure again <t:" + cooldownEnd.getTime() / 1000 + ":R>.")
					.queue();
			return true;
		}
		adventureCooldowns.put(userId, new Date(System.currentTimeMillis() + adventureCooldownTime));
		event.replyEmbeds(embed)
				.addComponents(ActionRow.of(Button.success("adv:confirm", "Confirm").withEmoji(Emoji.fromUnicode("✅")),
						Button.secondary("adv:cancel", Emoji.fromUnicode("❌"))))
				.queue();
		return true;
	}
}
