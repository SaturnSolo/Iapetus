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
import org.example.commands.channel.IgnoreChannelCommand;
import org.example.commands.channel.ListIgnoredCommand;
import org.example.commands.channel.RemoveIgnoreCommand;
import org.example.commands.pet.HatchCommand;
import org.example.commands.pet.PetMenuCommand;
import org.example.commands.shop.BerriesCommand;
import org.example.commands.shop.InventoryCommand;
import org.example.commands.shop.ShopCommand;
import org.example.events.DropHandler;
import org.example.events.TextResponses;


public class Main {
    public static CommandManager commandManager;
    public static ButtonManager buttonManager;
    public static ItemManager itemManager;

    public static void main(String[] args) {

        Dotenv dotenv = Dotenv.configure().load();
        String token = dotenv.get("TOKEN");

        if (token == null) {
            throw new IllegalArgumentException("TOKEN environment variable not found.");
        }

        JDABuilder builder = JDABuilder.createDefault(token);



        builder.setBulkDeleteSplittingEnabled(false);
        // Set activity (like "playing Something")
        builder.setActivity(Activity.listening("🍓"));
        builder.enableIntents(GatewayIntent.GUILD_MESSAGES, GatewayIntent.GUILD_PRESENCES, GatewayIntent.GUILD_MESSAGE_REACTIONS, GatewayIntent.MESSAGE_CONTENT, GatewayIntent.GUILD_MEMBERS, GatewayIntent.GUILD_EMOJIS_AND_STICKERS, GatewayIntent.GUILD_PRESENCES);

        Main.itemManager = new ItemManager();

        // initialize interaction managers.
        ButtonManager bm = new ButtonManager();
        Main.buttonManager = bm;
        bm.addButtons(
          new StrawberryButton()
        );

        CommandManager cm = new CommandManager();
        Main.commandManager = cm;
        cm.addCommands(
          new PingCommand(),
          new BonkCommand(),
          new RandomCommand(),
          new ShopCommand(),
          new InventoryCommand(),
          new BerriesCommand(),
          new PetMenuCommand(),
          new HatchCommand(),
          new IgnoreChannelCommand(),
          new RemoveIgnoreCommand(),
          new ListIgnoredCommand(),
          new HelpCommand(),
          new UseItemCommand()
        );

        builder.addEventListeners(cm, bm, new DropHandler(), new TextResponses());
        JDA jda = builder.build();
        cm.register(jda);
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