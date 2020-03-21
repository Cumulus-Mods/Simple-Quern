package com.wtoll.simplequern.block.entity;

import com.wtoll.simplequern.block.QuernBlock;
import com.wtoll.simplequern.container.QuernContainer;
import com.wtoll.simplequern.item.Handstone;
import com.wtoll.simplequern.recipe.GrindingRecipe;
import com.wtoll.simplequern.recipe.RecipeType;
import net.minecraft.block.BlockState;
import net.minecraft.block.entity.LockableContainerBlockEntity;
import net.minecraft.container.Container;
import net.minecraft.container.PropertyDelegate;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.inventory.Inventories;
import net.minecraft.inventory.SidedInventory;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.recipe.Recipe;
import net.minecraft.recipe.RecipeFinder;
import net.minecraft.recipe.RecipeInputProvider;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.sound.SoundCategory;
import net.minecraft.sound.SoundEvents;
import net.minecraft.text.Text;
import net.minecraft.text.TranslatableText;
import net.minecraft.util.BlockRotation;
import net.minecraft.util.DefaultedList;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Direction;
import net.minecraft.world.World;

import java.util.Iterator;
import java.util.Random;

public class QuernBlockEntity extends LockableContainerBlockEntity implements SidedInventory, RecipeInputProvider {
    private DefaultedList<ItemStack> inventory;
    private int grindTime;
    private int grindRequired = 1;
    private final PropertyDelegate propertyDelegate;

    private static final int[] TOP_SLOTS = new int[]{0};
    private static final int[] BOTTOM_SLOTS = new int[]{2};
    private static final int[] SIDE_SLOTS = new int[]{1};

    public QuernBlockEntity() {
        super(BlockEntityType.QUERN);
        this.inventory = DefaultedList.ofSize(3, ItemStack.EMPTY);
        this.propertyDelegate = new PropertyDelegate() {
            public int get(int key) {
                switch(key) {
                    case 0:
                        return QuernBlockEntity.this.grindTime;
                    case 1:
                        return QuernBlockEntity.this.grindRequired;
                    default:
                        return 0;
                }
            }

            public void set(int key, int value) {
                switch(key) {
                    case 0:
                        QuernBlockEntity.this.grindTime = value;
                        break;
                    case 1:
                        QuernBlockEntity.this.grindRequired = value;
                        break;
                }

            }

            public int size() {
                return 2;
            }
        };
    }

    @Override
    public void fromTag(CompoundTag tag) {
        super.fromTag(tag);
        this.inventory = DefaultedList.ofSize(this.getInvSize(), ItemStack.EMPTY);
        Inventories.fromTag(tag, this.inventory);
        this.grindTime = tag.getShort("GrindTime");
        this.grindRequired = tag.getShort("GrindRequired");
        markDirty();
    }

    @Override
    // TODO: Revisit this method
    public CompoundTag toTag(CompoundTag tag) {
        super.toTag(tag);
        Inventories.toTag(tag, this.inventory);
        tag.putShort("GrindTime", (short) this.grindTime);
        tag.putShort("GrindRequired", (short) this.grindRequired);
        return tag;
    }

    public boolean hasItemToGrind() {
        return !this.inventory.get(0).isEmpty();
    }

    public void activate(PlayerEntity player) {
        boolean dirty = false;
        if (!this.getWorld().isClient) {
            GrindingRecipe recipe = this.getWorld().getRecipeManager().getFirstMatch(RecipeType.GRINDING, this, this.world).orElse(null);
            if (this.canAcceptRecipeOutput(recipe)) {
                this.grindTime++;

                rotateClockwise(world, world.getBlockState(pos), pos);

                if (this.grindTime > recipe.getGrindTime()) {
                    this.inventory.get(1).damage(1, player, (pp) -> {
                    });
                    this.grindTime = 0;
                    this.craftRecipe(recipe);
                    world.playSound(null, this.pos, SoundEvents.BLOCK_GRINDSTONE_USE, SoundCategory.BLOCKS, 1f, 1f);
                    dirty = true;
                }
            }
        }
        if (dirty) markDirty();
    }

    private void rotateClockwise(World world, BlockState state, BlockPos pos) {
        if (!state.rotate(BlockRotation.CLOCKWISE_90).canPlaceAt(world, pos)) return;
        world.setBlockState(pos, state.rotate(BlockRotation.CLOCKWISE_90));
        world.updateNeighbor(pos, state.getBlock(), pos);
    }

    protected boolean canAcceptRecipeOutput(Recipe<?> recipe) {
        if (!(this.inventory.get(0)).isEmpty() && recipe != null) {
            ItemStack result = recipe.getOutput();
            if (result.isEmpty()) {
                return false;
            } else {
                ItemStack outputStack = this.inventory.get(2);
                if (outputStack.isEmpty()) {
                    return true;
                } else if (!outputStack.isItemEqualIgnoreDamage(result)) {
                    return false;
                } else if (outputStack.getCount() < this.getInvMaxStackAmount() && outputStack.getCount() < outputStack.getMaxCount()) {
                    return true;
                } else {
                    return outputStack.getCount() < result.getMaxCount();
                }
            }
        } else {
            return false;
        }
    }

    private void craftRecipe(Recipe<?> recipe) {
        if (recipe != null && this.canAcceptRecipeOutput(recipe)) {
            ItemStack input = this.inventory.get(0);
            ItemStack result = recipe.getOutput();
            ItemStack outputStack = this.inventory.get(2);
            ItemStack tool = this.inventory.get(1);

            if (outputStack.isEmpty()) {
                this.inventory.set(2, result.copy());
            } else if (outputStack.getItem() == result.getItem()) {
                outputStack.increment(result.getCount());
            }

            tool.damage(1, new Random(), null);
            input.decrement(1);

            if (tool.getDamage() >= tool.getMaxDamage()) {
                tool.decrement(1);
                //when handstone breaks, play the tool break sound
                world.playSound(null, pos, SoundEvents.ITEM_SHIELD_BREAK, SoundCategory.BLOCKS, 1.0f, 1.0f);
                updateState();
            }

            markDirty();
        }
    }

    @Override
    public int[] getInvAvailableSlots(Direction side) {
        switch (side) {
            case UP:
                return TOP_SLOTS;
            case DOWN:
                return BOTTOM_SLOTS;
            default:
                return SIDE_SLOTS;
        }
    }

    @Override
    public boolean canInsertInvStack(int slot, ItemStack stack, Direction dir) {
        return this.isValidInvStack(slot, stack);
    }

    @Override
    public boolean canExtractInvStack(int slot, ItemStack stack, Direction dir) {
        return true;
    }

    @Override
    public int getInvSize() {
        return this.inventory.size();
    }

    @Override
    public boolean isInvEmpty() {
        Iterator i = this.inventory.iterator();
        ItemStack stack;
        while(i.hasNext()) {
            stack = (ItemStack) i.next();
            if (!stack.isEmpty()) {
                return false;
            }
        }
        return true;
    }

    @Override
    public ItemStack getInvStack(int slot) {
        return this.inventory.get(slot);
    }

    @Override
    public ItemStack takeInvStack(int slot, int amount) {
        markDirty();
        updateState();
        return Inventories.splitStack(this.inventory, slot, amount);
    }

    @Override
    public ItemStack removeInvStack(int slot) {
        markDirty();
        updateState();
        return Inventories.removeStack(this.inventory, slot);
    }

    @Override
    // TODO: Revisit this
    public void setInvStack(int slot, ItemStack stack) {
        this.inventory.set(slot, stack);
        if (stack.getCount() > this.getInvMaxStackAmount()) {
            stack.setCount(this.getInvMaxStackAmount());
        }
        updateState();
        markDirty();
    }

    public void updateState() {
        if(inventory.get(1).getItem() instanceof Handstone) { //set block state to handstone of correct grind level
            world.setBlockState(pos, world.getBlockState(pos).with(QuernBlock.HANDSTONE, ((Handstone) inventory.get(1).getItem()).grindLevel()));
        }
        else { //set block state to no handstone
            world.setBlockState(pos, world.getBlockState(pos).with(QuernBlock.HANDSTONE, 0));
            this.grindTime = 0;
        }

        if (inventory.get(0).isEmpty()) {
            this.grindTime = 0;
        }

        GrindingRecipe recipe = this.getWorld().getRecipeManager().getFirstMatch(RecipeType.GRINDING, this, this.world).orElse(null);
        this.grindRequired = recipe != null ? recipe.getGrindTime() : 1;
    }

    @Override
    public boolean canPlayerUseInv(PlayerEntity player) {
        if (this.world.getBlockEntity(this.pos) != this) {
            return false;
        } else {
            return player.squaredDistanceTo((double)this.pos.getX() + 0.5D, (double)this.pos.getY() + 0.5D, (double)this.pos.getZ() + 0.5D) <= 64.0D;
        }
    }

    @Override
    public boolean isValidInvStack(int slot, ItemStack stack) {
        switch (slot) {
            case 0:
                return true;
            case 1:
                return inventory.get(1).getItem() instanceof Handstone;
            case 2:
                return false;
            default:
                return false;
        }
    }

    @Override
    public void clear() {
        this.inventory.clear();
        markDirty();
    }

    @Override
    protected Text getContainerName() {
        return new TranslatableText("container.quern");
    }

    @Override
    protected Container createContainer(int i, PlayerInventory playerInventory) {
        return new QuernContainer(i, playerInventory, this.pos);
    }

    @Override
    public void provideRecipeInputs(RecipeFinder recipeFinder) {
        for (ItemStack itemStack : this.inventory) {
            recipeFinder.addItem(itemStack);
        }
    }

    public PropertyDelegate getPropertyDelegate() {
        return this.propertyDelegate;
    }
}
