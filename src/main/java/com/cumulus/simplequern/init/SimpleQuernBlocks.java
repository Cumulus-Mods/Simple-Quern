package com.cumulus.simplequern.init;

import com.cumulus.simplequern.SimpleQuern;
import com.cumulus.simplequern.block.QuernBlock;
import net.minecraft.block.Block;
import net.minecraft.item.BlockItem;
import net.minecraft.item.Item;
import net.minecraft.item.ItemGroup;
import net.minecraft.util.Identifier;
import net.minecraft.util.registry.Registry;

public class SimpleQuernBlocks {
    public static final Block QUERN = register(new QuernBlock(), SimpleQuern.id("quern"), new Item.Settings().group(ItemGroup.DECORATIONS));

    public static Block register(Block b, Identifier id, Item.Settings settings) {
        Registry.register(Registry.ITEM, id, new BlockItem(b, settings));
        return Registry.register(Registry.BLOCK, id, b);
    }

    public static void onInitialize() {  }
}
