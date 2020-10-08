package com.cumulus.simplequern.init;

import com.cumulus.simplequern.SimpleQuern;
import com.cumulus.simplequern.screen.QuernScreenHandler;
import net.fabricmc.fabric.api.screenhandler.v1.ScreenHandlerRegistry;
import net.minecraft.screen.ScreenHandlerType;

public class SimpleQuernScreenHandlers {
	public static ScreenHandlerType<QuernScreenHandler> QUERN;

	public static void onInitialize() {
		QUERN = ScreenHandlerRegistry.registerSimple(SimpleQuern.id("quern"), QuernScreenHandler::new);
	}
}
