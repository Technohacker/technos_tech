package com.technohacker.tech;

import net.fabricmc.api.ModInitializer;
import net.fabricmc.fabric.api.item.v1.FabricItemSettings;
import net.fabricmc.fabric.api.itemgroup.v1.FabricItemGroup;
import net.fabricmc.fabric.api.itemgroup.v1.ItemGroupEvents;
import net.minecraft.block.Block;
import net.minecraft.item.BlockItem;
import net.minecraft.item.ItemGroup;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.registry.Registries;
import net.minecraft.registry.Registry;
import net.minecraft.util.Identifier;

import static java.util.Map.entry;

import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.technohacker.tech.blocks.ore_extractor.OreExtractorBlock;

public class TechnosTechMod implements ModInitializer {
	static final String NAMESPACE = "technos_tech";
	static final Logger LOGGER = LoggerFactory.getLogger(NAMESPACE);

	static final Map<String, Block> BLOCKS = Map.ofEntries(
			entry("ore_extractor", new OreExtractorBlock()));

	private static final ItemGroup ITEM_GROUP = FabricItemGroup.builder(new Identifier(NAMESPACE, "item_group"))
			.icon(() -> new ItemStack(Items.ACACIA_BOAT))
			.build();

	@Override
	public void onInitialize() {
		// This code runs as soon as Minecraft is in a mod-load-ready state.
		// However, some things (like resources) may still be uninitialized.
		// Proceed with mild caution.

		LOGGER.info("Techno's Tech booting up");

		for (Map.Entry<String, Block> entry : BLOCKS.entrySet()) {
			// Register the block
			Registry.register(Registries.BLOCK,
					new Identifier(NAMESPACE, entry.getKey()),
					entry.getValue());

			// And an item for the block too
			Registry.register(Registries.ITEM, new Identifier(NAMESPACE, entry.getKey()),
					new BlockItem(entry.getValue(), new FabricItemSettings()));
		}

		ItemGroupEvents.modifyEntriesEvent(ITEM_GROUP).register(content -> {
			for (Map.Entry<String, Block> entry : BLOCKS.entrySet()) {
				// Add the block to the item group
				content.add(entry.getValue());
			}
		});
	}
}