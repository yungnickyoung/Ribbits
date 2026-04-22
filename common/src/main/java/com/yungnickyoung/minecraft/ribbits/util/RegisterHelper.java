package com.yungnickyoung.minecraft.ribbits.util;

import com.yungnickyoung.minecraft.ribbits.RibbitsCommon;
import net.minecraft.core.registries.Registries;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.Identifier;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.block.Block;

public class RegisterHelper {
    public static Identifier id(String name) {
        return Identifier.fromNamespaceAndPath(RibbitsCommon.MOD_ID, name);
    }

    public static ResourceKey<Block> blockKey(String name) {
        return ResourceKey.create(Registries.BLOCK, id(name));
    }

    public static ResourceKey<Item> itemKey(String name) {
        return ResourceKey.create(Registries.ITEM, id(name));
    }

}
