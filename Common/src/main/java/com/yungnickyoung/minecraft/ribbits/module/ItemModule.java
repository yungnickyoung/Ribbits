package com.yungnickyoung.minecraft.ribbits.module;

import com.yungnickyoung.minecraft.ribbits.RibbitsCommon;
import com.yungnickyoung.minecraft.yungsapi.api.autoregister.AutoRegister;
import com.yungnickyoung.minecraft.yungsapi.api.autoregister.AutoRegisterItem;
import com.yungnickyoung.minecraft.yungsapi.api.autoregister.AutoRegisterUtils;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.WaterLilyBlockItem;

@AutoRegister(RibbitsCommon.MOD_ID)
public class ItemModule {
    @AutoRegister("giant_lilypad")
    public static final AutoRegisterItem GIANT_LILYPAD = AutoRegisterItem.of(() -> new WaterLilyBlockItem(BlockModule.GIANT_LILYPAD.get(), new Item.Properties().tab(RibbitsCommon.TAB_RIBBITS.get())));

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
