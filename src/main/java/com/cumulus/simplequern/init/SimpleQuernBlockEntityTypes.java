package com.cumulus.simplequern.init;

import com.cumulus.simplequern.SimpleQuern;
import com.cumulus.simplequern.block.entity.QuernBlockEntity;
import com.cumulus.simplequern.init.SimpleQuernBlocks;
import net.minecraft.block.entity.BlockEntityType;
import net.minecraft.util.registry.Registry;

public class SimpleQuernBlockEntityTypes {
    public static final BlockEntityType<QuernBlockEntity> QUERN = Registry.register(Registry.BLOCK_ENTITY_TYPE, SimpleQuern.id("quern"), BlockEntityType.Builder.create(QuernBlockEntity::new, SimpleQuernBlocks.QUERN).build(null));

    public static void onInitialize() {  }
}
