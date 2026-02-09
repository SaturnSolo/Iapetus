package org.example.buttons;

import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.components.actionrow.ActionRow;
import net.dv8tion.jda.api.components.buttons.Button;
import net.dv8tion.jda.api.components.buttons.ButtonStyle;
import net.dv8tion.jda.api.entities.MessageEmbed;
import net.dv8tion.jda.api.entities.User;
import net.dv8tion.jda.api.entities.emoji.Emoji;
import net.dv8tion.jda.api.events.interaction.component.ButtonInteractionEvent;
import net.dv8tion.jda.api.utils.messages.MessageEditBuilder;
import org.example.ButtonModule;
import org.example.Economy;
import org.example.ItemManager;
import org.example.database.Database;
import org.example.structures.Inventory;
import org.example.types.ItemId;
import org.example.types.UserId;
import org.example.utils.IapetusColor;

import java.util.*;

public class AdventureButtons implements ButtonModule {
	private final ItemManager itemMgr;
	private final Economy economy;
	private final Random rng;
	private final Map<String, Date> adventureCooldowns;
	private final Map<String, AdventureLocation> locations = new HashMap<>();
	private final Map<String, ChoiceEvent> choiceEvents = new HashMap<>();

	//
	// INITIALIZING THE LOCATIONS.
	//
	public AdventureButtons(ItemManager itemMgr, Economy economy, Random rng, Map<String, Date> advCooldowns) {
		this.itemMgr = itemMgr;
		this.economy = economy;
		this.rng = rng;
		this.adventureCooldowns = advCooldowns;

		addLocations(new AdventureLocation("cave", "%s stumbled upon a cave.",
				"it seems quite deep, it requires you to crawl.", 5)
				.addEvents(new AdventureEvent("The crawl was quite uncomfortable and you didn't find anything.", 2),
						new AdventureEvent("You couldn't fit through the tiny gap.", 1),
						new FindEvent("You crawled deep into the cave and found a neat rock. You pocketed it.",
								ItemId.ROCK, 2),
						new FindEvent(
								"You noticed a sparkle as you crawled through the dark cave. When you got closer you realized it was a gemstone!",
								ItemId.GEM, 1),

						new ChoiceEvent(
								"The path splits in two directions. \nOn the left you see a small glint, on the right darkness.",
								"cave_split", 8)
								.addChoices(new AdventureChoice("Go left", Emoji.fromUnicode("◀"),
										ButtonStyle.SECONDARY).addEvents(
												new FindEvent("You walk up to the glint and its a gem!", ItemId.GEM, 2),
												new DeathEvent(
														"You walk up to the glint and its a bear D:\n You attempt to flee but fail.",
														1),
												new AdventureEvent(
														"You walk up to the glint and its a bear D:\n You attempt to flee and succeed!",
														1)),
										new AdventureChoice("Go right", Emoji.fromUnicode("▶"), ButtonStyle.SECONDARY)
												.addEvents(
														new FindEvent("You pushed forward and found a neat rock.",
																ItemId.ROCK, 1),
														new ChoiceEvent(
																"You pushed forward and found an egg, do you take it?",
																"cave_find_egg", 1)
																.addChoices(new AdventureChoice("Take it!",
																		Emoji.fromUnicode("🥚"))
																		.addEvents(new FindEvent(
																				"You picked up the egg, I wonder whats inside?",
																				ItemId.EGG, 3, 1),
																				new DeathEvent(
																						"Its mother was just around the corner! It attacked you and took some of your items.",
																						2)),
																		new AdventureChoice("Leave it be.",
																				ButtonStyle.SECONDARY)
																				.addEvents(new AdventureEvent(
																						"You left the egg be and left the cave, light at last!",
																						1))))),
						new ChoiceEvent("The corridor is getting a bit tight, do you continue?", "cave_tight", 8)
								.addChoices(new AdventureChoice("Continue", Emoji.fromUnicode("⏩"), ButtonStyle.DANGER)
										.addEvents(new DeathEvent("You got stuck in the corridor!", 2), new DeathEvent(
												"Another person was trying to get through on the other side. \nBoth of you got trapped.",
												1),
												new AdventureEvent(
														"You made it to the other side, but you didn't find anything",
														5),
												new FindEvent(
														"You made it to the other side and found a massive gem cluster!",
														ItemId.GEM, 2, 3)),
										new AdventureChoice("Go back", ButtonStyle.SECONDARY)
												.addEvents(new AdventureEvent("You got out safely.", 1)))),
				new AdventureLocation("throne", "%s walked into a throne room.",
						"its fit for a queen, what riches might you find?", 1)
						.addEvents(new AdventureEvent("You searched around and found nothing", 2), new ChoiceEvent(
								"You found a hidden lever that revealed the queens dice collection. Do you take one?",
								"throne_dice", 1)
								.addChoices(new AdventureChoice("Take it!", Emoji.fromUnicode("🎲")).addEvents(
										new FindEvent(
												"You took the dice from the collection, hopefully no one notices!",
												ItemId.DICE, 2),
										new AdventureEvent(
												"Uh oh! The queen saw you taking and kicked you from her throne room.",
												1)),
										new AdventureChoice("Leave it be.", ButtonStyle.SECONDARY).addEvents(
												new FindEvent(
														"The queen saw you admiring the dice collection and offered you one!",
														ItemId.DICE, 1),
												new AdventureEvent("You left the collection alone.", 2)))),
				new AdventureLocation("forest", "%s wandered into a forest.",
						"the brush is quite thick, maybe something is inside.", 4)
						.addEvents(new StrawberryEvent(
								"You searched around and found some strawberry bushes, you picked up %s berries", 3, 5,
								true),
								new ChoiceEvent("In the wild you found a flower! Do you pick it?", "forest_flower", 3)
										.addChoices(new AdventureChoice("Pick it!").addEvents(new FindEvent(
												"You picked the rose, it hurt a little but your ok!", ItemId.ROSE, 1)),
												new AdventureChoice("Keep searching", ButtonStyle.SECONDARY).addEvents(
														new LocationEvent("forest", 2), new LocationEvent("cave", 4))),
								new AdventureEvent(
										"You kicked some brush around and found more brush, you found nothing.", 1)),
				new AdventureLocation("town", "%s walked into a town.", "there's a lot of places to go!", 3)
						.addEvents(new ChoiceEvent("Looking around the town square you see a couple places you can go.",
								"town_select", 1)
								.addChoices(
										new AdventureChoice("Bakery", Emoji.fromUnicode("🧁"), ButtonStyle.SECONDARY)
												.addEvents(new StrawberryEvent(
														"You went into the bakery and they gave you a berry. Yummy!", 1,
														1)),
										new AdventureChoice("Shop", Emoji.fromUnicode("🛒"), ButtonStyle.SECONDARY)
												.addEvents(new ChoiceEvent("You entered the shop, what will you do?",
														"town_shop", 1)
														.addChoices(new AdventureChoice("Steal", ButtonStyle.DANGER)
																.addEvents(new FindEvent(
																		"You looked around and all you could find was a rusty key. I wonder what it's used for?",
																		ItemId.KEY, 1),
																		new StrawberryEvent(
																				"You took from the register when the clerk wasn't looking!",
																				5, 2),
																		new DeathEvent(
																				"You got caught and the clerk fought you! \nYou lost some of your items.",
																				1),
																		new AdventureEvent(
																				"You got caugt and the clerk kicked you out!.",
																				2)),
																new AdventureChoice("Look around")
																		.addEvents(new AdventureEvent(
																				"You looked around the store, but found nothing of interest.",
																				1)))),
										new AdventureChoice("Hotel", Emoji.fromUnicode("🛌"), ButtonStyle.SECONDARY)
												.addEvents(new LocationEvent("hotel", 1))))

		);

		addLocations(new AdventureLocation("hotel", "Welcome to the Inn, %s!",
				"It's a little dusty but still nice. Have a look around!", 0)
				.addEvents(new ChoiceEvent(
						"You talk to the hotel clerk and are given a room. \nYou head to your room, what do you want to do?",
						"hotel_room", 1)
						.addChoices(new AdventureChoice("Search", Emoji.fromUnicode("🔍")).addEvents(
								new FindEvent("You searched through your room and found a dice!", ItemId.DICE, 1),
								new StrawberryEvent("Someone left a strawberry behind in your room, what a lucky day!",
										1, 1)),
								new AdventureChoice("Sleep", ButtonStyle.SECONDARY)
										.addEvents(new LocationEvent("dream", 1)))),
				new AdventureLocation("dream", "Sweet dreams %s.",
						"This is quite a pleasant dream, I wonder what this magical world has for us!", 0)
						.addEvents(new LocationEvent("nightmare", 1),
								new StrawberryEvent(
										"You dream of strawberries, and when you wake up you magically have more!", 5,
										4, true),
								new AdventureEvent("You dream of strawberries.", 4),
								new AdventureEvent("You've peacefully rested.", 2))
						.setCanLeave(false));
	}

	//
	// MANAGING THE LOCATIONS
	//

	private void addLocations(List<AdventureLocation> locations) {
		locations.forEach(location -> this.locations.put(location.id, location));
	}

	private void addLocations(AdventureLocation... locations) {
		addLocations(Arrays.asList(locations));
	}

	private AdventureLocation getLocation(String location) {
		return locations.get(location);
	}

	private AdventureLocation getRandomLocation() {
		List<AdventureLocation> pool = new ArrayList<>();
		this.locations.values().forEach(location -> {
			for (int i = 0; i < location.odds; ++i)
				pool.add(location);
		});
		return pool.get(rng.nextInt(pool.size()));
	}

	//
	// ADD CHOICES
	//

	private void addChoice(ChoiceEvent choice) {
		choiceEvents.put(choice.id, choice);
	}

	private ChoiceEvent getChoice(String id) {
		return choiceEvents.get(id);
	}

	//
	// COMMONLY USED FUNCTIONS
	//

	private String selectRandom(String... strings) {
		return strings[rng.nextInt(strings.length)];
	}

	//
	// BUTTON DISPATCH
	//

	@Override
	public void onButton(String id, ButtonInteractionEvent event) {
		switch (id) {
			case "confirm" -> handleConfirm(event);
			case "cancel" -> handleCancel(event);
			case "leave" -> handleLeave(event);
			default -> {
				if (id.endsWith("-investigate")) {
					String locationId = id.substring(0, id.indexOf("-investigate"));
					AdventureLocation location = getLocation(locationId);
					if (location != null)
						location.investigate(event);
				} else {
					// Choice button: id is "<choiceId>-<index>"
					int dash = id.lastIndexOf('-');
					if (dash > 0) {
						String choiceId = id.substring(0, dash);
						int index = Integer.parseInt(id.substring(dash + 1));
						ChoiceEvent choice = getChoice(choiceId);
						if (choice != null)
							choice.run(index, event);
					}
				}
			}
		}
	}

	private void handleConfirm(ButtonInteractionEvent event) {
		AdventureLocation location = getRandomLocation();
		MessageEmbed embed = location.generateEmbed(event.getUser());

		event.editMessage(new MessageEditBuilder().setEmbeds(embed)
				.setComponents(ActionRow.of(
						Button.primary("adv:%s-investigate".formatted(location.id), "Investigate")
								.withEmoji(Emoji.fromUnicode("🔍")),
						Button.secondary("adv:leave", Emoji.fromUnicode("💨")).withDisabled(!location.canLeave)))
				.setReplace(true).build()).queue();
	}

	private void handleCancel(ButtonInteractionEvent event) {
		User user = event.getUser();
		MessageEmbed embed = new EmbedBuilder().setThumbnail(user.getAvatarUrl()).setColor(IapetusColor.DARK_GREEN)
				.setTitle(user.getEffectiveName() + "** isn't ready**")
				.setDescription(
						selectRandom("Adventure isn't always for everybody it's okay", "Adventure when you are ready",
								"There is no rush to rush out into the adventure relax while you can",
								"Not ready for adventure yet? That's fine!"))
				.build();

		adventureCooldowns.put(user.getId(), new Date());
		event.editMessage(new MessageEditBuilder().setEmbeds(embed).setReplace(true).build()).queue();
	}

	private void handleLeave(ButtonInteractionEvent event) {
		User user = event.getUser();
		MessageEmbed embed = new EmbedBuilder().setThumbnail(user.getAvatarUrl()).setColor(IapetusColor.DARK_GREEN)
				.setTitle(user.getEffectiveName() + " left the area.")
				.setDescription(selectRandom("You left the area, who knows what adventures awaited you.",
						"Hopefully you didn't forget anything.", "Did you have a safe journey?"))
				.build();

		event.editMessage(new MessageEditBuilder().setEmbeds(embed).setReplace(true).build()).queue();
	}

	//
	// LOCATION CLASS
	//

	private class AdventureLocation {
		private final String id;
		private final String name;
		private final String description;
		private final int odds;
		private boolean canLeave = true;

		List<AdventureEvent> events;

		public AdventureLocation(String id, String name, String description, int odds) {
			this.id = id;
			this.name = name;
			this.description = description;
			this.odds = odds;

			this.events = new ArrayList<>();
		}

		public void investigate(ButtonInteractionEvent event) {
			int index = rng.nextInt(events.size());
			events.get(index).run(event);
		}

		public MessageEmbed generateEmbed(User user) {
			return new EmbedBuilder().setTitle(name.replaceAll("%s", user.getEffectiveName()))
					.setDescription(description).setFooter("Investigate 🔍 or Leave 💨")
					.setColor(IapetusColor.DARK_GREEN).build();
		}

		public AdventureLocation addEvents(List<AdventureEvent> events) {
			events.forEach(event -> {
				for (int i = 0; i < event.odds; ++i)
					this.events.add(event);
			});
			return this;
		}

		public AdventureLocation addEvents(AdventureEvent... events) {
			return addEvents(Arrays.asList(events));
		}

		public AdventureLocation setCanLeave(boolean canLeave) {
			this.canLeave = canLeave;
			return this;
		}
	}

	//
	// EVENTS
	// TODO: puzzle event, location (switch) event
	//

	private static class AdventureEvent {
		protected final String message;
		protected final int odds;

		public AdventureEvent(String message, int odds) {
			this.message = message;
			this.odds = odds;
		}

		public void run(ButtonInteractionEvent event) {
			MessageEmbed embed = new EmbedBuilder(event.getMessage().getEmbeds().get(0)).setDescription(message)
					.setFooter(null).build();
			event.editMessage(new MessageEditBuilder().setEmbeds(embed).setReplace(true).build()).queue();
		}
	}

	private class FindEvent extends AdventureEvent {
		private final int amount;
		private final ItemId itemId;

		public FindEvent(String message, ItemId itemId, int odds) {
			this(message, itemId, odds, 1);
		}

		public FindEvent(String message, ItemId itemId, int odds, int amount) {
			super(message, odds);
			this.itemId = itemId;
			this.amount = amount;
		}

		@Override
		public void run(ButtonInteractionEvent event) {
			if (itemId != null)
				itemMgr.giveItem(UserId.of(event.getUser()), itemId, amount);
			MessageEmbed embed = new EmbedBuilder(event.getMessage().getEmbeds().get(0)).setDescription(message)
					.setFooter("+%d %s".formatted(amount, itemMgr.getItem(itemId).getString(true))).build();
			event.editMessage(new MessageEditBuilder().setEmbeds(embed).setReplace(true).build()).queue();
		}
	}

	private class StrawberryEvent extends AdventureEvent {
		private final boolean isRandom;
		private final int amount;

		public StrawberryEvent(String message, int amount, int odds) {
			super(message, odds);
			this.amount = amount;
			this.isRandom = false;
		}

		public StrawberryEvent(String message, int amount, int odds, boolean random) {
			super(message, odds);
			this.amount = amount;
			this.isRandom = random;
		}

		@Override
		public void run(ButtonInteractionEvent event) {
			int found = amount;
			if (isRandom)
				found = rng.nextInt(amount) + 1;

			economy.reward(UserId.of(event.getUser()), found);
			MessageEmbed embed = new EmbedBuilder(event.getMessage().getEmbeds().get(0))
					.setDescription(message.replaceAll("%s", "" + found))
					.setFooter("+%d Strawberries 🍓".formatted(found)).build();
			event.editMessage(new MessageEditBuilder().setEmbeds(embed).setReplace(true).build()).queue();
		}
	}

	private class DeathEvent extends AdventureEvent {
		public DeathEvent(String message, int odds) {
			super(message, odds);
		}

		@Override
		public void run(ButtonInteractionEvent event) {
			User user = event.getUser();
			UserId userId = UserId.of(user);
			Inventory inventory = Database.getUserInventory(userId, itemMgr);

			String footer = null;
			if (inventory.totalCount() > 2) {
				List<ItemId> pool = new ArrayList<>();
				inventory.items().forEach((id, count) -> {
					for (int i = 0; i < count; i++)
						pool.add(id);
				});

				int itemsToTake = (int) Math.floor(Math.sqrt(pool.size()) - 0.4);
				Map<ItemId, Integer> taken = new EnumMap<>(ItemId.class);
				for (int i = 0; i < itemsToTake && !pool.isEmpty(); i++) {
					ItemId picked = pool.remove(rng.nextInt(pool.size()));
					itemMgr.takeItem(userId, picked);
					taken.merge(picked, 1, Integer::sum);
				}

				StringBuilder builder = new StringBuilder();
				taken.forEach((id, count) -> builder.append("-").append(count).append(" ")
						.append(itemMgr.getItem(id).getString(true)).append(" "));
				footer = builder.toString();
			}

			MessageEmbed embed = new EmbedBuilder(event.getMessage().getEmbeds().get(0)).setDescription(message)
					.setFooter(footer).setColor(IapetusColor.RED).build();
			event.editMessage(new MessageEditBuilder().setEmbeds(embed).setReplace(true).build()).queue();
		}
	}

	private class LocationEvent extends AdventureEvent {
		private final String locationId;

		public LocationEvent(String locationId, int odds) {
			super(null, odds);
			this.locationId = locationId;
		}

		@Override
		public void run(ButtonInteractionEvent event) {
			AdventureLocation location = getLocation(locationId);
			MessageEmbed embed = location.generateEmbed(event.getUser());

			event.editMessage(new MessageEditBuilder().setEmbeds(embed)
					.setComponents(ActionRow.of(
							Button.primary("adv:%s-investigate".formatted(location.id), "Investigate")
									.withEmoji(Emoji.fromUnicode("🔍")),
							Button.secondary("adv:leave", Emoji.fromUnicode("💨")).withDisabled(!location.canLeave)))
					.setReplace(true).build()).queue();
		}
	}

	private class ChoiceEvent extends AdventureEvent {
		private final String id;
		private final List<AdventureChoice> choices;

		public ChoiceEvent(String message, String id, int odds) {
			super(message, odds);
			this.id = id;
			this.choices = new ArrayList<>();
			addChoice(this);
		}

		public ChoiceEvent addChoices(List<AdventureChoice> choices) {
			this.choices.addAll(choices);
			return this;
		}

		public ChoiceEvent addChoices(AdventureChoice... choices) {
			return addChoices(Arrays.asList(choices));
		}

		@Override
		public void run(ButtonInteractionEvent event) {
			Collection<Button> buttons = new ArrayList<>();
			for (int i = 0; i < choices.size(); i++) {
				if (i > 4)
					break;
				AdventureChoice choice = choices.get(i);
				Button button = choice.toButton(id, i);
				buttons.add(button);
			}

			MessageEmbed embed = new EmbedBuilder(event.getMessage().getEmbeds().getFirst()).setDescription(message)
					.build();
			event.editMessage(new MessageEditBuilder().setEmbeds(embed).setComponents(ActionRow.of(buttons))
					.setReplace(true).build()).queue();
		}

		public void run(int index, ButtonInteractionEvent event) {
			AdventureChoice choice = choices.get(index);
			choice.select(event);
		}
	}

	private class AdventureChoice {
		private final List<AdventureEvent> events;

		private final String label;
		private final Emoji emoji;
		private final ButtonStyle style;

		public AdventureChoice(String label, Emoji emoji) {
			this(label, emoji, ButtonStyle.PRIMARY);
		}

		public AdventureChoice(String label, Emoji emoji, ButtonStyle style) {
			this.label = label;
			this.emoji = emoji;
			this.style = style;
			this.events = new ArrayList<>();
		}

		public AdventureChoice(String label) {
			this(label, ButtonStyle.PRIMARY);
		}

		public AdventureChoice(String label, ButtonStyle style) {
			this.label = label;
			this.style = style;
			this.emoji = null;
			this.events = new ArrayList<>();
		}

		public void select(ButtonInteractionEvent event) {
			int index = rng.nextInt(events.size());
			events.get(index).run(event);
		}

		public Button toButton(String choiceId, int index) {
			String buttonId = "adv:%s-%d".formatted(choiceId, index);
			Button button = Button.primary(buttonId, label).withStyle(style);
			if (emoji != null)
				button = button.withEmoji(emoji);
			return button;
		}

		public AdventureChoice addEvents(List<AdventureEvent> events) {
			events.forEach(event -> {
				for (int i = 0; i < event.odds; ++i)
					this.events.add(event);
			});
			return this;
		}

		public AdventureChoice addEvents(AdventureEvent... events) {
			return addEvents(Arrays.asList(events));
		}
	}
}
