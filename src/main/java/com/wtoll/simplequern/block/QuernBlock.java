package com.wtoll.simplequern.block;

import com.wtoll.simplequern.block.entity.QuernBlockEntity;
import com.wtoll.simplequern.container.Containers;
import net.fabricmc.fabric.api.block.FabricBlockSettings;
import net.fabricmc.fabric.api.container.ContainerProviderRegistry;
import net.minecraft.block.*;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.container.Container;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.stat.Stats;
import net.minecraft.state.StateManager;
import net.minecraft.state.property.BooleanProperty;
import net.minecraft.util.ActionResult;
import net.minecraft.util.Hand;
import net.minecraft.util.ItemScatterer;
import net.minecraft.util.hit.BlockHitResult;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Direction;
import net.minecraft.world.BlockView;
import net.minecraft.world.World;

import javax.annotation.Nullable;

public class QuernBlock extends BlockWithEntity {

    public static final BooleanProperty HANDSTONE;
    //TODO: Maybe add a thing for how rotated the handstone is that way it looks like its going around?

    public QuernBlock() {
        super(FabricBlockSettings.of(Material.STONE).nonOpaque().build());
        this.setDefaultState(this.stateManager.getDefaultState().with(HANDSTONE, false));
    }

    @Nullable
    @Override
    public BlockEntity createBlockEntity(BlockView view) {
        return new QuernBlockEntity();
    }

    @Override
    @SuppressWarnings("deprecation")
    public ActionResult onUse(BlockState state, World world, BlockPos pos, PlayerEntity player, Hand hand, BlockHitResult hit) {
        if (!world.isClient) {
            if (hit.getSide() == Direction.UP && state.get(HANDSTONE)) {
                ((QuernBlockEntity) world.getBlockEntity(pos)).activate(player);
            } else {
                openContainer(world, pos, player);
            }
        }
        return ActionResult.SUCCESS;
    }

    private void openContainer(World world, BlockPos pos, PlayerEntity player) {
        ContainerProviderRegistry.INSTANCE.openContainer(Containers.QUERN, player, (buffer) -> buffer.writeBlockPos(pos));
        player.incrementStat(Stats.INTERACT_WITH_FURNACE);
    }

    @Override
    public void onPlaced(World world, BlockPos pos, BlockState state, LivingEntity placer, ItemStack itemStack) {
        if (itemStack.hasCustomName()) {
            BlockEntity blockEntity = world.getBlockEntity(pos);
            if (blockEntity instanceof QuernBlockEntity) {
                ((QuernBlockEntity) blockEntity).setCustomName(itemStack.getName());
            }
        }
    }

    @Override
    @SuppressWarnings("deprecation")
    public void onBlockRemoved(BlockState state, World world, BlockPos pos, BlockState newState, boolean moved) {
        if (state.getBlock() != newState.getBlock()) {
            BlockEntity blockEntity = world.getBlockEntity(pos);
            if (blockEntity instanceof QuernBlockEntity) {
                ItemScatterer.spawn(world, pos, (QuernBlockEntity) blockEntity);
                world.updateHorizontalAdjacent(pos, this);
            }
            super.onBlockRemoved(state, world, pos, newState, moved);
        }
    }

    @Override
    @SuppressWarnings("deprecation")
    public boolean hasComparatorOutput(BlockState state) {
        return true;
    }

    @Override
    @SuppressWarnings("deprecation")
    public int getComparatorOutput(BlockState state, World world, BlockPos pos) {
        return Container.calculateComparatorOutput(world.getBlockEntity(pos));
    }

    @Override
    public BlockRenderType getRenderType(BlockState state) {
        return BlockRenderType.MODEL;
    }

    @Override
    protected void appendProperties(StateManager.Builder<Block, BlockState> builder) {
        builder.add(HANDSTONE);
    }

    static {
        HANDSTONE =  BooleanProperty.of("handstone");
    }
}
