package org.example;

import net.dv8tion.jda.api.events.interaction.component.ButtonInteractionEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;
import net.dv8tion.jda.api.interactions.components.buttons.Button;
import org.example.structures.IapetusButton;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ButtonManager extends ListenerAdapter {
    private final Map<String, IapetusButton> buttons = new HashMap<>();

    @Override
    public void onButtonInteraction(ButtonInteractionEvent event) {
        String buttonId = event.getButton().getId();
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
    public void removeButton(String id) {
        this.buttons.remove(id);
    }

    public void addButtons(List<IapetusButton> commands) {
        commands.forEach(button -> this.buttons.put(button.getId(), button));
    }
    public void addButtons(IapetusButton... buttons) {
        addButtons(Arrays.asList(buttons));
    }

    public Map<String, IapetusButton> getButtons() {
        return this.buttons;
    }

}
