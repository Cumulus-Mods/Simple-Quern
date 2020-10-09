package com.cumulus.simplequern.block;

import com.cumulus.simplequern.block.entity.QuernBlockEntity;
import net.fabricmc.fabric.api.object.builder.v1.block.FabricBlockSettings;
import net.fabricmc.fabric.api.tools.FabricToolTags;
import net.minecraft.block.*;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemPlacementContext;
import net.minecraft.item.ItemStack;
import net.minecraft.screen.NamedScreenHandlerFactory;
import net.minecraft.screen.ScreenHandler;
import net.minecraft.sound.BlockSoundGroup;
import net.minecraft.stat.Stats;
import net.minecraft.state.StateManager;
import net.minecraft.state.property.BooleanProperty;
import net.minecraft.state.property.DirectionProperty;
import net.minecraft.util.ActionResult;
import net.minecraft.util.BlockRotation;
import net.minecraft.util.Hand;
import net.minecraft.util.ItemScatterer;
import net.minecraft.util.hit.BlockHitResult;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Direction;
import net.minecraft.util.shape.VoxelShape;
import net.minecraft.util.shape.VoxelShapes;
import net.minecraft.world.BlockView;
import net.minecraft.world.World;

public class QuernBlock extends BlockWithEntity {

    private static final VoxelShape EMPTY = VoxelShapes.union(
            Block.createCuboidShape(0, 0, 0, 16, 8, 16),
            Block.createCuboidShape(6, 8, 6, 10, 13, 10));

    private static final VoxelShape OCCUPIED = Block.createCuboidShape(0, 0, 0, 16, 14, 16);

    public QuernBlock() {
        super(FabricBlockSettings.of(Material.STONE).nonOpaque().strength(3.5f, 3.5f).breakByTool(FabricToolTags.PICKAXES).sounds(BlockSoundGroup.STONE));
    }

    @Override
    public BlockEntity createBlockEntity(BlockView view) {
        return new QuernBlockEntity();
    }

    @SuppressWarnings("deprecation")
    @Override
    public ActionResult onUse(BlockState state, World world, BlockPos pos, PlayerEntity player, Hand hand, BlockHitResult hit) {
        QuernBlockEntity entity = (QuernBlockEntity) world.getBlockEntity(pos);
        if (!world.isClient) {
            if (hit.getSide() == Direction.UP && entity.hasHandstone() && entity.hasItemToGrind()) {
                ((QuernBlockEntity) world.getBlockEntity(pos)).activate(player);
            } else {
                openContainer(world, pos, player);
            }
        }
        return ActionResult.SUCCESS;
    }

    private void openContainer(World world, BlockPos pos, PlayerEntity player) {
        BlockEntity blockEntity = world.getBlockEntity(pos);
        if (blockEntity instanceof QuernBlockEntity)
        {
            player.openHandledScreen((NamedScreenHandlerFactory) blockEntity);
            player.incrementStat(Stats.INTERACT_WITH_FURNACE);
        }
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

    @SuppressWarnings("deprecation")
    @Override
    public void onStateReplaced(BlockState state, World world, BlockPos pos, BlockState newState, boolean moved) {
        if (state.getBlock() != newState.getBlock()) {
            BlockEntity blockEntity = world.getBlockEntity(pos);
            if (blockEntity instanceof QuernBlockEntity) {
                ItemScatterer.spawn(world, pos, (QuernBlockEntity) blockEntity);
                world.updateNeighbors(pos, this);
            }
            super.onStateReplaced(state, world, pos, newState, moved);
        }
    }

    @SuppressWarnings("deprecation")
    @Override
    public boolean hasComparatorOutput(BlockState state) {
        return true;
    }

    @SuppressWarnings("deprecation")
    @Override
    public int getComparatorOutput(BlockState state, World world, BlockPos pos) {
        return ScreenHandler.calculateComparatorOutput(world.getBlockEntity(pos));
    }

    @SuppressWarnings("deprecation")
    @Override
    public VoxelShape getOutlineShape(BlockState state, BlockView world, BlockPos pos, ShapeContext context) {
        return (world.getBlockEntity(pos) != null && ((QuernBlockEntity) world.getBlockEntity(pos)).hasHandstone()) ? OCCUPIED : EMPTY;
    }

}
