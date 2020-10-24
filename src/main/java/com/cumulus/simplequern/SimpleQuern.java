package com.cumulus.simplequern;

import com.cumulus.simplequern.init.*;
import net.fabricmc.api.ModInitializer;
import net.minecraft.util.Identifier;

@SuppressWarnings("unused")
public class SimpleQuern implements ModInitializer {

    public static final String MODID = "simplequern";

    @Override
    public void onInitialize() {
        SimpleQuernCompat.onInitialize();
        SimpleQuernStats.onInitialize();
        SimpleQuernRecipeSerializers.onInitialize();
        SimpleQuernRecipeTypes.onInitialize();
        SimpleQuernScreenHandlers.onInitialize();
        SimpleQuernItems.onInitialize();
        SimpleQuernBlockEntityTypes.onInitialize();
        SimpleQuernBlocks.onInitialize();
    }

    public static Identifier id(String s) {
        return new Identifier(MODID, s);
    }
}
