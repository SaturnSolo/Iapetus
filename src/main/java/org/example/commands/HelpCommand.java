package org.example.commands;

import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.entities.MessageEmbed;
import net.dv8tion.jda.api.events.interaction.command.SlashCommandInteractionEvent;
import net.dv8tion.jda.api.events.interaction.component.ButtonInteractionEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;
import net.dv8tion.jda.api.interactions.components.buttons.Button;
import net.dv8tion.jda.api.requests.restaction.interactions.ReplyCallbackAction;
import org.example.ButtonManager;
import org.example.Main;
import org.example.database.SQLiteDataSource;
import org.example.structures.IapetusButton;
import org.example.structures.IapetusCommand;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;

public class HelpCommand extends IapetusCommand {
    ButtonManager bm = Main.buttonManager;

    public HelpCommand() {
        super("help", "gives a bit of help and info");

        bm.addButtons(
                new IapetusButton(Button.primary("commands", "💚")) {
                    @Override
                    public void run(ButtonInteractionEvent event) {
                        EmbedBuilder embedBuilder2 = new EmbedBuilder();
                        embedBuilder2.setTitle("**`Commands`**");
                        embedBuilder2.setDescription("**Need help with commands or wondering what the commands are and what they do? I am here to help you!** \n \n" + "**`Fun commands`** \n" +
                                "**/adventure** takes you on an adventure who knows where it will take you \n" +
                                "**/berries** is a command that will tell you the amount of berries you have \n" +
                                "**/bonk** will let you bonk a user \n" +
                                "**/commands** lists all bot commands \n" +
                                "**/shop** opens the shop \n" +
                                "**/inventory** opens your inventory \n" +
                                "**/hatch** hatches a pet out of an egg \n" +
                                "**/lootchest** opens a loot chest! \n" +
                                "**/use** uses an item \n" +
                                "**/give** give other users berries \n" +
                                "**/pets** gives a pet menu \n" +
                                "**/random** gives a random compliment \n" +
                                "**/ping** pong\n \n" +
                                "**`Staff only commands`** \n" +
                                "**/ignore-channel** ignores a certain channel for berry drops \n" +
                                "**/list-ignored** lists ignored channels \n" +
                                "**/remove-ignore** removes an ignored channel for berry drops \n");

                        embedBuilder2.setColor(0xD4C2FC);
                        MessageEmbed embed2 = embedBuilder2.build();
                        event.getInteraction().replyEmbeds(embed2).queue();
                    }
                },
                new IapetusButton(Button.primary("info", "❔")) {
                    @Override
                    public void run(ButtonInteractionEvent event) {
                        EmbedBuilder embedBuilder3 = new EmbedBuilder();
                        embedBuilder3.setTitle("**`More Info`**");
                        embedBuilder3.setDescription("**Iapetus is a strawberry economy bot with pet commands, fun commands and a lot more! It is developed by `@saturnsolo`, and `@msvae`. \n \n Iapetus is coded using Java and the jda api. It is 100% java (according to [github](https://github.com/SaturnSolo/Iapetus)). \n \n Iapetus is based off of the moon Iapetus for it's dusty mossy look.**");
                        embedBuilder3.setColor(0xD4C2FC);
                        MessageEmbed embed3 = embedBuilder3.build();
                        event.getInteraction().replyEmbeds(embed3).queue();
                    }
                },
                new IapetusButton(Button.primary("links", "🔗")) {
                    @Override
                    public void run(ButtonInteractionEvent event) {
                        EmbedBuilder embedBuilder4 = new EmbedBuilder();
                        embedBuilder4.setTitle("**`Links`**");
                        embedBuilder4.setDescription("**Here you can find the link to our offical discord server and documentation! !** \n \n" +
                                "**`Discord` \n Join our [Discord](https://discord.com/invite/Dte5YBv3ej), here you'll find our latest updates and plans for the future!** \n \n" +
                                "**`Documentation` \n Here you will find the documentation on Iapetus from commands to our Terms Of Service and Privacy policy on our [Gitbook](https://iapetus-bot-development.gitbook.io/iapetus-bot)**");
                        embedBuilder4.setColor(0xD4C2FC);
                        MessageEmbed embed4 = embedBuilder4.build();
                        event.getInteraction().replyEmbeds(embed4).queue();
                    }
                });
    }

    @Override
    public boolean runCommand(SlashCommandInteractionEvent event) {
        EmbedBuilder embedBuilder1 = new EmbedBuilder();
        embedBuilder1.setTitle("**Iapetus**");
        embedBuilder1.setDescription("**`Basic info`** \n" +
                "**To start your adventures with Iapetus you can type /adventure and /berries to see how many berries you collected. There is also /shop where you can buy items and /inventory to see your items!** \n \n" +
                "**`More info`** \n" +
                "**The 💚 will take you to the commands menu, the ❔ will take you to the more info tab, the 🔗 will take you to the links!**");
        embedBuilder1.setColor(0xD4C2FC);
        embedBuilder1.setThumbnail("https://media.discordapp.net/attachments/1274910028888932402/1275871665603088465/iapetusnobackground.png?ex=66c77795&is=66c62615&hm=dd21eb064d66624006faed7bbab8435dbb030cdd2fa3847bcbe2557df9100ba8&=&format=webp&quality=lossless");
        MessageEmbed embed1 = embedBuilder1.build();
        event.replyEmbeds(embed1).addActionRow(bm.getButton("commands"), bm.getButton("info"), bm.getButton("links")).queue();
        logUserGuild(event.getUser().getId(), event.getGuild().getId());
        return true;
    }

    private void logUserGuild(String userId, String guildId) {
        String query = "INSERT OR IGNORE INTO user_guilds (user_id, guild_id) VALUES (?, ?)";

        try (Connection connection = SQLiteDataSource.getConnection();
             PreparedStatement ps = connection.prepareStatement(query)) {
            ps.setString(1, userId);
            ps.setString(2, guildId);
            ps.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}