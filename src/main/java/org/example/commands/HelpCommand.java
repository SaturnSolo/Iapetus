package org.example.commands;

import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.components.actionrow.ActionRow;
import net.dv8tion.jda.api.components.buttons.Button;
import net.dv8tion.jda.api.entities.MessageEmbed;
import net.dv8tion.jda.api.events.interaction.command.SlashCommandInteractionEvent;
import net.dv8tion.jda.api.events.interaction.component.ButtonInteractionEvent;
import org.example.ButtonManager;
import org.example.CommandManager;
import org.example.structures.IapetusButton;
import org.example.structures.IapetusCommand;
import org.example.utils.IapetusColor;

import java.util.HashMap;
import java.util.Map;

public class HelpCommand extends IapetusCommand {
	private static final MessageEmbed IAPETUS_MAIN_EMBED = new EmbedBuilder().setTitle("**Iapetus**").setDescription(
			"""
					**`Basic info`**\s
					**To start your adventures with Iapetus you can type /adventure and /berries to see how many berries you collected. There is also /shop where you can buy items and /inventory to see your items!**\s
					\s
					**`More info`**\s
					**The 💚 will take you to the commands menu, the ❔ will take you to the more info tab, the 🔗 will take you to the links!**""")
			.setColor(IapetusColor.LILA).build();

	private static final MessageEmbed MORE_INFO_EMBED = new EmbedBuilder().setTitle("**`More Info`**")
			.setDescription(
					"""
							**Iapetus is a strawberry economy bot with pet commands, fun commands and a lot more! It is developed by `@saturnsolo`, and `@sylviameows`.\s
							\s
							Iapetus is coded using Java and the JDA API. It is 100% Java (according to [github](https://github.com/SaturnSolo/Iapetus)).\s
							\s
							Iapetus is based off of the moon Iapetus for it's dusty mossy look.**""")
			.setColor(IapetusColor.LILA).build();

	private static final MessageEmbed LINKS_EMBED = new EmbedBuilder().setTitle("**`Links`**").setDescription(
			"""
					**Here you can find the link to our official discord server and documentation! !**\s
					\s
					**`Discord`\s
					 Join our [Discord](https://discord.com/invite/Dte5YBv3ej), here you'll find our latest updates and plans for the future!**\s
					\s
					**`Documentation`\s
					 Here you will find the documentation on Iapetus from commands to our Terms Of Service and Privacy policy on our [Gitbook](https://iapetus-bot-development.gitbook.io/iapetus-bot)**""")
			.setColor(IapetusColor.LILA).build();

	private final ButtonManager buttonMgr;

	public HelpCommand(ButtonManager buttonMgr, CommandManager commandMgr) {
		super("help", "gives a bit of help and info");
		this.buttonMgr = buttonMgr;
		registerButtons(commandMgr);
	}

	private void registerButtons(CommandManager commandMgr) {
		buttonMgr.addButtons(new IapetusButton(Button.primary("links", "🔗")) {
			@Override
			public void run(ButtonInteractionEvent event) {
				event.replyEmbeds(LINKS_EMBED).queue();
			}
		}, new IapetusButton(Button.primary("info", "❔")) {
			@Override
			public void run(ButtonInteractionEvent event) {
				event.replyEmbeds(MORE_INFO_EMBED).queue();
			}
		}, new IapetusButton(Button.primary("commands", "💚")) {
			@Override
			public void run(ButtonInteractionEvent event) {
				Map<String, String> funCommands = new HashMap<>();
				Map<String, String> adminCommands = new HashMap<>();

				commandMgr.getCommands().forEach((cmdName, cmd) -> {
					Long rawPerms = cmd.getSlash().getDefaultPermissions().getPermissionsRaw();
					System.out.printf("Required perms for %s: %d", cmdName, rawPerms);
					if (rawPerms == null)
						funCommands.put(cmdName, cmd.getDescription());
					else
						adminCommands.put(cmdName, cmd.getDescription());
				});

				StringBuilder commands = new StringBuilder();
				commands.append(
						"**Need help with commands or wondering what the commands are and what they do? I am here to help you!**\n\n");

				commands.append("**`Fun commands`**\n");
				funCommands.forEach((name, description) -> {
					commands.append("**/%s** - %s\n".formatted(name, description));
				});

				commands.append("\n");
				commands.append("**`Staff only commands`**\n");
				adminCommands.forEach((name, description) -> {
					commands.append("**/%s** - %s\n".formatted(name, description));
				});

				MessageEmbed embed = new EmbedBuilder().setTitle("**`Commands`**").setDescription(commands)
						.setColor(IapetusColor.LILA).build();

				event.replyEmbeds(embed).queue();
			}
		});
	}

	@Override
	public boolean runCommand(SlashCommandInteractionEvent event) {
		event.replyEmbeds(IAPETUS_MAIN_EMBED).addComponents(ActionRow.of(buttonMgr.getButton("commands"),
				buttonMgr.getButton("info"), buttonMgr.getButton("links"))).queue();
		return true;
	}
}
