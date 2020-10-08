package com.cumulus.simplequern.block.entity;

import com.cumulus.simplequern.init.SimpleQuernBlockEntityTypes;
import com.cumulus.simplequern.item.Handstone;
import com.cumulus.simplequern.recipe.GrindingRecipe;
import com.cumulus.simplequern.init.SimpleQuernRecipeTypes;
import com.cumulus.simplequern.screen.QuernScreenHandler;
import net.fabricmc.fabric.api.block.entity.BlockEntityClientSerializable;
import net.minecraft.block.BlockState;
import net.minecraft.block.entity.LockableContainerBlockEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.inventory.Inventories;
import net.minecraft.inventory.Inventory;
import net.minecraft.inventory.SidedInventory;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.recipe.RecipeFinder;
import net.minecraft.recipe.RecipeInputProvider;
import net.minecraft.screen.PropertyDelegate;
import net.minecraft.screen.ScreenHandler;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.sound.SoundCategory;
import net.minecraft.sound.SoundEvents;
import net.minecraft.text.Text;
import net.minecraft.text.TranslatableText;
import net.minecraft.util.Tickable;
import net.minecraft.util.collection.DefaultedList;
import net.minecraft.util.math.Direction;
import net.minecraft.util.math.MathHelper;

import java.util.Iterator;
import java.util.Random;

public class QuernBlockEntity extends LockableContainerBlockEntity implements Tickable, SidedInventory, RecipeInputProvider, BlockEntityClientSerializable {
    private DefaultedList<ItemStack> inventory;
    private int currentGrindTime;
    private int completeGrindTime = 1;
    private final PropertyDelegate propertyDelegate;

    private AnimationStage animationStage;

    private float animationProgress;
    private float prevAnimationProgress;

    private static final int[] TOP_SLOTS = new int[]{0};
    private static final int[] BOTTOM_SLOTS = new int[]{2};
    private static final int[] SIDE_SLOTS = new int[]{1};

    public QuernBlockEntity() {
        super(SimpleQuernBlockEntityTypes.QUERN);
        if (this.animationStage == null) {
            this.animationStage = AnimationStage.NORTH;
        }
        this.inventory = DefaultedList.ofSize(3, ItemStack.EMPTY);
        this.propertyDelegate = new PropertyDelegate() {
            public int get(int key) {
                switch(key) {
                    case 0:
                        return QuernBlockEntity.this.currentGrindTime;
                    case 1:
                        return QuernBlockEntity.this.completeGrindTime;
                    default:
                        return 0;
                }
            }

            public void set(int key, int value) {
                switch(key) {
                    case 0:
                        QuernBlockEntity.this.currentGrindTime = value;
                        break;
                    case 1:
                        QuernBlockEntity.this.completeGrindTime = value;
                        break;
                }

            }

            public int size() {
                return 2;
            }
        };
    }

    public void tick() {
        this.updateAnimation();
    }

    protected void updateAnimation() {
        this.prevAnimationProgress = this.animationProgress;
        switch(this.animationStage) {
            case TO_NORTH:
                this.animationProgress += 0.1F;
                if (this.animationProgress >= 4.0F) {
                    this.animationStage = AnimationStage.NORTH;
                    this.prevAnimationProgress -= 4.0F;
                    this.animationProgress = 0.0F;
                }
                break;
            case NORTH:
                this.animationProgress = 0.0F;
                break;
            case TO_EAST:
                this.animationProgress += 0.1F;
                if (this.animationProgress >= 1.0F) {
                    this.animationStage = AnimationStage.EAST;
                    this.animationProgress = 1.0F;
                }
                break;
            case EAST:
                this.animationProgress = 1.0F;
                break;
            case TO_SOUTH:
                this.animationProgress += 0.1F;
                if (this.animationProgress >= 2.0F) {
                    this.animationStage = AnimationStage.SOUTH;
                    this.animationProgress = 2.0F;
                }
                break;
            case SOUTH:
                this.animationProgress = 2.0F;
                break;
            case TO_WEST:
                this.animationProgress += 0.1F;
                if (this.animationProgress >= 3.0F) {
                    this.animationStage = AnimationStage.WEST;
                    this.animationProgress = 3.0F;
                }
                break;
            case WEST:
                this.animationProgress = 3.0F;
                break;
        }

    }

    public AnimationStage getAnimationStage() {
        return this.animationStage;
    }

    public ItemStack getOutputStack() {
        return this.getStack(2);
    }

    public ItemStack getInputStack() {
        return this.getStack(0);
    }

    public ItemStack getToolStack() {
        return this.getStack(1);
    }

    public boolean hasHandstone() {
        return this.getToolStack().getItem() instanceof Handstone;
    }

    @Override
    public void fromTag(BlockState state, CompoundTag tag) {
        super.fromTag(state, tag);
        this.inventory = DefaultedList.ofSize(this.size(), ItemStack.EMPTY);
        Inventories.fromTag(tag, this.inventory);
        this.currentGrindTime = tag.getShort("GrindTime");
        this.completeGrindTime = tag.getShort("GrindRequired");
        this.animationStage = AnimationStage.fromString(tag.getString("AnimationStage"));
        markDirty();
    }

    @Override
    public CompoundTag toTag(CompoundTag tag) {
        super.toTag(tag);
        Inventories.toTag(tag, this.inventory);
        tag.putShort("GrindTime", (short) this.currentGrindTime);
        tag.putShort("GrindRequired", (short) this.completeGrindTime);
        tag.putString("AnimationStage", this.animationStage.toString());
        return tag;
    }

    public boolean hasItemToGrind() {
        return !this.getInputStack().isEmpty();
    }

    public float getAnimationProgress(float f) {
        return MathHelper.lerp(f, this.prevAnimationProgress, this.animationProgress);
    }

    public boolean onSyncedBlockEvent(int type, int data) {
        if (type == 1) {
            if (data == 0) {
                this.animationStage = AnimationStage.TO_EAST;
            }

            if (data == 1) {
                this.animationStage = AnimationStage.TO_SOUTH;
            }

            if (data == 2) {
                this.animationStage = AnimationStage.TO_WEST;
            }

            if (data == 3) {
                this.animationStage = AnimationStage.TO_NORTH;
            }

            return true;
        } else {
            return super.onSyncedBlockEvent(type, data);
        }
    }

    public void triggerAnimation() {
        if (this.animationStage == AnimationStage.WEST) {
            this.world.addSyncedBlockEvent(this.pos, this.getCachedState().getBlock(), 1, 3);
        }
        if (this.animationStage == AnimationStage.SOUTH) {
            this.world.addSyncedBlockEvent(this.pos, this.getCachedState().getBlock(), 1, 2);
        }
        if (this.animationStage == AnimationStage.EAST) {
            this.world.addSyncedBlockEvent(this.pos, this.getCachedState().getBlock(), 1, 1);
        }
        if (this.animationStage == AnimationStage.NORTH) {
            this.world.addSyncedBlockEvent(this.pos, this.getCachedState().getBlock(), 1, 0);
        }
    }

    public void activate(PlayerEntity player) {
        if (this.animationProgress % 1.0 == 0.0) {
            boolean dirty = false;

            GrindingRecipe recipe = this.getWorld().getRecipeManager().getFirstMatch(SimpleQuernRecipeTypes.GRINDING, this, this.world).orElse(null);
            if (this.canAcceptRecipeOutput(recipe)) {
                this.triggerAnimation();
                if (!this.getWorld().isClient) {
                    this.currentGrindTime++;

                    if (this.currentGrindTime > recipe.getGrindTime()) {
                        this.inventory.get(1).damage(1, player, (pp) -> {
                        });
                        this.currentGrindTime = 0;
                        this.craftRecipe(recipe);
                        world.playSound(null, this.pos, SoundEvents.BLOCK_GRINDSTONE_USE, SoundCategory.BLOCKS, 1f, 1f);
                        dirty = true;
                    }
                }
            }

            if (dirty) {
                markDirty();
                sync();
            }
        }
    }

    protected boolean canAcceptRecipeOutput(GrindingRecipe recipe) {
        ItemStack toolStack = this.inventory.get(1);
        ItemStack inputStack = this.inventory.get(0);
        if (!inputStack.isEmpty() && recipe != null) {
            ItemStack result = recipe.getOutput(toolStack, (ServerWorld) this.world).get(0);
            if (result.isEmpty()) {
                return false;
            } else {
                ItemStack outputStack = this.inventory.get(2);
                if (outputStack.isEmpty()) {
                    return true;
                } else if (!outputStack.isItemEqualIgnoreDamage(result)) {
                    return false;
                } else if (outputStack.getCount() < this.getMaxCountPerStack() && outputStack.getCount() < outputStack.getMaxCount()) {
                    return true;
                } else {
                    return outputStack.getCount() < result.getMaxCount();
                }
            }
        } else {
            return false;
        }
    }

    private void craftRecipe(GrindingRecipe recipe) {
        if (recipe != null && this.canAcceptRecipeOutput(recipe)) {
            ItemStack input = this.inventory.get(0);
            ItemStack outputStack = this.inventory.get(2);
            ItemStack tool = this.inventory.get(1);

            ItemStack result = recipe.getOutput(tool, (ServerWorld) this.world).get(0);

            if (outputStack.isEmpty()) {
                this.inventory.set(2, result.copy());
            } else if (outputStack.getItem() == result.getItem()) {
                outputStack.increment(result.getCount());
            }

            tool.damage(1, new Random(), null);
            input.decrement(1);

            if (tool.getDamage() >= tool.getMaxDamage()) {
                tool.decrement(1);
            }

            markDirty();
            sync();
        }
    }

    @Override
    public int[] getAvailableSlots(Direction side) {
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
    public boolean canInsert(int slot, ItemStack stack, Direction dir) {
        return this.isValid(slot, stack);
    }

    @Override
    public boolean canExtract(int slot, ItemStack stack, Direction dir) {
        return true;
    }

    @Override
    public int size() {
        return this.inventory.size();
    }

    @Override
    public boolean isEmpty() {
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
    public ItemStack getStack(int slot) {
        return this.inventory.get(slot);
    }

    @Override
    public ItemStack removeStack(int slot, int amount) {
        updateState();
        markDirty();
        sync();
        return Inventories.splitStack(this.inventory, slot, amount);
    }

    @Override
    public ItemStack removeStack(int slot) {
        updateState();
        markDirty();
        sync();
        return Inventories.removeStack(this.inventory, slot);
    }

    @Override
    // TODO: Revisit this
    public void setStack(int slot, ItemStack stack) {
        this.inventory.set(slot, stack);
        if (stack.getCount() > this.getMaxCountPerStack()) {
            stack.setCount(this.getMaxCountPerStack());
        }
        updateState();
        markDirty();
        sync();
    }

    public void updateState() {
        if (inventory.get(0).isEmpty()) {
            this.currentGrindTime = 0;
        }

        GrindingRecipe recipe = this.getWorld().getRecipeManager().getFirstMatch(SimpleQuernRecipeTypes.GRINDING, this, this.world).orElse(null);
        this.completeGrindTime = recipe != null ? recipe.getGrindTime() : this.completeGrindTime;
    }

    @Override
    public boolean canPlayerUse(PlayerEntity player) {
        if (this.world.getBlockEntity(this.pos) != this) {
            return false;
        } else {
            return player.squaredDistanceTo((double)this.pos.getX() + 0.5D, (double)this.pos.getY() + 0.5D, (double)this.pos.getZ() + 0.5D) <= 64.0D;
        }
    }

    @Override
    public boolean isValid(int slot, ItemStack stack) {
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
        sync();
    }

    @Override
    protected Text getContainerName() {
        return new TranslatableText("container.quern");
    }

    @Override
    protected ScreenHandler createScreenHandler(int i, PlayerInventory playerInventory) {
        return new QuernScreenHandler(i, playerInventory, this, this.propertyDelegate);
    }

    @Override
    public ScreenHandler createMenu(int i, PlayerInventory playerInventory, PlayerEntity playerEntity) {
        return new QuernScreenHandler(i, playerInventory, this, this.propertyDelegate);
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

    @Override
    public void fromClientTag(CompoundTag tag) {
        fromTag(null, tag);
    }

    @Override
    public CompoundTag toClientTag(CompoundTag tag) {
        toTag(tag);
        return tag;
    }

    public static enum AnimationStage {
        NORTH,
        TO_EAST,
        EAST,
        TO_SOUTH,
        SOUTH,
        TO_WEST,
        WEST,
        TO_NORTH;

        @Override
        public String toString() {
            switch (this) {
                case NORTH: return "NORTH";
                case TO_EAST: return "TO_EAST";
                case EAST: return "EAST";
                case TO_SOUTH: return "TO_SOUTH";
                case SOUTH: return "SOUTH";
                case TO_WEST: return "TO_WEST";
                case WEST: return "WEST";
                case TO_NORTH: return "TO_NORTH";
            }
            return "";
        }

        public static AnimationStage fromString(String s) {
            if (s.toLowerCase().equals("north")) return NORTH;
            if (s.toLowerCase().equals("to_east")) return TO_EAST;
            if (s.toLowerCase().equals("east")) return EAST;
            if (s.toLowerCase().equals("to_south")) return TO_SOUTH;
            if (s.toLowerCase().equals("south")) return SOUTH;
            if (s.toLowerCase().equals("to_west")) return TO_WEST;
            if (s.toLowerCase().equals("west")) return WEST;
            if (s.toLowerCase().equals("to_north")) return TO_NORTH;
            return NORTH;
        }
    }
}
