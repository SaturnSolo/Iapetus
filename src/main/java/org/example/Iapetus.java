package org.example;

import io.github.cdimascio.dotenv.Dotenv;
import net.dv8tion.jda.api.JDA;
import net.dv8tion.jda.api.JDABuilder;
import net.dv8tion.jda.api.entities.Activity;
import net.dv8tion.jda.api.requests.GatewayIntent;
import org.example.commands.*;
import org.example.commands.give.GiveCommand;
import org.example.commands.pet.HatchCommand;
import org.example.commands.pet.PetMenuCommand;
import org.example.commands.shop.BerriesCommand;
import org.example.commands.shop.InventoryCommand;
import org.example.commands.shop.ShopCommand;
import org.example.events.DropHandler;
import org.example.events.InteractionLogger;
import org.example.events.TextResponses;
import org.example.items.*;
import org.example.items.flowers.CherryBlossomItem;
import org.example.items.flowers.RoseItem;
import org.example.items.flowers.TulipItem;

import java.security.SecureRandom;
import java.util.Random;

public class Iapetus {
	private static Iapetus INSTANCE;

	private final CommandManager commandMgr;
	private final ButtonRouter buttonRouter;
	private final ItemManager itemMgr;
	private final Economy economy;
	private final Random rng;

	private Iapetus() {
		INSTANCE = this;
		Dotenv dotenv = Dotenv.configure().load();
		String token = dotenv.get("TOKEN");

		if (token == null)
			throw new IllegalArgumentException("TOKEN environment variable not found.");

		JDABuilder builder = JDABuilder.createDefault(token);
		builder.setBulkDeleteSplittingEnabled(false);

		rng = new SecureRandom();

		// initialize managers.
		itemMgr = new ItemManager();
		economy = new Economy(itemMgr);
		buttonRouter = new ButtonRouter();
		commandMgr = new CommandManager();

		// Set activity
		builder.setActivity(Activity.listening("🍓"));
		builder.enableIntents(GatewayIntent.GUILD_MESSAGES, GatewayIntent.GUILD_PRESENCES,
				GatewayIntent.GUILD_MESSAGE_REACTIONS, GatewayIntent.MESSAGE_CONTENT, GatewayIntent.GUILD_MEMBERS,
				GatewayIntent.GUILD_PRESENCES);

		// Register items
		RockItem rockItem = new RockItem(rng);
		itemMgr.register(new DiceItem(rng));
		itemMgr.register(new GemItem());
		itemMgr.register(new KeyItem());
		itemMgr.register(new PumpkinItem());
		itemMgr.register(rockItem);
		itemMgr.register(new ShinyItem(itemMgr));
		itemMgr.register(new SkullItem());
		itemMgr.register(new SwordItem());
		itemMgr.register(new EggItem());
		itemMgr.register(new RoseItem());
		itemMgr.register(new TulipItem());
		itemMgr.register(new CherryBlossomItem());

		// Create commands that are also ButtonModules
		ShopCommand shopCommand = new ShopCommand(itemMgr, economy);
		HelpCommand helpCommand = new HelpCommand(commandMgr);
		DropHandler dropHandler = new DropHandler(economy);
		AdventureCommands adventureCommands = new AdventureCommands(itemMgr, economy, rng);

		// Register button modules
		buttonRouter.register("shop", shopCommand);
		buttonRouter.register("help", helpCommand);
		buttonRouter.register("drop", dropHandler);
		buttonRouter.register("rps", rockItem);
		buttonRouter.register("adv", adventureCommands.getAdventureButtons());

		// Register commands
		commandMgr.addCommands(new PingCommand(), new BonkCommand(), new RandomCommand(rng), shopCommand,
				new InventoryCommand(itemMgr), new BerriesCommand(economy), new PetMenuCommand(),
				new HatchCommand(itemMgr, rng), new IgnoredChannels(), helpCommand, new UseItemCommand(itemMgr),
				adventureCommands, new LootChestCommands(itemMgr, economy, rng), new GiveCommand(economy),
				new TopBerriesCommand(economy), new DailyCommand(economy));

		builder.addEventListeners(commandMgr, buttonRouter, dropHandler, new TextResponses(rng),
				new InteractionLogger());
		JDA jda = builder.build();
		commandMgr.register(jda);
	}

	public static void main(String[] args) {
		if (INSTANCE == null)
			INSTANCE = new Iapetus();
	}

}
