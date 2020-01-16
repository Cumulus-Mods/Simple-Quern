package com.wtoll.simplequern.block;

import com.wtoll.simplequern.Utility;
import net.minecraft.block.Block;
import net.minecraft.item.BlockItem;
import net.minecraft.item.Item;
import net.minecraft.item.ItemGroup;
import net.minecraft.util.Identifier;
import net.minecraft.util.registry.Registry;

public class Blocks {
    public static final Block QUERN;

    static {
        QUERN = register(new QuernBlock(), Utility.id("quern"), new Item.Settings().group(ItemGroup.DECORATIONS));
    }

    public static Block register(Block b, Identifier id, Item.Settings settings) {
        Registry.register(Registry.ITEM, id, new BlockItem(b, settings));
        return Registry.register(Registry.BLOCK, id, b);
    }

    public static void initialize() {  }
}
