package org.example;


import io.github.cdimascio.dotenv.Dotenv;
import net.dv8tion.jda.api.JDA;
import net.dv8tion.jda.api.JDABuilder;
import net.dv8tion.jda.api.entities.Activity;
import net.dv8tion.jda.api.interactions.commands.OptionType;
import net.dv8tion.jda.api.interactions.commands.build.Commands;
import net.dv8tion.jda.api.requests.GatewayIntent;
import net.dv8tion.jda.api.utils.ChunkingFilter;
import net.dv8tion.jda.api.utils.MemberCachePolicy;
import net.dv8tion.jda.api.utils.cache.CacheFlag;
import org.example.commands.*;


public class Main {
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

        // Add your event listeners here
        builder.addEventListeners(new CommandHandler());
        builder.addEventListeners(new IgnoredChannels());
        builder.addEventListeners(new ShopCommands());
        builder.addEventListeners(new FunCommands());
        builder.addEventListeners(new InventoryCommand());
        builder.addEventListeners(new PetCommands());
        builder.addEventListeners(new PetMenu());
        builder.addEventListeners(new HelpCommand());

        JDA jda = builder.build();

        jda.updateCommands().addCommands(
               // Commands.slash("berries", "shows the amount of berries you have"),
               // Commands.slash("bonk", "bonk someone"),
                Commands.slash("ignore-channel", "ignore channels")
                        .addOption(OptionType.CHANNEL, "channel-1", "choose channels for to ignore"),
                Commands.slash("remove-ignore", "remove from ignored channels")
                        .addOption(OptionType.CHANNEL, "channel-1", "choose channels for to remove from ignored"),
                Commands.slash("shop", "opens the shop"),
                Commands.slash("berries", "the amount of berries you have"),
                Commands.slash("inventory", "opens your inventory"),
                Commands.slash("random", "sends something random"),
                Commands.slash("bonk", "bonk someone")
                        .addOption(OptionType.MENTIONABLE, "name", "mention someone to bonk"),
                Commands.slash("hatch", "hatches a pet out of an egg"),
                Commands.slash("pets", "shows a list of your pets"),
                Commands.slash("help", "gives a bit of help and info"),
                Commands.slash("ignored", "shows ignored channels")
        ).queue();
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