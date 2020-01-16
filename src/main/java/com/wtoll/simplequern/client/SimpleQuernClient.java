package com.wtoll.simplequern.client;

import com.wtoll.simplequern.client.screen.QuernScreen;
import com.wtoll.simplequern.container.Containers;
import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.fabric.api.client.screen.ScreenProviderRegistry;

public class SimpleQuernClient implements ClientModInitializer {
    @SuppressWarnings("deprecation")
    @Override
    public void onInitializeClient() {
        ScreenProviderRegistry.INSTANCE.registerFactory(Containers.QUERN, (syncId, identifier, player, buffer) -> {
            return new QuernScreen(syncId, player);
        });
    }
}
