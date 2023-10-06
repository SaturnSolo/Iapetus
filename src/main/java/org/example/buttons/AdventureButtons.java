package org.example.buttons;

import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.entities.Message;
import net.dv8tion.jda.api.entities.MessageEmbed;
import net.dv8tion.jda.api.entities.User;
import net.dv8tion.jda.api.entities.emoji.Emoji;
import net.dv8tion.jda.api.events.interaction.component.ButtonInteractionEvent;
import net.dv8tion.jda.api.interactions.components.ActionRow;
import net.dv8tion.jda.api.interactions.components.ItemComponent;
import net.dv8tion.jda.api.interactions.components.buttons.Button;
import net.dv8tion.jda.api.interactions.components.buttons.ButtonStyle;
import net.dv8tion.jda.api.utils.messages.MessageEditBuilder;
import org.example.ButtonManager;
import org.example.ItemManager;
import org.example.Main;
import org.example.commands.AdventureCommands;
import org.example.items.Item;
import org.example.structures.IapetusButton;
import org.example.utils.BerryUtils;
import org.example.utils.InventoryUtils;
import org.example.utils.MemberUtils;

import java.util.*;

public class AdventureButtons {
    ButtonManager bm = Main.buttonManager;
    Map<String, AdventureLocation> locations = new HashMap<>();
    Map<String, ChoiceEvent> choiceEvents = new HashMap<>();
    Random random = new Random();


    //
    // INITIALIZING THE BUTTONS AND LOCATIONS.
    //

    public AdventureButtons() {
        bm.addButtons(
          new ConfirmButton(),
          new LeaveButton(),
          new CancelButton()
        );

        addLocations(
          new AdventureLocation("cave", "%s stumbled upon a cave.", "it seems quite deep, it requires you to crawl.", 5).addEvents(
            new AdventureEvent("The crawl was quite uncomfortable and you didn't find anything.", 4),
            new AdventureEvent("You couldn't fit through the tiny gap.", 1),
            new FindEvent("You crawled deep into the cave and found a neat rock. You pocketed it.","rock",4),
            new FindEvent("You noticed a sparkle as you crawled through the dark cave. When you got closer you realized it was a gemstone!","gem",1),

            new ChoiceEvent("The path splits in two directions. \nOn the left you see a small glint, on the right darkness.", "cave_split",8).addChoices(
              new AdventureChoice("Go left", Emoji.fromUnicode("◀"), ButtonStyle.SECONDARY).addEvents(
                new FindEvent("You walk up to the glint and its a gem!","gem",2),
                new DeathEvent("You walk up to the glint and its a bear D:\n You attempt to flee but fail.", 1),
                new AdventureEvent("You walk up to the glint and its a bear D:\n You attempt to flee and succeed!", 1)
              ),
              new AdventureChoice("Go right", Emoji.fromUnicode("▶"), ButtonStyle.SECONDARY).addEvents(
                new FindEvent("You pushed forward and found a neat rock.", "rock", 1),
                new ChoiceEvent("You pushed forward and found an egg, do you take it?", "cave_find_egg",1).addChoices(
                  new AdventureChoice("Take it!", Emoji.fromUnicode("🥚")).addEvents(
                    new FindEvent("You picked up the egg, I wonder whats inside?", "egg", 3, 1),
                    new DeathEvent("Its mother was just around the corner! It attacked you and took some of your items.", 2)
                  ),
                  new AdventureChoice("Leave it be.", ButtonStyle.SECONDARY).addEvents(
                    new AdventureEvent("You left the egg be and left the cave, light at last!", 1)
                  )
                )
              )
            ),
            new ChoiceEvent("The corridor is getting a bit tight, do you continue?", "cave_tight", 8).addChoices(
              new AdventureChoice("Continue", Emoji.fromUnicode("⏩"), ButtonStyle.DANGER).addEvents(
                new DeathEvent("You got stuck in the corridor!", 2),
                new DeathEvent("Another person was trying to get through on the other side. \nBoth of you got trapped.", 1),
                new AdventureEvent("You made it to the other side, but you didn't find anything", 5),
                new FindEvent("You made it to the other side and found a massive gem cluster!", "gem", 2, 3)
              ),
              new AdventureChoice("Go back", ButtonStyle.SECONDARY).addEvents(
                new AdventureEvent("You got out safely.", 1)
              )
            )
          ),
          new AdventureLocation("throne", "%s walked into a throne room.", "its fit for a queen, what riches might you find?", 1).addEvents(
            new AdventureEvent("You searched around and found nothing", 2),
            new ChoiceEvent("You found a hidden lever that revealed the queens dice collection. Do you take one?", "throne_dice", 1).addChoices(
              new AdventureChoice("Take it!", Emoji.fromUnicode("🎲")).addEvents(
                new FindEvent("You took the dice from the collection, hopefully no one notices!", "dice", 2),
                new AdventureEvent("Uh oh! The queen saw you taking and kicked you from her throne room.", 1)
              ),
              new AdventureChoice("Leave it be.", ButtonStyle.SECONDARY).addEvents(
                new FindEvent("The queen saw you admiring the dice collection and offered you one!", "dice", 1),
                new AdventureEvent("You left the collection alone.", 2)
              )
            )
          ),
          new AdventureLocation("forest", "%s wandered into a forest.", "the brush is quite thick, maybe something is inside.", 4).addEvents(
            new StrawberryEvent("You searched around and found some strawberry bushes, you picked up %s berries",3,5,true),
            new ChoiceEvent("In the wild you found a flower! Do you pick it?", "forest_flower", 3).addChoices(
              new AdventureChoice("Pick it!").addEvents(
                new FindEvent("You picked the rose, it hurt a little but your ok!", "rose",1)
              ),
              new AdventureChoice("Keep searching.", ButtonStyle.SECONDARY).addEvents(
                new LocationEvent("forest",8),
                new LocationEvent("cave",2)
              )
            ),
            new AdventureEvent("You kicked some brush around and found more brush, you found nothing.", 1)
          )
//          new AdventureLocation("hell", "Welcome to hell, %s.", "you can't escape...", 1).addEvents(
//            new DeathEvent("You met the devil, he killed you.", 1)
//          ).disableLeave()
        );
    }

    //
    // MANAGING THE LOCATIONS
    //

    public void addLocations(List<AdventureLocation> locations) {
        locations.forEach(location -> this.locations.put(location.id, location));
    }
    public void addLocations(AdventureLocation... locations) {
        addLocations(Arrays.asList(locations));
    }

    public AdventureLocation getLocation(String location) {
        return locations.get(location);
    }

    private AdventureLocation getRandomLocation() {
        List<AdventureLocation> locations = new ArrayList<>();
        this.locations.values().forEach(location -> {
            for (int i = 0; i < location.odds; ++i) locations.add(location);
        });
        int index = random.nextInt(locations.size());
        return locations.get(index);
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

    public void disableRow(Message message) {
        disableRow(message, 0);
    }
    public void disableRow(Message message, int row) {
        ActionRow disabled = message.getActionRows().get(row).asDisabled();
        message.editMessageComponents(disabled).queue();
    }
    public String selectRandom(String... strings) {
        List<String> list = Arrays.asList(strings);
        int index = new Random().nextInt(list.size());
        return list.get(index);
    }

    //
    // LOCATION CLASS
    //

    private class AdventureLocation {
        String id;
        String name;
        String description;
        int odds;
        boolean canLeave = true;

        // List<IapetusButton> buttons;
        List<AdventureEvent> events;

        public AdventureLocation(String id, String name, String description, int odds) {
            this.id = id;
            this.name = name;
            this.description = description;
            this.odds = odds;

            // this.buttons = new ArrayList<>();
            this.events = new ArrayList<>();
        }
        public AdventureLocation(String id, String description, int odds) {
            this(id,id,description,odds);
        }
        public AdventureLocation(String id, String name, String description) {
            this(id,name,description,1);
        }
        public AdventureLocation(String id, String description) {
            this(id,id,description,1);
        }

        public String asString() {
            return description;
        }

        public void investigate(ButtonInteractionEvent event) {
            int index = random.nextInt(events.size());
            events.get(index).run(event);
        }

        public MessageEmbed generateEmbed(User user ) {
            return new EmbedBuilder()
              .setTitle(name.replaceAll("%s",user.getEffectiveName()))
              .setDescription(description)
              .setFooter("Investigate \uD83D\uDD0D or Leave \uD83D\uDCA8")
              .setColor(0x474B24)
              .build();
        }

        public AdventureLocation addEvents(List<AdventureEvent> events) {
            events.forEach(event -> {
                for (int i = 0; i < event.odds; ++i) this.events.add(event);
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
        public AdventureLocation disableLeave() {
            return setCanLeave(false);
        }
        public AdventureLocation enableLeave() {
            return setCanLeave(true);
        }

    }


    //
    // EVENTS!!
    // TODO: puzzle event, death event (lose random item), location (switch) event
    //

    // this is the base event, extended for different events!
    private static class AdventureEvent {
        String message;
        int odds;
        public AdventureEvent(String message, int odds) {
            this.message = message;
            this.odds = odds;
        }

        public void run(ButtonInteractionEvent event) {
            MessageEmbed embed = new EmbedBuilder(event.getMessage().getEmbeds().get(0))
              .setDescription(message).setFooter(null).build();
            event.editMessage(new MessageEditBuilder().setEmbeds(embed).setReplace(true).build()).queue();
        }
    }

    // when a user finds an iteM!
    private class FindEvent extends AdventureEvent {
        private final int amount;
        private final ItemManager im = Main.itemManager;
        String itemId;
        public FindEvent(String message, String itemId, int odds) {
            this(message,itemId,odds,1);
        }
        public FindEvent(String message, String itemId, int odds, int amount) {
            super(message, odds);
            this.itemId = itemId;
            this.amount = amount;
        }

        @Override
        public void run(ButtonInteractionEvent event) {
            if (itemId != null) im.giveItem(event.getUser().getId(), itemId, amount);
            MessageEmbed embed = new EmbedBuilder(event.getMessage().getEmbeds().get(0))
              .setDescription(message)
              .setFooter("+"+amount+" "+im.getItem(itemId).getString(true))
              .build();
            event.editMessage(new MessageEditBuilder().setEmbeds(embed).setReplace(true).build()).queue();
        }
    }

    private class StrawberryEvent extends AdventureEvent {
        private final boolean isRandom;
        private final int amount;
        private final ItemManager im = Main.itemManager;
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
            if (isRandom) found = random.nextInt(amount) + 1;

            BerryUtils.giveUserBerries(event.getUser(), found);
            MessageEmbed embed = new EmbedBuilder(event.getMessage().getEmbeds().get(0))
              .setDescription(message.replaceAll("%s", ""+found))
              .setFooter("+"+found+" Strawberries 🍓")
              .build();
            event.editMessage(new MessageEditBuilder().setEmbeds(embed).setReplace(true).build()).queue();
        }
    }

    private class DeathEvent extends AdventureEvent {
        ItemManager im = Main.itemManager;
        public DeathEvent(String message, int odds) {
            super(message, odds);
        }

        @Override
        public void run(ButtonInteractionEvent event) {
            User user = event.getUser();
            InventoryUtils.Inventory inventory = InventoryUtils.getUserInventory(user.getId());

            String footer = null;
            if (inventory.size() > 2) {
                int itemsToTake = (int) Math.floor(Math.sqrt(inventory.size())-0.4);
                Map<Item, Integer> taken = new HashMap<>();
                for (int i = 0; i < itemsToTake ; i++) {
                    Item item = inventory.remove(random.nextInt(inventory.size()));
                    Integer count = taken.get(item);
                    if (count == null) count = 0;
                    taken.put(item, count+1);
                }

                StringBuilder builder = new StringBuilder();
                taken.forEach((item,count) -> builder.append("-"+count+" "+item.getString(true)+" "));
                footer = builder.toString();
            }

            MessageEmbed embed = new EmbedBuilder(event.getMessage().getEmbeds().get(0))
              .setDescription(message)
              .setFooter(footer)
              .setColor(0xB80C09)
              .build();
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

            String investigateButtonId = "adv-"+location.id+"-investigate";
            if (!bm.buttonExists(investigateButtonId)) bm.addButtons(new InvestigateButton(location.id));

            event.editMessage(new MessageEditBuilder().setEmbeds(embed).setActionRow(
              bm.getButton(investigateButtonId), bm.getButton("adv-leave").withDisabled(!location.canLeave)
            ).setReplace(true).build()).queue();
        }
    }

    private class ChoiceEvent extends AdventureEvent {
        public String id;
        public List<AdventureChoice> choices;

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

        public void run(ButtonInteractionEvent event) {
            Collection<ItemComponent> buttons = new ArrayList<>();
            for (int i = 0; i < choices.size(); i++) {
                if (i > 4) break;
                AdventureChoice choice = choices.get(i);
                ChoiceButton button = choice.getButton(id, i);
                if (!bm.buttonExists(button.getId())) bm.addButtons(button);
                buttons.add(button.getButton());
            }

            MessageEmbed embed = new EmbedBuilder(event.getMessage().getEmbeds().get(0))
              .setDescription(message)
              .build();
            event.editMessage(new MessageEditBuilder().setEmbeds(embed).setActionRow(buttons).setReplace(true).build()).queue();
        }
        public void run(int index, ButtonInteractionEvent event) {
            AdventureChoice choice = choices.get(index);
            choice.select(event);
        }
    }

    public class AdventureChoice {
        ChoiceButton button;
        List<AdventureEvent> events;

        String label;
        Emoji emoji;
        ButtonStyle style;
        public AdventureChoice(String label, Emoji emoji) {
            this.label = label;
            this.emoji = emoji;
            this.style = ButtonStyle.PRIMARY;
            this.events = new ArrayList<>();
        }
        public AdventureChoice(String label, Emoji emoji, ButtonStyle style) {
            this.label = label;
            this.emoji = emoji;
            this.style = style;
            this.events = new ArrayList<>();
        }
        public AdventureChoice(String label) {
            this.label = label;
            this.style = ButtonStyle.PRIMARY;
            this.events = new ArrayList<>();
        }
        public AdventureChoice(String label, ButtonStyle style) {
            this.label = label;
            this.style = style;
            this.events = new ArrayList<>();
        }

        public void select(ButtonInteractionEvent event) {
            int index = random.nextInt(events.size());
            events.get(index).run(event);
        }

        public ChoiceButton getButton(String choiceId,int index) {
            String id = choiceId+"-"+index;
            if (emoji == null) return new ChoiceButton(id, label, style);
            return new ChoiceButton(id, label, emoji, style);
        }

        public AdventureChoice addEvents(List<AdventureEvent> events) {
            events.forEach(event -> {
                for (int i = 0; i < event.odds; ++i) this.events.add(event);
            });
            return this;
        }
        public AdventureChoice addEvents(AdventureEvent... events) {
            return addEvents(Arrays.asList(events));
        }
    }

    //
    // BUTTONS
    //

    private class ConfirmButton extends IapetusButton {
        public ConfirmButton() {
            super(Button.success("confirm-adv", "Confirm").withEmoji(Emoji.fromUnicode("✅")));
        }

        @Override
        public void run(ButtonInteractionEvent event) {
            User user = event.getUser();

//            ActionRow update = event.getMessage().getActionRows().get(0).asDisabled();
//            event.getMessage().editMessageComponents(update).queue();

            AdventureLocation location = getRandomLocation();
            MessageEmbed embed = location.generateEmbed(user);

            String investigateButtonId = "adv-"+location.id+"-investigate";
            if (!bm.buttonExists(investigateButtonId)) bm.addButtons(new InvestigateButton(location.id));

            event.editMessage(new MessageEditBuilder().setEmbeds(embed).setActionRow(
                bm.getButton(investigateButtonId), bm.getButton("adv-leave").withDisabled(!location.canLeave)
              ).setReplace(true).build()).queue();
        }
    }

    public class CancelButton extends IapetusButton {
        public CancelButton() {
            super(Button.secondary("cancel-adv", Emoji.fromUnicode("❌")));
        }

        @Override
        public void run(ButtonInteractionEvent event) {
            String pfp = event.getUser().getAvatarUrl();
            User user = event.getUser();
            MessageEmbed embed = new EmbedBuilder()
              .setThumbnail(pfp)
              .setColor(0x474B24)
              .setTitle(user.getEffectiveName() + "** isn't ready**")
              .setDescription(selectRandom(
                "Adventure isn't always for everybody it's okay",
                "Adventure when you are ready",
                "There is no rush to rush out into the adventure relax while you can",
                "Not ready for adventure yet? That's fine!"
              ))
              .build();

            AdventureCommands.adventureCooldowns.put(user.getId(), new Date());
            event.editMessage(new MessageEditBuilder().setEmbeds(embed).setReplace(true).build()).queue();
        }
    }

    private class InvestigateButton extends IapetusButton {
        public InvestigateButton(String locationId) {
            super(Button.primary("adv-" + locationId + "-investigate", "Investigate").withEmoji(Emoji.fromUnicode("🔍")));
        }

        @Override
        public void run(ButtonInteractionEvent event) {
            String locationId = event.getButton().getId().split("-")[1];
            AdventureLocation location = getLocation(locationId);

//            disableRow(event.getMessage());
            location.investigate(event);
        }
    }

    private class ChoiceButton extends IapetusButton {
        public ChoiceButton(String choiceId, String label, Emoji emoji) {
            super(Button.primary("adv-" + choiceId, label).withEmoji(emoji));
        }

        public ChoiceButton(String choiceId, String label) {
            super(Button.primary("adv-" + choiceId, label));
        }

        public ChoiceButton(String choiceId, String label, Emoji emoji, ButtonStyle style) {
            super(Button.primary("adv-" + choiceId, label).withEmoji(emoji).withStyle(style));
        }

        public ChoiceButton(String choiceId, String label, ButtonStyle style) {
            super(Button.primary("adv-" + choiceId, label).withStyle(style));
        }

        @Override
        public void run(ButtonInteractionEvent event) {
            String choiceId = event.getButton().getId().split("-")[1];
            int selection = Integer.parseInt(event.getButton().getId().split("-")[2]);
            ChoiceEvent choice = getChoice(choiceId);

//            disableRow(event.getMessage());
            choice.run(selection, event);
        }
    }

    private class LeaveButton extends IapetusButton {
        public LeaveButton() {
            super(Button.secondary("adv-leave", Emoji.fromUnicode("\uD83D\uDCA8")));
        }

        @Override
        public void run(ButtonInteractionEvent event) {
            User user = event.getUser();
            MessageEmbed embed = new EmbedBuilder()
              .setThumbnail(user.getAvatarUrl())
              .setColor(0x474B24)
              .setTitle(user.getEffectiveName() + " left the area.")
              .setDescription(selectRandom(
                "You left the area, who knows what adventures awaited you.",
                "Hopefully you didn't forget anything.",
                "Did you have a safe journey?"
              )).build();

            event.editMessage(new MessageEditBuilder().setEmbeds(embed).setReplace(true).build()).queue();
        }
    }
}
