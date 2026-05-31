package io.github.aaryadev.justsingle.client.state;

import net.minecraft.item.Item;

public final class ButtonStateTracker {
    private static final int MAX_BUTTONS = 8;

    private final long[] lastNativePressNanos = new long[MAX_BUTTONS];
    private final long[] lastDuplicateNanos = new long[MAX_BUTTONS];
    private final long[] pressStartNanos = new long[MAX_BUTTONS];
    private final boolean[] pressed = new boolean[MAX_BUTTONS];
    private Item lastRightClickItem;

    public void onNativePress(int button, long nowNanos) {
        if (!isTrackedButton(button)) {
            return;
        }
        pressed[button] = true;
        pressStartNanos[button] = nowNanos;
        lastNativePressNanos[button] = nowNanos;
    }

    public void onNativeRelease(int button) {
        if (!isTrackedButton(button)) {
            return;
        }
        pressed[button] = false;
    }

    public long getLastNativePressNanos(int button) {
        if (!isTrackedButton(button)) {
            return 0L;
        }
        return lastNativePressNanos[button];
    }

    public long getLastDuplicateNanos(int button) {
        if (!isTrackedButton(button)) {
            return 0L;
        }
        return lastDuplicateNanos[button];
    }

    public long getPressStartNanos(int button) {
        if (!isTrackedButton(button)) {
            return 0L;
        }
        return pressStartNanos[button];
    }

    public void markDuplicated(int button, long nowNanos) {
        if (!isTrackedButton(button)) {
            return;
        }
        lastDuplicateNanos[button] = nowNanos;
    }

    public Item getLastRightClickItem() {
        return lastRightClickItem;
    }

    public void markRightClickItem(Item item) {
        lastRightClickItem = item;
    }

    public boolean isPressed(int button) {
        return isTrackedButton(button) && pressed[button];
    }

    public void reset() {
        for (int i = 0; i < MAX_BUTTONS; i++) {
            lastNativePressNanos[i] = 0L;
            lastDuplicateNanos[i] = 0L;
            pressStartNanos[i] = 0L;
            pressed[i] = false;
        }
        lastRightClickItem = null;
    }

    private boolean isTrackedButton(int button) {
        return button >= 0 && button < MAX_BUTTONS;
    }
}
