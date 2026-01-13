package org.example;

import net.dv8tion.jda.api.components.buttons.Button;
import net.dv8tion.jda.api.events.interaction.component.ButtonInteractionEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;
import org.example.structures.IapetusButton;

import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

public class ButtonManager extends ListenerAdapter {
    private final Map<String, IapetusButton> buttons = new HashMap<>();

    @Override
    public void onButtonInteraction(ButtonInteractionEvent event) {
        String buttonId = event.getButton().getCustomId();
        IapetusButton button = getIapetusButton(buttonId);
        if (button == null) return;

        button.run(event);
    }

    public IapetusButton getIapetusButton(String id) {
        return this.buttons.get(id);
    }
    public Button getButton(String id) {
        return getIapetusButton(id).getButton();
    }
    public boolean buttonExists(String id) { return getIapetusButton(id) != null; }
    public void addButtons(IapetusButton... buttons) { Arrays.stream(buttons).forEach(button -> this.buttons.put(button.getId(), button)); }
    public Map<String, IapetusButton> getButtons() {
        return this.buttons;
    }
}
