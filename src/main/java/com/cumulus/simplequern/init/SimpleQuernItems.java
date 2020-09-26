package com.cumulus.simplequern.init;

import com.cumulus.simplequern.SimpleQuern;
import com.cumulus.simplequern.item.BasicHandstone;
import net.minecraft.item.Item;
import net.minecraft.item.ItemGroup;
import net.minecraft.util.registry.Registry;

public class SimpleQuernItems {
    public static final Item HANDSTONE = register(new BasicHandstone(1, 100), "handstone");
    public static final Item DIAMOND_HANDSTONE = register(new BasicHandstone(2, 100), "diamond_handstone");
    public static final Item OBSIDIAN_HANDSTONE = register(new BasicHandstone(3, 100), "obsidian_handstone");

    public static final Item IRON_DUST = register(new Item(new Item.Settings().group(ItemGroup.MATERIALS)), "iron_dust");
    public static final Item GOLD_DUST = register(new Item(new Item.Settings().group(ItemGroup.MATERIALS)), "gold_dust");

    public static Item register(Item i, String s) {
        return Registry.register(Registry.ITEM, SimpleQuern.id(s), i);
    }

    public static void onInitialize() {  }
}
