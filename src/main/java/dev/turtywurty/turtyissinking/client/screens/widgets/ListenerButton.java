package dev.turtywurty.turtyissinking.client.screens.widgets;

import net.minecraft.client.gui.components.Button;

import java.util.ArrayList;
import java.util.List;

public class ListenerButton extends Button {
    private final List<Runnable> onPress = new ArrayList<>();

    public ListenerButton(Builder builder) {
        super(builder);
    }

    @Override
    public void onClick(double pMouseX, double pMouseY) {
        super.onClick(pMouseX, pMouseY);
        if (this.onPress.isEmpty())
            return;

        for (Runnable runnable : this.onPress) {
            runnable.run();
        }
    }

    public Runnable addPressListener(Runnable runnable) {
        this.onPress.add(runnable);
        return runnable;
    }

    public void removePressListener(Runnable runnable) {
        this.onPress.remove(runnable);
    }
}
