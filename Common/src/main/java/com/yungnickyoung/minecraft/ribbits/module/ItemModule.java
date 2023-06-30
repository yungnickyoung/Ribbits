package com.yungnickyoung.minecraft.ribbits.module;

import com.yungnickyoung.minecraft.ribbits.RibbitsCommon;
import com.yungnickyoung.minecraft.yungsapi.api.autoregister.AutoRegister;
import com.yungnickyoung.minecraft.yungsapi.api.autoregister.AutoRegisterItem;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.WaterLilyBlockItem;

@AutoRegister(RibbitsCommon.MOD_ID)
public class ItemModule {
    @AutoRegister("giant_lilypad")
    public static final AutoRegisterItem GIANT_LILYPAD = AutoRegisterItem.of(() -> new WaterLilyBlockItem(BlockModule.GIANT_LILYPAD.get(), new Item.Properties().tab(RibbitsCommon.TAB_RIBBITS.get())));
}
