package org.example;


import io.github.cdimascio.dotenv.Dotenv;
import net.dv8tion.jda.api.JDA;
import net.dv8tion.jda.api.JDABuilder;
import net.dv8tion.jda.api.entities.Activity;
import net.dv8tion.jda.api.requests.GatewayIntent;
import net.dv8tion.jda.api.utils.ChunkingFilter;
import net.dv8tion.jda.api.utils.MemberCachePolicy;
import net.dv8tion.jda.api.utils.cache.CacheFlag;
import org.example.buttons.StrawberryButton;
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
import org.example.items.PumpkinItem;

import java.security.SecureRandom;
import java.util.Random;


public class Iapetus {
    private static Iapetus INSTANCE;

    private final CommandManager commandMgr;
    private final ButtonManager buttonMgr;
    private final ItemManager itemMgr;
    private final Random rng;

    private Iapetus() {
        INSTANCE = this;
        Dotenv dotenv = Dotenv.configure().load();
        String token = dotenv.get("TOKEN");

        if (token == null) throw new IllegalArgumentException("TOKEN environment variable not found.");

        JDABuilder builder = JDABuilder.createDefault(token);
        builder.setBulkDeleteSplittingEnabled(false);

        rng = new SecureRandom();

        // initialize interaction managers.
        itemMgr = new ItemManager();
        buttonMgr = new ButtonManager();
        commandMgr = new CommandManager();

        // Initialize Item
        Item.init(itemMgr);

        // Set activity (like "playing Something")
        builder.setActivity(Activity.listening("🍓"));
        builder.enableIntents(GatewayIntent.GUILD_MESSAGES, GatewayIntent.GUILD_PRESENCES, GatewayIntent.GUILD_MESSAGE_REACTIONS, GatewayIntent.MESSAGE_CONTENT, GatewayIntent.GUILD_MEMBERS, GatewayIntent.GUILD_PRESENCES);

        new DiceItem(rng);
        new GemItem();
        new KeyItem();
        new PumpkinItem();
        new RockItem(buttonMgr, rng);
        new ShinyItem(itemMgr);

        // Add stuff to the respective managers
        buttonMgr.addButtons(
                new StrawberryButton()
        );
        commandMgr.addCommands(
                new PingCommand(), new BonkCommand(), new RandomCommand(rng), new ShopCommand(buttonMgr), new InventoryCommand(), new BerriesCommand(), new PetMenuCommand(), new HatchCommand(itemMgr, rng), new IgnoredChannels(), new HelpCommand(buttonMgr), new UseItemCommand(itemMgr), new AdventureCommands(buttonMgr, itemMgr, rng), new LootChestCommands(itemMgr, rng), new GiveCommand(), new ComCommands(), new TopBerriesCommand(), new DailyCommand()
        );

        builder.addEventListeners(commandMgr, buttonMgr, new DropHandler(), new TextResponses(rng), new InteractionLogger());
        JDA jda = builder.build();
        commandMgr.register(jda);
    }

    public static void main(String[] args) {
        if (INSTANCE == null) INSTANCE = new Iapetus();
    }


    public void configureMemoryUsage(JDABuilder builder) {
        // Disable cache for member activities (streaming/games/spotify)
        builder.disableCache(CacheFlag.ACTIVITY);

        // Only cache members who are either in a voice channel or owner of the guild
        builder.setMemberCachePolicy(MemberCachePolicy.ALL.or(MemberCachePolicy.OWNER));

        // Disable member chunking on startup
        builder.setChunkingFilter(ChunkingFilter.NONE);

        // Consider guilds with more than 50 members as "large".
        // Large guilds will only provide online members in their setup and thus reduce bandwidth if chunking is disabled.
        builder.setLargeThreshold(50);
    }
}