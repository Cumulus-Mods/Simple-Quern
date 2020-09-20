package com.cumulus.simplequern.screen;

import com.cumulus.simplequern.SimpleQuern;
import net.fabricmc.fabric.api.screenhandler.v1.ScreenHandlerRegistry;
import net.minecraft.screen.ScreenHandlerType;

public class ScreenHandlers {
	public static ScreenHandlerType<QuernScreenHandler> QUERN;

	public static void initialize() {
		QUERN = ScreenHandlerRegistry.registerSimple(SimpleQuern.id("quern"), QuernScreenHandler::new);
	}
}
