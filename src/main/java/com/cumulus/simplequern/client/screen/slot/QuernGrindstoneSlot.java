package com.cumulus.simplequern.client.screen.slot;

import com.cumulus.simplequern.screen.QuernScreenHandler;
import net.minecraft.inventory.Inventory;
import net.minecraft.item.ItemStack;
import net.minecraft.screen.slot.Slot;

public class QuernGrindstoneSlot extends Slot {
	private final QuernScreenHandler handler;

	public QuernGrindstoneSlot(QuernScreenHandler handler, Inventory inventory, int index, int x, int y) {
		super(inventory, index, x, y);
		this.handler = handler;
	}

	public boolean canInsert(ItemStack stack) {
		return this.handler.isGrindStone(stack);
	}
}
