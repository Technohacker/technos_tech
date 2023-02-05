package com.technohacker.tech.blocks.ore_extractor;

import net.fabricmc.fabric.api.object.builder.v1.block.FabricBlockSettings;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.FacingBlock;
import net.minecraft.block.Material;
import net.minecraft.item.ItemPlacementContext;
import net.minecraft.state.StateManager;
import net.minecraft.util.math.Direction;

public class OreExtractorBlock extends FacingBlock {
    public OreExtractorBlock() {
        super(FabricBlockSettings.of(Material.METAL));

        // By default, point to north
        setDefaultState(getDefaultState().with(FACING, Direction.NORTH));
    }

    @Override
    protected void appendProperties(StateManager.Builder<Block, BlockState> builder) {
        builder.add(FACING);
    }

    @Override
    public BlockState getPlacementState(ItemPlacementContext ctx) {
        // Point the block along the direction the player's facing
        return super.getPlacementState(ctx).with(FACING, ctx.getPlayerLookDirection());
    }
}
