package com.wtoll.simplequern.block;

import com.wtoll.simplequern.block.entity.QuernBlockEntity;
import com.wtoll.simplequern.container.Containers;
import net.fabricmc.fabric.api.block.FabricBlockSettings;
import net.fabricmc.fabric.api.container.ContainerProviderRegistry;
import net.fabricmc.fabric.api.tools.FabricToolTags;
import net.minecraft.block.*;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.container.Container;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemPlacementContext;
import net.minecraft.item.ItemStack;
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
import net.minecraft.world.BlockView;
import net.minecraft.world.World;

public class QuernBlock extends BlockWithEntity {

    public static final BooleanProperty HANDSTONE;
    public static final DirectionProperty FACING;

    public QuernBlock() {
        super(FabricBlockSettings.of(Material.STONE).nonOpaque().strength(3.5f, 3.5f).breakByTool(FabricToolTags.PICKAXES).sounds(BlockSoundGroup.STONE).build());
        this.setDefaultState(this.stateManager.getDefaultState().with(HANDSTONE, false).with(FACING, Direction.NORTH));
    }

    @Override
    public BlockEntity createBlockEntity(BlockView view) {
        return new QuernBlockEntity();
    }

    @Override
    @SuppressWarnings("deprecation")
    public ActionResult onUse(BlockState state, World world, BlockPos pos, PlayerEntity player, Hand hand, BlockHitResult hit) {
        if (!world.isClient) {
            if (hit.getSide() == Direction.UP && state.get(HANDSTONE) && ((QuernBlockEntity) world.getBlockEntity(pos)).hasItemToGrind()) {
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
    public BlockState getPlacementState(ItemPlacementContext ctx) {
        return getDefaultState().with(FACING, ctx.getPlayerFacing().getOpposite());
    }

    @Override
    @SuppressWarnings("deprecation")
    public BlockState rotate(BlockState state, BlockRotation rotation) {
        return state.with(FACING, rotation.rotate(state.get(FACING)));
    }

    @Override
    protected void appendProperties(StateManager.Builder<Block, BlockState> builder) {
        builder.add(HANDSTONE, FACING);
    }

    static {
        HANDSTONE = BooleanProperty.of("handstone");
        FACING = HorizontalFacingBlock.FACING;
    }
}
