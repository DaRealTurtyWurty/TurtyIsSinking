package dev.turtywurty.turtyissinking.client.screens.widgets;

import net.minecraft.client.gui.Font;
import net.minecraft.client.gui.components.EditBox;
import net.minecraft.network.chat.Component;
import net.minecraft.util.Mth;

public class BarrelNumberEditBox extends EditBox {
    private final ListenerButton decreaseButton, increaseButton;

    private int value;
    private int minValue;
    private int maxValue;

    private BarrelNumberEditBox(Font pFont, int pX, int pY, int pWidth, int pHeight, Component pMessage, ListenerButton decreaseButton, ListenerButton increaseButton) {
        super(pFont, pX, pY, pWidth, pHeight, pMessage);
        setResponder(value -> {
            String text = BarrelNumberEditBox.this.getValue();
            if (value.isEmpty()) {
                setValue(0);
            }

            try {
                setValue(Integer.parseInt(value));
            } catch (NumberFormatException exception) {
                setValue(0);
            }

            if (!text.equals(BarrelNumberEditBox.this.getValue()))
                setCursorPosition(getValue().length());

            updateButtons();
        });

        setFilter(value -> {
            if (value.isEmpty())
                return true;

            try {
                int parsedValue = Integer.parseInt(value);
                return parsedValue >= this.minValue && parsedValue <= this.maxValue;
            } catch (NumberFormatException exception) {
                return false;
            }
        });

        this.decreaseButton = decreaseButton;
        this.increaseButton = increaseButton;

        this.decreaseButton.addPressListener(() -> {
            if(this.decreaseButton.active) {
                setValue(this.value - 1);
            }

            updateButtons();
        });

        this.increaseButton.addPressListener(() -> {
            if(this.increaseButton.active) {
                setValue(this.value + 1);
            }

            updateButtons();
        });
    }

    public void updateButtons() {
        this.decreaseButton.active = this.value != this.minValue;
        this.increaseButton.active = this.value != this.maxValue;
    }

    public int getIntValue() {
        return this.value;
    }

    public void setValue(int value) {
        boolean changed = this.value != value;

        this.value = Mth.clamp(value, this.minValue, this.maxValue);
        internal_setValue(Integer.toString(this.value));

        if (changed) {
            moveCursorToEnd();
        }
    }

    private void internal_setValue(String text) {
        if (super.filter.test(text)) {
            super.value = text.length() > super.maxLength ? text.substring(0, super.maxLength) : text;

            setCursorPosition(super.value.length());
            setHighlightPos(getCursorPosition());
        }
    }

    public void setMinValue(int minValue) {
        this.minValue = Math.min(minValue, this.maxValue - 1);
        this.value = Mth.clamp(this.value, this.minValue, this.maxValue);
    }

    public void setMaxValue(int maxValue) {
        this.maxValue = Math.max(maxValue, this.minValue + 1);
        this.value = Mth.clamp(this.value, this.minValue, this.maxValue);
    }

    public void setMinMaxValues(int minValue, int maxValue) {
        this.minValue = Math.min(minValue, maxValue - 1);
        this.maxValue = Math.max(maxValue, minValue + 1);
        this.value = Mth.clamp(this.value, this.minValue, this.maxValue);
    }

    public static class Builder {
        private final Font font;
        private final Component message;
        private final ListenerButton decreaseButton, increaseButton;

        private int x, y, width, height;
        private int defaultValue = 0;
        private int minValue = Integer.MIN_VALUE, maxValue = Integer.MAX_VALUE;

        public Builder(Font font, Component message, ListenerButton decreaseButton, ListenerButton increaseButton) {
            this.font = font;
            this.message = message;
            this.decreaseButton = decreaseButton;
            this.increaseButton = increaseButton;
        }

        public Builder x(int x) {
            this.x = x;
            return this;
        }

        public Builder y(int y) {
            this.y = y;
            return this;
        }

        public Builder width(int width) {
            this.width = width;
            return this;
        }

        public Builder height(int height) {
            this.height = height;
            return this;
        }

        public Builder position(int x, int y) {
            this.x = x;
            this.y = y;
            return this;
        }

        public Builder size(int width, int height) {
            this.width = width;
            this.height = height;
            return this;
        }

        public Builder bounds(int x, int y, int width, int height) {
            this.x = x;
            this.y = y;
            this.width = width;
            this.height = height;
            return this;
        }

        public Builder defaultValue(int value) {
            this.defaultValue = Mth.clamp(value, this.minValue, this.maxValue);
            return this;
        }

        public Builder minValue(int minValue) {
            this.minValue = minValue;
            this.defaultValue = Mth.clamp(this.defaultValue, this.minValue, this.maxValue);
            return this;
        }

        public Builder maxValue(int maxValue) {
            this.maxValue = maxValue;
            this.defaultValue = Mth.clamp(this.defaultValue, this.minValue, this.maxValue);
            return this;
        }

        public BarrelNumberEditBox build() {
            BarrelNumberEditBox editBox = new BarrelNumberEditBox(this.font, this.x, this.y, this.width, this.height, this.message, this.decreaseButton, this.increaseButton);
            editBox.setMinMaxValues(this.minValue, this.maxValue);
            editBox.setValue(this.defaultValue);
            return editBox;
        }
    }
}
