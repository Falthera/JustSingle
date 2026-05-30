package io.github.aaryadev.justsingle.client.emulation;

import io.github.aaryadev.justsingle.client.mixin.client.MouseInvoker;
import io.github.aaryadev.justsingle.client.state.ButtonStateTracker;
import net.minecraft.client.Mouse;
import org.lwjgl.glfw.GLFW;

import java.util.concurrent.locks.LockSupport;

public final class DoubleClickEmulator {
    private static final ThreadLocal<Boolean> SYNTHETIC_GUARD = ThreadLocal.withInitial(() -> Boolean.FALSE);

    private final ClickTimingModel timingModel;
    private final ButtonStateTracker stateTracker;

    public DoubleClickEmulator(ClickTimingModel timingModel, ButtonStateTracker stateTracker) {
        this.timingModel = timingModel;
        this.stateTracker = stateTracker;
    }

    public boolean isSyntheticDispatch() {
        return SYNTHETIC_GUARD.get();
    }

    public void duplicateAsHardwareBounce(Mouse mouse, long window, int button, int mods, long nowNanos, boolean inScreen) {
        long lastNativePressNanos = stateTracker.getLastNativePressNanos(button);
        long lastDuplicateNanos = stateTracker.getLastDuplicateNanos(button);

        if (!timingModel.shouldDuplicate(inScreen, nowNanos, lastDuplicateNanos, lastNativePressNanos)) {
            return;
        }

        if (!timingModel.isTapLike(stateTracker.getPressStartNanos(button), nowNanos)) {
            return;
        }

        SYNTHETIC_GUARD.set(Boolean.TRUE);
        try {
            MouseInvoker invoker = (MouseInvoker) mouse;
            invoker.justsingle$invokeOnMouseButton(window, button, GLFW.GLFW_RELEASE, mods);
            LockSupport.parkNanos(timingModel.sampleBounceGapNs());
            invoker.justsingle$invokeOnMouseButton(window, button, GLFW.GLFW_PRESS, mods);
            stateTracker.markDuplicated(button, nowNanos);
        } finally {
            SYNTHETIC_GUARD.set(Boolean.FALSE);
        }
    }
}
