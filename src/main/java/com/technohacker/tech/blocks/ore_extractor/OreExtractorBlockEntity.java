package com.technohacker.tech.blocks.ore_extractor;

import com.technohacker.tech.TechnosTechMod;
import com.technohacker.tech.util.CommonTags;

import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.inventory.Inventories;
import net.minecraft.inventory.Inventory;
import net.minecraft.inventory.SimpleInventory;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.sound.SoundCategory;
import net.minecraft.sound.SoundEvents;
import net.minecraft.state.property.Properties;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

public class OreExtractorBlockEntity extends BlockEntity implements Inventory {
    static final int TICK_CYCLE = 20;
    static final int DISPENSE_TICK = 15;

    private int tickCycleStep = 0;
    private SimpleInventory inventory = new SimpleInventory(1);

    public OreExtractorBlockEntity(BlockPos pos, BlockState state) {
        super(TechnosTechMod.BLOCK_ENTITIES.get("ore_extractor"), pos, state);
    }

    @Override
    public void writeNbt(NbtCompound nbt) {
        Inventories.writeNbt(nbt, this.inventory.stacks);

        super.writeNbt(nbt);
    }

    @Override
    public void readNbt(NbtCompound nbt) {
        super.readNbt(nbt);

        Inventories.readNbt(nbt, this.inventory.stacks);
    }

    // For Inventory
    @Override
    public void clear() {
        this.inventory.clear();
    }

    @Override
    public int size() {
        return this.inventory.size();
    }

    @Override
    public boolean isEmpty() {
        return this.inventory.isEmpty();
    }

    @Override
    public ItemStack getStack(int slot) {
        return this.inventory.getStack(slot);
    }

    @Override
    public ItemStack removeStack(int slot, int amount) {
        return this.inventory.removeStack(slot, amount);
    }

    @Override
    public ItemStack removeStack(int slot) {
        return this.inventory.removeStack(slot);
    }

    @Override
    public void setStack(int slot, ItemStack stack) {
        this.inventory.setStack(slot, stack);
    }

    @Override
    public boolean canPlayerUse(PlayerEntity player) {
        return this.inventory.canPlayerUse(player);
    }

    // Block-specific logic
    public static void tick(World world, BlockPos pos, BlockState state, OreExtractorBlockEntity be) {
        // Don't tick on the client
        if (world.isClient) {
            return;
        }

        be.tickCycleStep += 1;

        // Take the block that's right in front of us
        var frontPos = pos.offset(state.get(Properties.FACING));
        var frontBlock = world.getBlockState(frontPos);
        var facingAnOre = frontBlock.isIn(CommonTags.COMMON_ORES);

        // Only continue processing if we're facing an ore
        if (!facingAnOre) {
            return;
        }

        // Play a block breaking sound every 4 ticks if there's a block
        if (be.tickCycleStep % 4 == 0) {
            world.playSound(null, pos, SoundEvents.BLOCK_STONE_HIT, SoundCategory.BLOCKS, 0.25f, 1.0f);
        }

        switch (be.tickCycleStep) {
            case DISPENSE_TICK -> {
                world.playSound(null, pos, SoundEvents.BLOCK_STONE_BREAK, SoundCategory.BLOCKS, 0.25f, 1.0f);

                var drops = Block.getDroppedStacks(frontBlock, (ServerWorld) world, frontPos, null);

                for (var stack : drops) {
                    be.inventory.addStack(stack);
                }
            }
            case TICK_CYCLE -> {
                be.tickCycleStep = 0;
            }
        }
    }
}
