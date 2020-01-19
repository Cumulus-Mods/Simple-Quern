package com.wtoll.simplequern.item;

import com.wtoll.simplequern.Utility;
import net.minecraft.item.Item;
import net.minecraft.item.ItemGroup;
import net.minecraft.util.registry.Registry;

public class Items {
    public static final Item HANDSTONE;
    public static final Item DIAMOND_HANDSTONE;
    public static final Item OBSIDIAN_HANDSTONE;
    public static final Item IRON_DUST;
    public static final Item GOLD_DUST;

    static {
        HANDSTONE = register(new ConcreteHandstone(1, 100), "handstone");
        DIAMOND_HANDSTONE = register(new ConcreteHandstone(2, 100), "diamond_handstone");
        OBSIDIAN_HANDSTONE = register(new ConcreteHandstone(3, 100), "obsidian_handstone");
        IRON_DUST = register(new Item(new Item.Settings().group(ItemGroup.MATERIALS)), "iron_dust");
        GOLD_DUST = register(new Item(new Item.Settings().group(ItemGroup.MATERIALS)), "gold_dust");
    }

    public static Item register(Item i, String s) {
        return Registry.register(Registry.ITEM, Utility.id(s), i);
    }

    public static void initialize() {  }
}
