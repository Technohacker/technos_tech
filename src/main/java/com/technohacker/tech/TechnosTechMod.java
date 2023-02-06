package com.technohacker.tech;

import net.fabricmc.api.ModInitializer;
import net.fabricmc.fabric.api.item.v1.FabricItemSettings;
import net.fabricmc.fabric.api.itemgroup.v1.FabricItemGroup;
import net.fabricmc.fabric.api.itemgroup.v1.ItemGroupEvents;
import net.fabricmc.fabric.api.object.builder.v1.block.entity.FabricBlockEntityTypeBuilder;
import net.minecraft.block.Block;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.block.entity.BlockEntityType;
import net.minecraft.item.BlockItem;
import net.minecraft.item.Item;
import net.minecraft.item.ItemGroup;
import net.minecraft.item.ItemStack;
import net.minecraft.registry.Registries;
import net.minecraft.registry.Registry;
import net.minecraft.util.Identifier;

import static java.util.Map.entry;

import java.util.HashMap;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.technohacker.tech.blocks.ore_extractor.OreExtractorBlock;
import com.technohacker.tech.blocks.ore_extractor.OreExtractorBlockEntity;

public class TechnosTechMod implements ModInitializer {
	static final String NAMESPACE = "technos_tech";
	static final Logger LOGGER = LoggerFactory.getLogger(NAMESPACE);

	public static final Map<String, Block> BLOCKS;
	public static final Map<String, BlockEntityType<? extends BlockEntity>> BLOCK_ENTITIES;
	public static final Map<String, Item> ITEMS;
	public static final ItemGroup ITEM_GROUP;

	static {
		BLOCKS = Map.ofEntries(
				entry("ore_extractor", new OreExtractorBlock()));

		BLOCK_ENTITIES = Map.ofEntries(
				entry("ore_extractor",
						FabricBlockEntityTypeBuilder.create(OreExtractorBlockEntity::new, BLOCKS.get("ore_extractor"))
								.build()));

		var items = new HashMap<String, Item>();

		// Add an item for each block
		for (var entry : BLOCKS.entrySet()) {
			items.put(entry.getKey(), new BlockItem(entry.getValue(), new FabricItemSettings()));
		}

		ITEMS = Map.copyOf(items);

		ITEM_GROUP = FabricItemGroup.builder(new Identifier(NAMESPACE, "item_group"))
				.icon(() -> new ItemStack(ITEMS.get("ore_extractor")))
				.build();
	}

	@Override
	public void onInitialize() {
		// This code runs as soon as Minecraft is in a mod-load-ready state.
		// However, some things (like resources) may still be uninitialized.
		// Proceed with mild caution.

		LOGGER.info("Techno's Tech booting up");

		// Register every block
		for (var entry : BLOCKS.entrySet()) {
			Registry.register(Registries.BLOCK, new Identifier(NAMESPACE, entry.getKey()), entry.getValue());
		}

		// Register every block entity
		for (var entry : BLOCK_ENTITIES.entrySet()) {
			Registry.register(Registries.BLOCK_ENTITY_TYPE, new Identifier(NAMESPACE, entry.getKey()),
					entry.getValue());
		}

		// Register every item
		for (var entry : ITEMS.entrySet()) {
			Registry.register(Registries.ITEM, new Identifier(NAMESPACE, entry.getKey()), entry.getValue());
		}

		// And add the item group
		ItemGroupEvents.modifyEntriesEvent(ITEM_GROUP).register(content -> {
			for (var entry : ITEMS.entrySet()) {
				// Every item to the item group
				content.add(entry.getValue());
			}
		});
	}
}