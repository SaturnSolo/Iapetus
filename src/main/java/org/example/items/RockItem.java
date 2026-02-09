package org.example.items;

import net.dv8tion.jda.api.components.actionrow.ActionRow;
import net.dv8tion.jda.api.components.buttons.Button;
import net.dv8tion.jda.api.entities.emoji.Emoji;
import net.dv8tion.jda.api.entities.emoji.UnicodeEmoji;
import net.dv8tion.jda.api.events.interaction.command.SlashCommandInteractionEvent;
import net.dv8tion.jda.api.events.interaction.component.ButtonInteractionEvent;
import org.example.ButtonModule;
import org.example.types.ItemId;

import java.util.Random;

public class RockItem extends Item implements ButtonModule {
	private final Random rng;

	public RockItem(Random rng) {
		super(ItemId.ROCK, "Rock", "It's a rock", Emoji.fromUnicode("🪨"));
		this.rng = rng;
	}

	@Override
	public boolean use(SlashCommandInteractionEvent event) {
		event.reply("**Rock, paper, or scissors?**")
				.addComponents(ActionRow.of(Button.primary("rps:rock", Emoji.fromUnicode("🪨")),
						Button.primary("rps:paper", Emoji.fromUnicode("🗞")),
						Button.primary("rps:scissors", Emoji.fromUnicode("✂"))))
				.queue();
		return false;
	}

	@Override
	public void onButton(String id, ButtonInteractionEvent event) {
		String computer = switch (rng.nextInt(3)) {
			case 0 -> "rock";
			case 1 -> "paper";
			default -> "scissors";
		};

		int result = switch (id) {
			case String s when s.equals(computer) -> 0;
			case "rock" -> computer.equals("scissors") ? 1 : -1;
			case "paper" -> computer.equals("rock") ? 1 : -1;
			case "scissors" -> computer.equals("paper") ? 1 : -1;
			default -> 2;
		};

		if (result == 2) {
			event.reply("an error occured processing this request, please try again.").queue();
			return;
		}

		// Disable the buttons so they can't be used again
		event.getMessage()
				.editMessageComponents(event.getMessage().getComponents().getFirst().asActionRow().asDisabled())
				.queue();

		String playerEmoji = Emojis.valueOf(id.toUpperCase()).get();
		String computerEmoji = Emojis.valueOf(computer.toUpperCase()).get();

		switch (result) {
			case 0 -> event.reply("**You both chose** " + playerEmoji + "\n **it's a draw!**").queue();
			case 1 ->
				event.reply("**You chose** " + playerEmoji + " **The bot chose** " + computerEmoji + "\n **You win!**")
						.queue();
			case -1 ->
				event.reply("**You chose** " + playerEmoji + " **The bot chose** " + computerEmoji + "\n **You lose!**")
						.queue();
		}
	}

	private enum Emojis {
		ROCK(Emoji.fromUnicode("🪨")), PAPER(Emoji.fromUnicode("🗞")), SCISSORS(Emoji.fromUnicode("✂"));

		private final Emoji emoji;

		Emojis(UnicodeEmoji emoji) {
			this.emoji = emoji;
		}

		public String get() {
			return emoji.getFormatted();
		}
	}
}
