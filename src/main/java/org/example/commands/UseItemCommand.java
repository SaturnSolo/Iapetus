package org.example.commands;

import net.dv8tion.jda.api.events.interaction.command.SlashCommandInteractionEvent;
import net.dv8tion.jda.api.interactions.commands.Command;
import net.dv8tion.jda.api.interactions.commands.OptionType;
import net.dv8tion.jda.api.interactions.commands.build.Commands;
import net.dv8tion.jda.api.interactions.commands.build.OptionData;
import org.example.ItemManager;
import org.example.items.Item;
import org.example.structures.IapetusCommand;

import java.util.ArrayList;
import java.util.Collection;

public class UseItemCommand extends IapetusCommand {
	private final ItemManager itemMgr;

	public UseItemCommand(ItemManager itemMgr) {
		super(Commands.slash("use", "to use an item").addOptions(
				new OptionData(OptionType.STRING, "item", "use item", true).addChoices(getChoices(itemMgr))));

		this.itemMgr = itemMgr;
	}

	@Override
	public boolean runCommand(SlashCommandInteractionEvent event) {
		String choice = event.getOption("item").getAsString(); // NPE is impossible
		String userId = event.getUser().getId();

		if (!itemMgr.hasItem(userId, choice)) {
			event.reply("**You don't have this item in your inventory.**").setEphemeral(true).queue();
			return true;
		}
		Item item = itemMgr.getItem(choice);
		if (item.use(event))
			itemMgr.takeItem(userId, choice);
		return true;
	}

	private static Collection<Command.Choice> getChoices(ItemManager itemMgr) {
		Collection<Command.Choice> choices = new ArrayList<>();
		itemMgr.getItems().forEach((id, item) -> choices.add(new Command.Choice(item.getString(true), id)));
		return choices;
	}
}
