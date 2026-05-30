package io.github.aaryadev.justsingle.client.emulation;

import java.util.concurrent.ThreadLocalRandom;

public final class ClickTimingModel {
    private static final double LEFT_CLICK_PROBABILITY = 0.35D;

    private static final long MIN_DUPLICATE_COOLDOWN_NS = 14_000_000L;
    private static final long MAX_DUPLICATE_COOLDOWN_NS = 38_000_000L;

    private static final long MIN_NATIVE_PRESS_SPACING_NS = 7_000_000L;
    private static final long MAX_VALID_HOLD_NS = 350_000_000L;

    private static final long MIN_BOUNCE_GAP_NS = 120_000L;
    private static final long MAX_BOUNCE_GAP_NS = 680_000L;

    public boolean shouldDuplicate(int button, long nowNanos, long lastDuplicateNanos, long lastNativePressNanos) {
        if (lastNativePressNanos != 0L && nowNanos - lastNativePressNanos < MIN_NATIVE_PRESS_SPACING_NS) {
            return false;
        }

        if (lastDuplicateNanos != 0L) {
            long cooldownNs = sampleDuplicateCooldownNs();
            if (nowNanos - lastDuplicateNanos < cooldownNs) {
                return false;
            }
        }

        if (button == 1) {
            return true;
        }

        double probability = LEFT_CLICK_PROBABILITY;
        return ThreadLocalRandom.current().nextDouble() < probability;
    }

    public boolean isTapLike(long pressStartNanos, long nowNanos) {
        if (pressStartNanos == 0L) {
            return true;
        }
        return nowNanos - pressStartNanos <= MAX_VALID_HOLD_NS;
    }

    public long sampleBounceGapNs() {
        return ThreadLocalRandom.current().nextLong(MIN_BOUNCE_GAP_NS, MAX_BOUNCE_GAP_NS + 1L);
    }

    private long sampleDuplicateCooldownNs() {
        return ThreadLocalRandom.current().nextLong(MIN_DUPLICATE_COOLDOWN_NS, MAX_DUPLICATE_COOLDOWN_NS + 1L);
    }
}
