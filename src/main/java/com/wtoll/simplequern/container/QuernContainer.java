package com.wtoll.simplequern.container;

import com.wtoll.simplequern.block.entity.BlockEntityType;
import com.wtoll.simplequern.block.entity.QuernBlockEntity;
import com.wtoll.simplequern.recipe.QuernInputSlotFiller;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.container.*;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.inventory.BasicInventory;
import net.minecraft.inventory.Inventory;
import net.minecraft.item.ItemStack;
import net.minecraft.recipe.Recipe;
import net.minecraft.recipe.RecipeFinder;
import net.minecraft.recipe.RecipeInputProvider;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

//TODO: Finalize
public class QuernContainer extends CraftingContainer<Inventory> {
    private final Inventory inventory;
    private final PropertyDelegate propertyDelegate;
    protected final World world;
    private final BlockPos pos;

    public QuernContainer(int syncId, PlayerInventory playerInventory) {
        this(syncId, playerInventory, BlockPos.ORIGIN);
    }

    public QuernContainer(int syncId, PlayerInventory playerInventory, BlockPos pos) {
        super(null, syncId);
        this.world = playerInventory.player.world;
        BlockEntity entity = world.getBlockEntity(pos);
        this.pos = pos;
        if (entity != null && entity instanceof QuernBlockEntity) {
            this.inventory = ((QuernBlockEntity) entity);
            this.propertyDelegate = ((QuernBlockEntity) entity).getPropertyDelegate();
        } else {
            this.inventory = new BasicInventory(3);
            this.propertyDelegate = new ArrayPropertyDelegate(4);
        }

        checkContainerSize(inventory, 3);
        checkContainerDataCount(propertyDelegate, 1);

        this.addSlot(new Slot(inventory, 0, 56, 17));
        this.addSlot(new QuernHandstoneSlot(this, inventory, 1, 56, 53));
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

    public int getGrindProgress() {
        return this.propertyDelegate.get(0);
    }

    public int getGrindRequired() {
        return this.propertyDelegate.get(1);
    }

    public float getGrindProportion() {
        return (float) getGrindProgress() / (float) getGrindRequired();
    }

    public void populateRecipeFinder(RecipeFinder recipeFinder) {
        if (this.inventory instanceof RecipeInputProvider) {
            ((RecipeInputProvider) this.inventory).provideRecipeInputs(recipeFinder);
        }

    }

    public void clearCraftingSlots() {
        this.inventory.clear();
    }

    public void fillInputSlots(boolean bl, Recipe<?> recipe, ServerPlayerEntity serverPlayerEntity) {
        (new QuernInputSlotFiller(this)).fillInputSlots(serverPlayerEntity, recipe, bl);
    }

    public boolean matches(Recipe<? super Inventory> recipe) {
        return recipe.matches(this.inventory, this.world);
    }

    public int getCraftingResultSlotIndex() {
        return 2;
    }

    public int getCraftingWidth() {
        return 1;
    }

    public int getCraftingHeight() {
        return 1;
    }

    @Environment(EnvType.CLIENT)
    public int getCraftingSlotCount() {
        return 3;
    }

    public boolean canUse(PlayerEntity player) {
        return this.inventory.canPlayerUseInv(player);
    }

    public ItemStack transferSlot(PlayerEntity player, int invSlot) {
        ItemStack itemStack = ItemStack.EMPTY;
        Slot slot = (Slot)this.slots.get(invSlot);
        if (slot != null && slot.hasStack()) {
            ItemStack itemStack2 = slot.getStack();
            itemStack = itemStack2.copy();
            if (invSlot == 2) {
                if (!this.insertItem(itemStack2, 3, 39, true)) {
                    return ItemStack.EMPTY;
                }

                slot.onStackChanged(itemStack2, itemStack);
            } else if (invSlot != 1 && invSlot != 0) {
                if (invSlot >= 3 && invSlot < 30) {
                    if (!this.insertItem(itemStack2, 30, 39, false)) {
                        return ItemStack.EMPTY;
                    }
                } else if (invSlot >= 30 && invSlot < 39 && !this.insertItem(itemStack2, 3, 30, false)) {
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
}
