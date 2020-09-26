package com.cumulus.simplequern.item;

import net.minecraft.item.Item;
import net.minecraft.item.ItemGroup;

public class BasicHandstone extends Item implements Handstone {
    private final int grindTier;

    public BasicHandstone(int grindTier, int durability) {
        super(new Item.Settings().maxDamage(durability).group(ItemGroup.TOOLS));
        this.grindTier = grindTier;
    }

    @Override
    public int grindTier() {
        return this.grindTier;
    }
}
