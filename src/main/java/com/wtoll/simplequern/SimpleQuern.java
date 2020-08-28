package com.wtoll.simplequern;

import com.wtoll.simplequern.block.Blocks;
import com.wtoll.simplequern.block.entity.BlockEntityType;
import com.wtoll.simplequern.item.Items;
import com.wtoll.simplequern.recipe.RecipeSerializer;
import com.wtoll.simplequern.recipe.RecipeType;
import com.wtoll.simplequern.screen.QuernScreen;
import com.wtoll.simplequern.screen.ScreenHandlers;
import com.wtoll.simplequern.stat.Stats;
import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.api.ModInitializer;
import net.fabricmc.fabric.api.client.screenhandler.v1.ScreenRegistry;

public class SimpleQuern implements ModInitializer, ClientModInitializer
{
    public static final String MODID = "simplequern";

    @Override
    public void onInitialize() {
        Stats.initialize();
        RecipeSerializer.initialize();
        RecipeType.initialize();
        ScreenHandlers.initialize();
        Items.initialize();
        BlockEntityType.initialize();
        Blocks.initialize();
    }

    @Override
    public void onInitializeClient()
    {
        ScreenRegistry.register(ScreenHandlers.QUERN, QuernScreen::new);
    }
}
