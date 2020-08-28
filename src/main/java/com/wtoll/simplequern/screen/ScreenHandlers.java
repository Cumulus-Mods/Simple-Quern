package com.wtoll.simplequern.screen;

import com.wtoll.simplequern.Utility;
import net.fabricmc.fabric.api.client.screenhandler.v1.ScreenRegistry;
import net.fabricmc.fabric.api.screenhandler.v1.ScreenHandlerRegistry;
import net.minecraft.screen.ScreenHandlerType;

public class ScreenHandlers
{
	public static ScreenHandlerType<QuernScreenHandler> QUERN;

	public static void initialize()
	{
		QUERN = ScreenHandlerRegistry.registerSimple(Utility.id("quern"), QuernScreenHandler::new);
	}
}
