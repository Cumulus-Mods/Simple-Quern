package com.cumulus.simplequern.item;

import net.minecraft.item.Item;
import net.minecraft.item.ItemGroup;

public class ConcreteHandstone extends Item implements Handstone {
    private final int grindLevel;

    public ConcreteHandstone(int grindLevel, int durability) {
        super(new Item.Settings().maxDamage(durability).group(ItemGroup.TOOLS));
        this.grindLevel = grindLevel;
    }

    @Override
    public int grindLevel() {
        return this.grindLevel;
    }
}
