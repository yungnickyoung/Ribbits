package com.yungnickyoung.minecraft.ribbits.module;

import com.yungnickyoung.minecraft.ribbits.RibbitsCommon;
import net.fabricmc.fabric.api.client.itemgroup.FabricItemGroupBuilder;
import net.fabricmc.fabric.api.item.v1.FabricItemSettings;
import net.fabricmc.fabric.api.object.builder.v1.block.FabricBlockSettings;
import net.minecraft.core.Registry;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.block.HugeMushroomBlock;
import net.minecraft.world.level.block.SoundType;
import net.minecraft.world.level.material.Material;
import net.minecraft.world.level.material.MaterialColor;

public class BlockModuleFabric {
    public static void init() {
//        BlockModule.BROWN_TOADSTOOL = new HugeMushroomBlock(FabricBlockSettings
//                .of(Material.WOOD, MaterialColor.DIRT)
//                .strength(0.2f)
//                .sound(SoundType.WOOD));
//        BlockModule.RED_TOADSTOOL = new HugeMushroomBlock(FabricBlockSettings
//                .of(Material.WOOD, MaterialColor.COLOR_RED)
//                .strength(0.2f)
//                .sound(SoundType.WOOD));
//        BlockModule.TOADSTOOL_STEM = new HugeMushroomBlock(FabricBlockSettings
//                .of(Material.WOOD, MaterialColor.WOOL)
//                .strength(0.2f)
//                .sound(SoundType.WOOD));
//
//        BlockModule.TAB_RIBBITS = FabricItemGroupBuilder.build(
//                new ResourceLocation(RibbitsCommon.MOD_ID, "general"),
//                () -> new ItemStack(BlockModule.RED_TOADSTOOL));

        // Blocks
//        Registry.register(Registry.BLOCK, new ResourceLocation(RibbitsCommon.MOD_ID, "brown_toadstool"), BlockModule.BROWN_TOADSTOOL);
//        Registry.register(Registry.BLOCK, new ResourceLocation(RibbitsCommon.MOD_ID, "red_toadstool"), BlockModule.RED_TOADSTOOL);
//        Registry.register(Registry.BLOCK, new ResourceLocation(RibbitsCommon.MOD_ID, "toadstool_stem"), BlockModule.TOADSTOOL_STEM);
//
//        // Items
//        Registry.register(Registry.ITEM,
//                new ResourceLocation(RibbitsCommon.MOD_ID, "brown_toadstool"),
//                new BlockItem(BlockModule.BROWN_TOADSTOOL, new FabricItemSettings().group(BlockModule.TAB_RIBBITS)));
//        Registry.register(Registry.ITEM,
//                new ResourceLocation(RibbitsCommon.MOD_ID, "red_toadstool"),
//                new BlockItem(BlockModule.RED_TOADSTOOL, new FabricItemSettings().group(BlockModule.TAB_RIBBITS)));
//        Registry.register(Registry.ITEM,
//                new ResourceLocation(RibbitsCommon.MOD_ID, "toadstool_stem"),
//                new BlockItem(BlockModule.TOADSTOOL_STEM, new FabricItemSettings().group(BlockModule.TAB_RIBBITS)));
    }
}
