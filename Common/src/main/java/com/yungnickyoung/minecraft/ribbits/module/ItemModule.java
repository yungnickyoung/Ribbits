package com.yungnickyoung.minecraft.ribbits.module;

import com.yungnickyoung.minecraft.ribbits.RibbitsCommon;
import com.yungnickyoung.minecraft.ribbits.item.RibbitSpawnEggItem;
import com.yungnickyoung.minecraft.yungsapi.api.autoregister.AutoRegister;
import com.yungnickyoung.minecraft.yungsapi.api.autoregister.AutoRegisterItem;
import com.yungnickyoung.minecraft.yungsapi.api.autoregister.AutoRegisterUtils;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.WaterLilyBlockItem;

@AutoRegister(RibbitsCommon.MOD_ID)
public class ItemModule {
    @AutoRegister("giant_lilypad")
    public static final AutoRegisterItem GIANT_LILYPAD = AutoRegisterItem.of(() -> new WaterLilyBlockItem(BlockModule.GIANT_LILYPAD.get(), new Item.Properties().tab(RibbitsCommon.TAB_RIBBITS.get())));

    @AutoRegister("ribbit_nitwit_spawn_egg")
    public static final AutoRegisterItem RIBBIT_NITWIT_SPAWN_EGG = AutoRegisterItem.of(() -> new RibbitSpawnEggItem(EntityTypeModule.RIBBIT.get(), RibbitProfessionModule.NITWIT, 0xb3c35b, 0x769b4f, new Item.Properties().tab(RibbitsCommon.TAB_RIBBITS.get())));

    @AutoRegister("ribbit_fisherman_spawn_egg")
    public static final AutoRegisterItem RIBBIT_FISHERMAN_SPAWN_EGG = AutoRegisterItem.of(() -> new RibbitSpawnEggItem(EntityTypeModule.RIBBIT.get(), RibbitProfessionModule.FISHERMAN, 0xb3c35b, 0x956c41, new Item.Properties().tab(RibbitsCommon.TAB_RIBBITS.get())));

    @AutoRegister("ribbit_gardener_spawn_egg")
    public static final AutoRegisterItem RIBBIT_GARDENER_SPAWN_EGG = AutoRegisterItem.of(() -> new RibbitSpawnEggItem(EntityTypeModule.RIBBIT.get(), RibbitProfessionModule.GARDENER, 0xb3c35b, 0x6b4434, new Item.Properties().tab(RibbitsCommon.TAB_RIBBITS.get())));

    @AutoRegister("ribbit_merchant_spawn_egg")
    public static final AutoRegisterItem RIBBIT_MERCHANT_SPAWN_EGG = AutoRegisterItem.of(() -> new RibbitSpawnEggItem(EntityTypeModule.RIBBIT.get(), RibbitProfessionModule.MERCHANT, 0xb3c35b, 0x5b3f28, new Item.Properties().tab(RibbitsCommon.TAB_RIBBITS.get())));

    @AutoRegister("ribbit_sorcerer_spawn_egg")
    public static final AutoRegisterItem RIBBIT_SORCERER_SPAWN_EGG = AutoRegisterItem.of(() -> new RibbitSpawnEggItem(EntityTypeModule.RIBBIT.get(), RibbitProfessionModule.SORCERER, 0xb3c35b, 0x774d7e, new Item.Properties().tab(RibbitsCommon.TAB_RIBBITS.get())));

    @AutoRegister("_ignored")
    public static void registerCompostables() {
        AutoRegisterUtils.addCompostableItem(() -> BlockModule.SWAMP_DAISY.get().asItem(), 0.65F);
        AutoRegisterUtils.addCompostableItem(() -> BlockModule.GIANT_LILYPAD.get().asItem(), 0.65F);
        AutoRegisterUtils.addCompostableItem(() -> BlockModule.UMBRELLA_LEAF.get().asItem(), 0.65F);
        AutoRegisterUtils.addCompostableItem(() -> BlockModule.TOADSTOOL.get().asItem(), 0.65F);
        AutoRegisterUtils.addCompostableItem(() -> BlockModule.TOADSTOOL_STEM.get().asItem(), 0.85F);
        AutoRegisterUtils.addCompostableItem(() -> BlockModule.BROWN_TOADSTOOL.get().asItem(), 0.85F);
        AutoRegisterUtils.addCompostableItem(() -> BlockModule.RED_TOADSTOOL.get().asItem(), 0.85F);
    }
}
