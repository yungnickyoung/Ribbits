package com.yungnickyoung.minecraft.ribbits;

import com.yungnickyoung.minecraft.ribbits.item.RibbitSpawnEggDispenseItemBehaviorFabric;
import com.yungnickyoung.minecraft.ribbits.module.ConfigModuleFabric;
import com.yungnickyoung.minecraft.ribbits.module.ItemModule;
import com.yungnickyoung.minecraft.ribbits.module.NetworkModuleFabric;
import com.yungnickyoung.minecraft.ribbits.player.PlayerInstrumentTracker;
import com.yungnickyoung.minecraft.ribbits.supporters.SupporterEventsFabric;
import net.fabricmc.api.ModInitializer;
import net.fabricmc.fabric.api.event.lifecycle.v1.ServerTickEvents;
import net.fabricmc.fabric.api.networking.v1.ServerPlayConnectionEvents;
import net.minecraft.core.dispenser.DispenseItemBehavior;
import net.minecraft.world.level.block.DispenserBlock;
import software.bernie.geckolib.GeckoLib;

public class RibbitsFabric implements ModInitializer {
    @Override
    public void onInitialize() {
        GeckoLib.initialize();
        RibbitsCommon.init();

        ConfigModuleFabric.init();
        NetworkModuleFabric.registerC2SPackets();

        // Custom dispenser behavior for Ribbit spawn eggs
        DispenseItemBehavior ribbitSpawnEggDispenseItemBehavior = new RibbitSpawnEggDispenseItemBehaviorFabric();
        DispenserBlock.registerBehavior(ItemModule.RIBBIT_NITWIT_SPAWN_EGG.get(), ribbitSpawnEggDispenseItemBehavior);
        DispenserBlock.registerBehavior(ItemModule.RIBBIT_FISHERMAN_SPAWN_EGG.get(), ribbitSpawnEggDispenseItemBehavior);
        DispenserBlock.registerBehavior(ItemModule.RIBBIT_GARDENER_SPAWN_EGG.get(), ribbitSpawnEggDispenseItemBehavior);
        DispenserBlock.registerBehavior(ItemModule.RIBBIT_MERCHANT_SPAWN_EGG.get(), ribbitSpawnEggDispenseItemBehavior);
        DispenserBlock.registerBehavior(ItemModule.RIBBIT_SORCERER_SPAWN_EGG.get(), ribbitSpawnEggDispenseItemBehavior);

        // Events
        ServerTickEvents.START_SERVER_TICK.register(server -> PlayerInstrumentTracker.onServerTick());
        ServerPlayConnectionEvents.JOIN.register((handler, sender, server) -> SupporterEventsFabric.onPlayerJoin(sender));
    }
}
