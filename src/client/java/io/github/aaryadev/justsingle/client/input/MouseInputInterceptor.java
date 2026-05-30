package io.github.aaryadev.justsingle.client.input;

import io.github.aaryadev.justsingle.client.emulation.ClickTimingModel;
import io.github.aaryadev.justsingle.client.emulation.DoubleClickEmulator;
import io.github.aaryadev.justsingle.client.state.ButtonStateTracker;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.Mouse;
import net.minecraft.client.input.MouseInput;
import org.lwjgl.glfw.GLFW;

public final class MouseInputInterceptor {
    private static final MouseInputInterceptor INSTANCE = new MouseInputInterceptor();

    private final ButtonStateTracker buttonStateTracker = new ButtonStateTracker();
    private final DoubleClickEmulator emulator = new DoubleClickEmulator(new ClickTimingModel(), buttonStateTracker);

    private boolean focusedLastTick = true;

    private MouseInputInterceptor() {
    }

    public static MouseInputInterceptor getInstance() {
        return INSTANCE;
    }

    public void onClientTick(MinecraftClient client) {
        if (client == null) {
            return;
        }

        boolean focusedNow = client.isWindowFocused();
        if (!focusedNow && focusedLastTick) {
            buttonStateTracker.reset();
        }
        focusedLastTick = focusedNow;
    }

    public void onMouseButton(Mouse mouse, MinecraftClient client, long window, MouseInput input, int action) {
        if (emulator.isSyntheticDispatch()) {
            return;
        }

        if (client == null || client.getWindow() == null) {
            return;
        }

        if (window != client.getWindow().getHandle()) {
            return;
        }

        if (!client.isWindowFocused()) {
            buttonStateTracker.reset();
            return;
        }

        int button = input.button();
        long nowNanos = System.nanoTime();
        if (action == GLFW.GLFW_PRESS) {
            buttonStateTracker.onNativePress(button, nowNanos);
            boolean inScreen = client.currentScreen != null;
            emulator.duplicateAsHardwareBounce(mouse, window, input, nowNanos, inScreen);
            return;
        }

        if (action == GLFW.GLFW_RELEASE) {
            buttonStateTracker.onNativeRelease(button);
        }
    }
}
