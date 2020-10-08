package com.cumulus.simplequern;

import com.cumulus.simplequern.init.SimpleQuernBlocks;
import com.cumulus.simplequern.init.SimpleQuernBlockEntityTypes;
import com.cumulus.simplequern.init.SimpleQuernItems;
import com.cumulus.simplequern.init.SimpleQuernStats;
import com.cumulus.simplequern.init.SimpleQuernRecipeSerializers;
import com.cumulus.simplequern.init.SimpleQuernRecipeTypes;
import com.cumulus.simplequern.init.SimpleQuernScreenHandlers;
import net.fabricmc.api.ModInitializer;
import net.minecraft.util.Identifier;

@SuppressWarnings("unused")
public class SimpleQuern implements ModInitializer {

    public static final String MODID = "simplequern";

    @Override
    public void onInitialize() {
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
