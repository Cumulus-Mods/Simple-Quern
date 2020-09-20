package com.cumulus.simplequern.client;

import com.cumulus.simplequern.screen.QuernScreen;
import com.cumulus.simplequern.screen.ScreenHandlers;
import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.fabric.api.client.screenhandler.v1.ScreenRegistry;

public class SimpleQuernClient implements ClientModInitializer {
	@Override
	public void onInitializeClient() {
		ScreenRegistry.register(ScreenHandlers.QUERN, QuernScreen::new);
	}
}
