package com.wtoll.simplequern.item;

import net.minecraft.item.Item;
import net.minecraft.item.ItemGroup;

public class ConcreteHandstone extends Item implements Handstone {
    private final int grindLevel;
    private final HandstoneEnum property;

    public ConcreteHandstone(int grindLevel, int durability, HandstoneEnum property) {
        super(new Item.Settings().maxDamage(durability).group(ItemGroup.TOOLS));
        this.grindLevel = grindLevel;
        this.property = property;
    }

    @Override
    public int grindLevel() {
        return this.grindLevel;
    }

    @Override
    public HandstoneEnum property()
    {
        return this.property;
    }
}
