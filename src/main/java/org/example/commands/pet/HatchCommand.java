package org.example.commands.pet;

import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.entities.MessageEmbed;
import net.dv8tion.jda.api.entities.User;
import net.dv8tion.jda.api.events.interaction.command.SlashCommandInteractionEvent;
import org.example.ItemManager;
import org.example.database.Database;
import org.example.structures.IapetusCommand;
import org.example.types.ItemId;
import org.example.types.UserId;
import org.example.utils.IapetusColor;

import java.util.Random;

public class HatchCommand extends IapetusCommand {
	private final ItemManager itemMgr;
	private final Random rng;

	public HatchCommand(ItemManager itemMgr, Random rng) {
		super("hatch", "hatches a pet out of an egg");
		this.itemMgr = itemMgr;
		this.rng = rng;
	}

	@Override
	public boolean runCommand(SlashCommandInteractionEvent event) {
		User user = event.getUser();
		UserId userId = UserId.of(user);

		if (!itemMgr.hasItem(userId, ItemId.EGG)) {
			event.reply("You don't have an egg to hatch.").queue();
			return true;
		}

		// If the user has an egg, hatch it into a random pet
		String hatchedPet = hatchRandomPet();

		// Log the hatching in the database
		Database.logHatchInDatabase(userId.value(), hatchedPet);

		// Remove the egg from the inventory
		itemMgr.takeItem(userId, ItemId.EGG);

		MessageEmbed embed = new EmbedBuilder().setTitle("Hatching a Pet")
				.setDescription("You hatched a new pet: %s".formatted(hatchedPet)).setColor(IapetusColor.RED).build();

		event.replyEmbeds(embed).queue();
		return true;
	}

	private String hatchRandomPet() {
		String[] possiblePets = {"🐢", "🦃", "🐈", "🐕", "🐑", "🦌", "🐂", "🐄", "🦎", "🐍", "🐣", "🦐", "🦩", "🐌",
				"🦢", "🐊", "🦙", "🕷", "🦨", "🦋", "🐸", "🦑", "🦐", "🦞", "🐖", "🦝", "🐙", "🦦", "🐚", "🦌", "🦔",
				"🌻"};
		return possiblePets[rng.nextInt(possiblePets.length)];
	}
}
