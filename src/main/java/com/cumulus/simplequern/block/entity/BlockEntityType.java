package com.cumulus.simplequern.block.entity;

import com.cumulus.simplequern.SimpleQuern;
import com.cumulus.simplequern.block.Blocks;
import net.minecraft.util.registry.Registry;

public class BlockEntityType {
    public static final net.minecraft.block.entity.BlockEntityType<QuernBlockEntity> QUERN;

    static {
        QUERN = Registry.register(Registry.BLOCK_ENTITY_TYPE, SimpleQuern.id("quern"), net.minecraft.block.entity.BlockEntityType.Builder.create(QuernBlockEntity::new, Blocks.QUERN).build(null));
    }

    public static void initialize() {  }
}
