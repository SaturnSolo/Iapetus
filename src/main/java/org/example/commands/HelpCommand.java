package org.example.commands;

import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.components.actionrow.ActionRow;
import net.dv8tion.jda.api.components.buttons.Button;
import net.dv8tion.jda.api.entities.MessageEmbed;
import net.dv8tion.jda.api.events.interaction.command.SlashCommandInteractionEvent;
import net.dv8tion.jda.api.events.interaction.component.ButtonInteractionEvent;
import org.example.ButtonManager;
import org.example.structures.IapetusButton;
import org.example.structures.IapetusCommand;
import org.example.utils.IapetusColor;

public class HelpCommand extends IapetusCommand {
    private final ButtonManager buttonMgr;

    public HelpCommand(ButtonManager buttonMgr) {
        super("help", "gives a bit of help and info");
        this.buttonMgr = buttonMgr;

        buttonMgr.addButtons(
                new IapetusButton(Button.primary("commands", "💚")) {
                    @Override
                    public void run(ButtonInteractionEvent event) {
                        MessageEmbed embed = new EmbedBuilder().setTitle("**`Commands`**").setDescription("""
                                **Need help with commands or wondering what the commands are and what they do? I am here to help you!**\s
                                \s
                                **`Fun commands`**\s
                                **/adventure** takes you on an adventure who knows where it will take you\s
                                **/berries** is a command that will tell you the amount of berries you have\s
                                **/bonk** will let you bonk a user\s
                                **/daily** daily reward\s
                                **/commands** lists all bot commands\s
                                **/shop** opens the shop\s
                                **/inventory** opens your inventory\s
                                **/hatch** hatches a pet out of an egg\s
                                **/lootchest** opens a loot chest!\s
                                **/leaderboard** shows what user in the server has the most berries\s
                                **/use** uses an item\s
                                **/give** give other users berries\s
                                **/pets** gives a pet menu\s
                                **/random** gives a random compliment\s
                                **/ping** pong
                                \s
                                **`Staff only commands`**\s
                                **/ignore-channel** ignores a certain channel for berry drops\s
                                **/list-ignored** lists ignored channels\s
                                **/remove-ignore** removes an ignored channel for berry drops\s
                                """
                        ).setColor(IapetusColor.LILA).build();

                        event.getInteraction().replyEmbeds(embed).queue();
                    }
                }, new IapetusButton(Button.primary("info", "❔")) {
                    @Override
                    public void run(ButtonInteractionEvent event) {
                        MessageEmbed embed = new EmbedBuilder().setTitle("**`More Info`**").setDescription("""
                                **Iapetus is a strawberry economy bot with pet commands, fun commands and a lot more! It is developed by `@saturnsolo`, and `@sylviameows`.\s
                                \s
                                Iapetus is coded using Java and the JDA API. It is 100% Java (according to [github](https://github.com/SaturnSolo/Iapetus)).\s
                                \s
                                Iapetus is based off of the moon Iapetus for it's dusty mossy look.**"""
                        ).setColor(IapetusColor.LILA).build();
                        event.getInteraction().replyEmbeds(embed).queue();
                    }
                }, new IapetusButton(Button.primary("links", "🔗")) {
                    @Override
                    public void run(ButtonInteractionEvent event) {
                        MessageEmbed embed = new EmbedBuilder().setTitle("**`Links`**").setDescription("""
                                **Here you can find the link to our official discord server and documentation! !**\s
                                \s
                                **`Discord`\s
                                 Join our [Discord](https://discord.com/invite/Dte5YBv3ej), here you'll find our latest updates and plans for the future!**\s
                                \s
                                **`Documentation`\s
                                 Here you will find the documentation on Iapetus from commands to our Terms Of Service and Privacy policy on our [Gitbook](https://iapetus-bot-development.gitbook.io/iapetus-bot)**"""
                        ).setColor(IapetusColor.LILA).build();

                        event.getInteraction().replyEmbeds(embed).queue();
                    }
                });
    }

    @Override
    public boolean runCommand(SlashCommandInteractionEvent event) {
        MessageEmbed embed = new EmbedBuilder().setTitle("**Iapetus**").setDescription("""
                **`Basic info`**\s
                **To start your adventures with Iapetus you can type /adventure and /berries to see how many berries you collected. There is also /shop where you can buy items and /inventory to see your items!**\s
                \s
                **`More info`**\s
                **The 💚 will take you to the commands menu, the ❔ will take you to the more info tab, the 🔗 will take you to the links!**"""
        ).setColor(IapetusColor.LILA).setThumbnail("https://media.discordapp.net/attachments/1274910028888932402/1275871665603088465/iapetusnobackground.png?ex=66c77795&is=66c62615&hm=dd21eb064d66624006faed7bbab8435dbb030cdd2fa3847bcbe2557df9100ba8&=&format=webp&quality=lossless").build();
        event.replyEmbeds(embed).addComponents(ActionRow.of(buttonMgr.getButton("commands"), buttonMgr.getButton("info"), buttonMgr.getButton("links"))).queue();
        return true;
    }
}
