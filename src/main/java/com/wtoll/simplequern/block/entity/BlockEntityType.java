package com.wtoll.simplequern.block.entity;

import com.wtoll.simplequern.Utility;
import com.wtoll.simplequern.block.Blocks;
import net.minecraft.util.registry.Registry;

public class BlockEntityType {
    public static final net.minecraft.block.entity.BlockEntityType<QuernBlockEntity> QUERN;

    static {
        QUERN = Registry.register(Registry.BLOCK_ENTITY, Utility.id("quern"), net.minecraft.block.entity.BlockEntityType.Builder.create(QuernBlockEntity::new, Blocks.QUERN).build(null));
    }

    public static void initialize() {  }
}
