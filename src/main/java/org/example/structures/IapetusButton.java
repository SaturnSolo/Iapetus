package org.example.structures;

import net.dv8tion.jda.api.components.buttons.Button;
import net.dv8tion.jda.api.events.interaction.component.ButtonInteractionEvent;


/**
 * Represents a button to the <b>ButtonManager</b> class. <br><br>
 *
 * <h3>Anonymous implementation:</h3>
 * <pre>{@code
 * // example of an anonymous button implementation.
 *  const bm = Main.buttonManager;
 *  bm.addButtons(new IapetusButton("id")) {
 * @Override
 * public void run(ButtonInteractionEvent event) {
 * event.reply("example").queue();
 * }
 * })
 * }</pre>
 * <h3>Subclass implementation:</h3>
 * <pre>{@code
 * // example of a defined subclass
 *  public class ExampleButton extends IapetusButton {
 *      public ExampleButton() {
 *          super("id")
 * }
 *
 * @Override
 * public void run (ButtonInteractionEvent event) {
 * String response = exampleFunction();
 * event.reply(response).queue();
 * }
 *
 * private String exampleFunction() {
 * return "example response!";
 * }
 * }
 * }</pre>
 *
 * <pre>{@code
 *  // put this somewhere where it'll run on startup:
 *  const bm = Main.buttonManager;
 *  bm.addButtons(new ExampleButton());
 *  }</pre>
 *
 * @see org.example.ButtonManager
 */
public abstract class IapetusButton {
    private final String id;
    private final Button button;

    public IapetusButton(Button button) {
        this.id = button.getCustomId();
        this.button = button;
    }

    public Button getButton() {
        if (button != null) return button;
        return Button.secondary(id, id);
    }

    public abstract void run(ButtonInteractionEvent event);

    public String getId() {
        return id;
    }
}
