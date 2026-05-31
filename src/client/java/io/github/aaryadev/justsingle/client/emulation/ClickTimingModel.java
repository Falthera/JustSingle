package io.github.aaryadev.justsingle.client.emulation;

import java.util.concurrent.ThreadLocalRandom;

public final class ClickTimingModel {
    private static final double LEFT_CLICK_PROBABILITY = 0.35D;
    private static final double RIGHT_CLICK_PROBABILITY = 0.70D;

    private static final long MIN_BOUNCE_GAP_NS = 120_000L;
    private static final long MAX_BOUNCE_GAP_NS = 680_000L;

    public boolean shouldDuplicate(int button, long nowNanos, long lastDuplicateNanos, long lastNativePressNanos) {
        if (button == 1) {
            return ThreadLocalRandom.current().nextDouble() < RIGHT_CLICK_PROBABILITY;
        }

        double probability = LEFT_CLICK_PROBABILITY;
        return ThreadLocalRandom.current().nextDouble() < probability;
    }

    public long sampleBounceGapNs() {
        return ThreadLocalRandom.current().nextLong(MIN_BOUNCE_GAP_NS, MAX_BOUNCE_GAP_NS + 1L);
    }
}
