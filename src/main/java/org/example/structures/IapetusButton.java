package org.example.structures;

import net.dv8tion.jda.api.events.interaction.component.ButtonInteractionEvent;
import net.dv8tion.jda.api.interactions.components.buttons.Button;


/** Represents a button to the <b>ButtonManager</b> class. <br><br>
 *
 * <h3>Anonymous implementation:</h3>
 * <pre>{@code
 * // example of an anonymous button implementation.
 *  const bm = Main.buttonManager;
 *  bm.addButtons(new IapetusButton("id")) {
 *      @Override
 *      public void run(ButtonInteractionEvent event) {
 *          event.reply("example").queue();
 *      }
 *  })
 *  }</pre>
 *  <h3>Subclass implementation:</h3>
 * <pre>{@code
 * // example of a defined subclass
 *  public class ExampleButton extends IapetusButton {
 *      public ExampleButton() {
 *          super("id")
 *      }
 *
 *      @Override
 *      public void run (ButtonInteractionEvent event) {
 *          String response = exampleFunction();
 *          event.reply(response).queue();
 *      }
 *
 *      private String exampleFunction() {
 *          return "example response!";
 *      }
 *  }
 *  }</pre>
 *
 *  <pre>{@code
 *  // put this somewhere where it'll run on startup:
 *  const bm = Main.buttonManager;
 *  bm.addButtons(new ExampleButton());
 *  }</pre>
 *
 * @see org.example.ButtonManager
 */
public abstract class IapetusButton {
    String id;
    Button button;
    // Button button;

    public IapetusButton(String id) {
        this.id = id;
    }

    public IapetusButton(Button button) {
        this.id = button.getId();
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
