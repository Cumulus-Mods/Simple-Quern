package com.cumulus.simplequern;

import com.cumulus.simplequern.block.Blocks;
import com.cumulus.simplequern.block.entity.BlockEntityType;
import com.cumulus.simplequern.item.Items;
import com.cumulus.simplequern.stat.Stats;
import com.cumulus.simplequern.recipe.RecipeSerializer;
import com.cumulus.simplequern.recipe.RecipeType;
import com.cumulus.simplequern.screen.ScreenHandlers;
import net.fabricmc.api.ModInitializer;
import net.minecraft.util.Identifier;

@SuppressWarnings("unused")
public class SimpleQuern implements ModInitializer {

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

    public static Identifier id(String s) {
        return new Identifier(MODID, s);
    }
}
