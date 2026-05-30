package io.github.aaryadev.justsingle.client;

import io.github.aaryadev.justsingle.JustSingleMod;
import io.github.aaryadev.justsingle.client.input.MouseInputInterceptor;
import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.fabric.api.client.event.lifecycle.v1.ClientTickEvents;

public final class JustSingleClient implements ClientModInitializer {
    @Override
    public void onInitializeClient() {
        ClientTickEvents.END_CLIENT_TICK.register(MouseInputInterceptor.getInstance()::onClientTick);
        JustSingleMod.LOGGER.info("JustSingle client pipeline initialized");
    }
}
