package org.example.events;

import net.dv8tion.jda.api.events.message.MessageReceivedEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;

import java.util.*;

public class TextResponses extends ListenerAdapter {
	private final static List<String> BLUEBERRY = List.of(
			"Blueberries ranked number one in antioxidant health benefits in a comparison with more than 40 fresh fruits and vegetables.",
			"A single blueberry bush can produce as many as 6,000 blueberries per year.",
			"If a blueberry plant is well taken care of, it can live 70 years or longer,",
			"Most blueberry bushes, however, have a life span of 30 to 50 years. A single healthy blueberry plant can average more than 2,000 berries per season, meaning it can yield more than 100,000 berries over the course of its life.");
	private final static List<String> CHERRY = List.of("Cherries are often sweet but tart!",
			"Under ideal conditions, a cherry tree can produce fruit for over 100 years.",
			"The country of Turkey is by far the largest cherry producer followed by the U.S.",
			"Despite the name, cherry trees don’t always produce cherries. Most varieties are ornamental, not fruit-bearing, trees.",
			"Sweet cherries are mostly grown in California, Washington and Oregon, and tart cherries are mostly grown in Michigan and Wisconsin. The most common type of sweet cherry is the Bing, followed by Lambert and gold-toned Rainier. Sweet cherries are usually eaten fresh, as a fun snack. The most common type of tart cherry is the Montmorency.",
			"Today, Michigan is the big producer in the cherry-growing business, with more than 30,000 acres of cherry trees. In the Traverse City region alone, a whopping 4 million trees produce 150 to 200 million pounds of tart cherries annually. Traverse City’s annual National Cherry Festival started in the 1920s as an informal “blessing of the blossoms” ceremony. Now it’s a weeklong festival attended by people from all over the world.",
			"It’s no coincidence that Michigan and Wisconsin produce so many cherries. The land off Lake Michigan is ideal for cherry trees. Weather conditions prevent early frost, and the light wind helps pollinate the trees. Alkaline soil and shallow limestone deposits are also beneficial.");
	private static final List<String> STRAWBERRY = List.of("Look a strawberry!", "Strawberries are my favorite!",
			"Strawberries are grown in every state in the US but California leads the pack—10 million baskets of strawberries are shipped daily during harvest time.",
			"Strawberries are related to roses", "Strawberries are the first fruit to ripen in spring.",
			"A strawberry :D",
			"Ancient records reveal that the strawberry was grown in Rome dating back to 200 BC. They used the berries to treat depression, fever, and sore throats.");
	private static final List<String> MOSS = List.of("I love moss!",
			"Mosses are small, non-vascular flowerless plants in the taxonomic division Bryophyta sensu stricto. Bryophyta may also refer to the parent group bryophytes, which comprise liverworts, mosses, and hornworts. Mosses typically form dense green clumps or mats, often in damp or shady locations.",
			"Moss dates back to 450 million years ago, and have survived and thrived through a range of drastic climate changes.",
			"Unlike most other plants, mosses don’t have roots. Instead they have rhizoids, which are small hairlike structures. Their main function is anchoring the plant to rock, bark or soil.",
			"Mosses grow in many different environments, from cold snowy mountains to baking hot deserts.",
			"Mosses function like sponges, using their capillary spaces to hang on to water.",
			"Mosses can impact the temperature of the soil, both warming it up and cooling it down depending on the environment.");
	private static final List<String> IAPETUS = List.of("Hello if you are needing help type /help or /commands!",
			"Hello I am Iapetus the moss moon! Type /help or /commands to get started!",
			"Hello I am Iapetus I am coded using Java! Type /help or /commands to get started!");

	private final Random rng;
	public TextResponses(Random rng) {
		this.rng = rng;
	}

	@Override
	public void onMessageReceived(MessageReceivedEvent event) {
		if (event.getAuthor().isBot() || event.isWebhookMessage())
			return;
		String content = event.getMessage().getContentDisplay().toLowerCase();

		StringBuilder output = new StringBuilder();
		switch (content) {
			case String s when s.equals("ping") -> output.append("Pong! 🌸").append("\n");
			case String s when s.contains("🍓") ->
				output.append(STRAWBERRY.get(rng.nextInt(STRAWBERRY.size()))).append("\n");
			case String s when s.contains("🍒") -> output.append(CHERRY.get(rng.nextInt(CHERRY.size()))).append("\n");
			case String s when s.contains("\uD83E\uDED0") ->
				output.append(BLUEBERRY.get(rng.nextInt(BLUEBERRY.size()))).append("\n");
			case String s when s.contains("moss") -> output.append(MOSS.get(rng.nextInt(MOSS.size()))).append("\n");
			case String s when (s.equals("iapetus")
					|| s.contains("<@%s>".formatted(event.getJDA().getSelfUser().getId()))) ->
				output.append(IAPETUS.get(rng.nextInt(IAPETUS.size()))).append("\n");
			default -> {
				return;
			}
		}

		event.getChannel().sendMessage(output.toString().strip()).queue();
	}
}
