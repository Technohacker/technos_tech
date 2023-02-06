package com.technohacker.tech.blocks.ore_extractor;

import com.technohacker.tech.TechnosTechMod;

import net.fabricmc.fabric.api.object.builder.v1.block.FabricBlockSettings;
import net.minecraft.block.Block;
import net.minecraft.block.BlockRenderType;
import net.minecraft.block.BlockState;
import net.minecraft.block.BlockWithEntity;
import net.minecraft.block.Material;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.block.entity.BlockEntityTicker;
import net.minecraft.block.entity.BlockEntityType;
import net.minecraft.item.ItemPlacementContext;
import net.minecraft.state.StateManager;
import net.minecraft.state.property.Properties;
import net.minecraft.util.BlockMirror;
import net.minecraft.util.BlockRotation;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Direction;
import net.minecraft.world.World;

public class OreExtractorBlock extends BlockWithEntity {
    public OreExtractorBlock() {
        super(FabricBlockSettings.of(Material.METAL));

        // By default, point to north
        setDefaultState(getDefaultState().with(Properties.FACING, Direction.NORTH));
    }

    // For directional placement
    @Override
    protected void appendProperties(StateManager.Builder<Block, BlockState> builder) {
        builder.add(Properties.FACING);
    }

    @Override
    public BlockState getPlacementState(ItemPlacementContext ctx) {
        // Point the block along the direction the player's facing
        return super.getPlacementState(ctx).with(Properties.FACING, ctx.getPlayerLookDirection());
    }

    @Override
    public BlockState rotate(BlockState state, BlockRotation rotation) {
        return (BlockState)state.with(Properties.FACING, rotation.rotate(state.get(Properties.FACING)));
    }

    @Override
    public BlockState mirror(BlockState state, BlockMirror mirror) {
        return state.rotate(mirror.getRotation(state.get(Properties.FACING)));
    }

    // For BlockWithEntity
    @Override
    public BlockRenderType getRenderType(BlockState state) {
        return BlockRenderType.MODEL;
    }

    @Override
    public <T extends BlockEntity> BlockEntityTicker<T> getTicker(World world, BlockState state,
            BlockEntityType<T> type) {
        return checkType(type, TechnosTechMod.BLOCK_ENTITIES.get("ore_extractor"), (world1, pos, state1,
                be) -> OreExtractorBlockEntity.tick(world1, pos, state1, (OreExtractorBlockEntity) be));
    }

    @Override
    public BlockEntity createBlockEntity(BlockPos pos, BlockState state) {
        return new OreExtractorBlockEntity(pos, state);
    }
}
