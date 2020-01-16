package com.wtoll.simplequern.container;

import com.wtoll.simplequern.item.Handstone;
import net.minecraft.container.Slot;
import net.minecraft.inventory.Inventory;
import net.minecraft.item.ItemStack;


// TODO: Finalize
public class QuernHandstoneSlot extends Slot {
    private final QuernContainer container;

    public QuernHandstoneSlot(QuernContainer abstractFurnaceContainer, Inventory inventory, int invSlot, int xPosition, int yPosition) {
        super(inventory, invSlot, xPosition, yPosition);
        this.container = abstractFurnaceContainer;
    }

    public boolean canInsert(ItemStack stack) {
        return stack.getItem() instanceof Handstone;
    }
}
