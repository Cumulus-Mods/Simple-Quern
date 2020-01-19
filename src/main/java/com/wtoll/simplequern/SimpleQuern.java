package com.wtoll.simplequern;

import com.wtoll.simplequern.block.Blocks;
import com.wtoll.simplequern.block.entity.BlockEntityType;
import com.wtoll.simplequern.container.Containers;
import com.wtoll.simplequern.item.Items;
import com.wtoll.simplequern.recipe.RecipeSerializer;
import com.wtoll.simplequern.recipe.RecipeType;
import com.wtoll.simplequern.stat.Stats;
import net.fabricmc.api.ModInitializer;

public class SimpleQuern implements ModInitializer {
    public static final String MODID = "simplequern";

    // Two issues
    // The handstones don't break after
    // The tiers don't work

    @Override
    public void onInitialize() {
        Stats.initialize();
        RecipeSerializer.initialize();
        RecipeType.initialize();
        Containers.initialize();
        Items.initialize();
        BlockEntityType.initialize();
        Blocks.initialize();
    }
}
