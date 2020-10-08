package com.cumulus.simplequern.screen;

import com.cumulus.simplequern.client.screen.slot.QuernGrindstoneSlot;
import com.cumulus.simplequern.client.screen.slot.QuernOutputSlot;
import com.cumulus.simplequern.init.SimpleQuernScreenHandlers;
import com.cumulus.simplequern.init.SimpleQuernRecipeTypes;
import com.cumulus.simplequern.item.Handstone;
import com.cumulus.simplequern.recipe.GrindingRecipe;
import com.cumulus.simplequern.recipe.QuernInputSlotFiller;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.inventory.Inventory;
import net.minecraft.inventory.SimpleInventory;
import net.minecraft.item.ItemStack;
import net.minecraft.recipe.Recipe;
import net.minecraft.recipe.RecipeFinder;
import net.minecraft.recipe.RecipeInputProvider;
import net.minecraft.recipe.RecipeType;
import net.minecraft.recipe.book.RecipeBookCategory;
import net.minecraft.screen.AbstractRecipeScreenHandler;
import net.minecraft.screen.ArrayPropertyDelegate;
import net.minecraft.screen.PropertyDelegate;
import net.minecraft.screen.slot.Slot;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.world.World;

public class QuernScreenHandler extends AbstractRecipeScreenHandler<Inventory> {
	private final Inventory inventory;
	private final PropertyDelegate propertyDelegate;
	private final World world;
	private final RecipeType<? extends GrindingRecipe> recipeType;

	public QuernScreenHandler(int syncId, PlayerInventory playerInventory) {
		this(syncId, playerInventory, new SimpleInventory(3), new ArrayPropertyDelegate(2));
	}

	public QuernScreenHandler(int syncId, PlayerInventory playerInventory, Inventory inventory, PropertyDelegate propertyDelegate) {
		super(SimpleQuernScreenHandlers.QUERN, syncId);
		this.recipeType = SimpleQuernRecipeTypes.GRINDING;

		checkSize(inventory, 3);
		checkDataCount(propertyDelegate, 2);

		this.inventory = inventory;
		this.propertyDelegate = propertyDelegate;
		this.world = playerInventory.player.world;

		this.addSlot(new Slot(inventory, 0, 56, 17));
		this.addSlot(new QuernGrindstoneSlot(this, inventory, 1, 56, 53));
		this.addSlot(new QuernOutputSlot(playerInventory.player, inventory, 2, 116, 35));

		int k;
		for(k = 0; k < 3; ++k) {
			for(int j = 0; j < 9; ++j) {
				this.addSlot(new Slot(playerInventory, j + k * 9 + 9, 8 + j * 18, 84 + k * 18));
			}
		}

		for(k = 0; k < 9; ++k) {
			this.addSlot(new Slot(playerInventory, k, 8 + k * 18, 142));
		}

		this.addProperties(propertyDelegate);
	}

	@Override
	public void populateRecipeFinder(RecipeFinder finder) {
		if (this.inventory instanceof RecipeInputProvider) {
			((RecipeInputProvider)this.inventory).provideRecipeInputs(finder);
		}
	}

	@Override
	public void clearCraftingSlots() {
		this.inventory.clear();
	}

	@Override
	public boolean matches(Recipe<? super Inventory> recipe) {
		return recipe.matches(this.inventory, this.world);
	}

	@Override
	public int getCraftingResultSlotIndex() {
		return 2;
	}

	@Override
	public int getCraftingWidth() {
		return 1;
	}

	@Override
	public int getCraftingHeight() {
		return 1;
	}

	@Override
	public int getCraftingSlotCount() {
		return 3;
	}

	@Override
	public RecipeBookCategory getCategory() {
		return null;
	}

	@Override
	public boolean canUse(PlayerEntity player) {
		return this.inventory.canPlayerUse(player);
	}

	public ItemStack transferSlot(PlayerEntity player, int index) {
		ItemStack itemStack = ItemStack.EMPTY;
		Slot slot = (Slot)this.slots.get(index);
		if (slot != null && slot.hasStack()) {
			ItemStack itemStack2 = slot.getStack();
			itemStack = itemStack2.copy();
			if (index == 2) {
				if (!this.insertItem(itemStack2, 3, 39, true)) {
					return ItemStack.EMPTY;
				}

				slot.onStackChanged(itemStack2, itemStack);
			} else if (index != 1 && index != 0) {
				if (this.isGrindable(itemStack2)) {
					if (!this.insertItem(itemStack2, 0, 1, false)) {
						return ItemStack.EMPTY;
					}
				} else if (this.isGrindStone(itemStack2)) {
					if (!this.insertItem(itemStack2, 1, 2, false)) {
						return ItemStack.EMPTY;
					}
				} else if (index >= 3 && index < 30) {
					if (!this.insertItem(itemStack2, 30, 39, false)) {
						return ItemStack.EMPTY;
					}
				} else if (index >= 30 && index < 39 && !this.insertItem(itemStack2, 3, 30, false)) {
					return ItemStack.EMPTY;
				}
			} else if (!this.insertItem(itemStack2, 3, 39, false)) {
				return ItemStack.EMPTY;
			}

			if (itemStack2.isEmpty()) {
				slot.setStack(ItemStack.EMPTY);
			} else {
				slot.markDirty();
			}

			if (itemStack2.getCount() == itemStack.getCount()) {
				return ItemStack.EMPTY;
			}

			slot.onTakeItem(player, itemStack2);
		}

		return itemStack;
	}

	public boolean isGrindable(ItemStack itemStack) {
		return this.world.getRecipeManager().getFirstMatch(this.recipeType, new SimpleInventory(itemStack), this.world).isPresent();
	}

	public boolean isGrindStone(ItemStack itemStack) {
		return itemStack.getItem() instanceof Handstone;
	}

	public int getGrindProgress() {
		return this.propertyDelegate.get(0);
	}

	public int getGrindRequired() {
		return this.propertyDelegate.get(1);
	}

	public float getGrindProportion() {
		return (float) getGrindProgress() / (float) getGrindRequired();
	}

	public void fillInputSlots(boolean bl, Recipe<?> recipe, ServerPlayerEntity serverPlayerEntity) {
		(new QuernInputSlotFiller(this)).fillInputSlots(serverPlayerEntity, recipe, bl);
	}
}
