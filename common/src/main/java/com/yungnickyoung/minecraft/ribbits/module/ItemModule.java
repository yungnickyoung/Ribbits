package com.yungnickyoung.minecraft.ribbits.module;

import com.yungnickyoung.minecraft.ribbits.RibbitsCommon;
import com.yungnickyoung.minecraft.ribbits.item.MaracaItem;
import com.yungnickyoung.minecraft.ribbits.item.RibbitSpawnEggDispenseItemBehavior;
import com.yungnickyoung.minecraft.ribbits.item.RibbitSpawnEggItem;
import com.yungnickyoung.minecraft.yungsapi.api.autoregister.AutoRegister;
import com.yungnickyoung.minecraft.yungsapi.api.autoregister.AutoRegisterItem;
import com.yungnickyoung.minecraft.yungsapi.api.autoregister.AutoRegisterUtils;
import net.minecraft.core.dispenser.DispenseItemBehavior;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.PlaceOnWaterBlockItem;
import net.minecraft.world.level.block.DispenserBlock;

@AutoRegister(RibbitsCommon.MOD_ID)
public class ItemModule {
    @AutoRegister("giant_lilypad")
    public static final AutoRegisterItem GIANT_LILYPAD = AutoRegisterItem.of(() -> new PlaceOnWaterBlockItem(BlockModule.GIANT_LILYPAD.get(), new Item.Properties()));

    @AutoRegister("ribbit_nitwit_spawn_egg")
    public static final AutoRegisterItem RIBBIT_NITWIT_SPAWN_EGG = AutoRegisterItem.of(
            () -> new RibbitSpawnEggItem(RibbitProfessionModule.NITWIT, new Item.Properties())
    );

    @AutoRegister("ribbit_fisherman_spawn_egg")
    public static final AutoRegisterItem RIBBIT_FISHERMAN_SPAWN_EGG = AutoRegisterItem.of(
            () -> new RibbitSpawnEggItem(RibbitProfessionModule.FISHERMAN, new Item.Properties())
    );

    @AutoRegister("ribbit_gardener_spawn_egg")
    public static final AutoRegisterItem RIBBIT_GARDENER_SPAWN_EGG = AutoRegisterItem.of(
            () -> new RibbitSpawnEggItem(RibbitProfessionModule.GARDENER, new Item.Properties())
    );

    @AutoRegister("ribbit_merchant_spawn_egg")
    public static final AutoRegisterItem RIBBIT_MERCHANT_SPAWN_EGG = AutoRegisterItem.of(
            () -> new RibbitSpawnEggItem(RibbitProfessionModule.MERCHANT, new Item.Properties())
    );

    @AutoRegister("ribbit_sorcerer_spawn_egg")
    public static final AutoRegisterItem RIBBIT_SORCERER_SPAWN_EGG = AutoRegisterItem.of(
            () -> new RibbitSpawnEggItem(RibbitProfessionModule.SORCERER, new Item.Properties())
    );
    @AutoRegister("maraca")
    public static final AutoRegisterItem MARACA = AutoRegisterItem.of(() -> new MaracaItem(new Item.Properties().stacksTo(1)));

    @AutoRegister("_ignored")
    public static void registerCompostables() {
        AutoRegisterUtils.addCompostableItem(() -> BlockModule.SWAMP_DAISY.get().asItem(), 0.65F);
        AutoRegisterUtils.addCompostableItem(() -> BlockModule.GIANT_LILYPAD.get().asItem(), 0.65F);
        AutoRegisterUtils.addCompostableItem(() -> BlockModule.UMBRELLA_LEAF.get().asItem(), 0.65F);
        AutoRegisterUtils.addCompostableItem(() -> BlockModule.TOADSTOOL.get().asItem(), 0.65F);
        AutoRegisterUtils.addCompostableItem(() -> BlockModule.TOADSTOOL_STEM.get().asItem(), 0.85F);
        AutoRegisterUtils.addCompostableItem(() -> BlockModule.BROWN_TOADSTOOL.get().asItem(), 0.85F);
        AutoRegisterUtils.addCompostableItem(() -> BlockModule.RED_TOADSTOOL.get().asItem(), 0.85F);

        DispenseItemBehavior ribbitSpawnEggDispenseItemBehavior = new RibbitSpawnEggDispenseItemBehavior();

        DispenserBlock.registerBehavior(RIBBIT_NITWIT_SPAWN_EGG::get, ribbitSpawnEggDispenseItemBehavior);
        DispenserBlock.registerBehavior(RIBBIT_FISHERMAN_SPAWN_EGG::get, ribbitSpawnEggDispenseItemBehavior);
        DispenserBlock.registerBehavior(RIBBIT_GARDENER_SPAWN_EGG::get, ribbitSpawnEggDispenseItemBehavior);
        DispenserBlock.registerBehavior(RIBBIT_MERCHANT_SPAWN_EGG::get, ribbitSpawnEggDispenseItemBehavior);
        DispenserBlock.registerBehavior(RIBBIT_SORCERER_SPAWN_EGG::get, ribbitSpawnEggDispenseItemBehavior);
    }
}
