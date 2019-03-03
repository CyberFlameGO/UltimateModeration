package com.songoda.ultimatemoderation.utils.gui;

import org.bukkit.Sound;
import org.bukkit.event.inventory.ClickType;

public class Range {

    private int min;
    private int max;
    private ClickType clickType;
    private boolean bottom;
    private Sound onClickSound = Sound.UI_BUTTON_CLICK;

    public Range(int min, int max, ClickType clickType, boolean bottom) {
        this.min = min;
        this.max = max;
        this.clickType = clickType;
        this.bottom = bottom;
    }

    public Range(int min, int max, Sound onClickSound, ClickType clickType, boolean bottom) {
        this.min = min;
        this.max = max;
        this.onClickSound = onClickSound;
        this.clickType = clickType;
        this.bottom = bottom;
    }

    public int getMin() {
        return min;
    }

    public int getMax() {
        return max;
    }

    public ClickType getClickType() {
        return clickType;
    }

    public boolean isBottom() {
        return bottom;
    }

    public Sound getOnClickSound() {
        return onClickSound;
    }
}
