package com.technohacker.tech.util;

import net.minecraft.block.Block;
import net.minecraft.registry.RegistryKeys;
import net.minecraft.registry.tag.TagKey;
import net.minecraft.util.Identifier;

public class CommonTags {
    public static final TagKey<Block> COMMON_ORES = TagKey.of(RegistryKeys.BLOCK, new Identifier("c", "ores"));
}
