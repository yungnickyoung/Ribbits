package com.yungnickyoung.minecraft.ribbits.fabric;

import com.yungnickyoung.minecraft.ribbits.RibbitsCommon;
import com.yungnickyoung.minecraft.ribbits.config.ConfigScreenRegistry;
import com.yungnickyoung.minecraft.ribbits.fabric.module.EntityDataSerializerModuleFabric;
import com.yungnickyoung.minecraft.ribbits.fabric.supporters.SupporterEventsFabric;
import com.yungnickyoung.minecraft.ribbits.player.PlayerInstrumentTracker;
import dev.architectury.platform.Platform;
import net.fabricmc.api.ModInitializer;
import net.fabricmc.fabric.api.event.lifecycle.v1.ServerLifecycleEvents;
import net.fabricmc.fabric.api.event.lifecycle.v1.ServerTickEvents;
import net.fabricmc.fabric.api.networking.v1.ServerPlayConnectionEvents;
import net.minecraft.server.MinecraftServer;

public class RibbitsFabric implements ModInitializer {
    private static MinecraftServer currentServer = null;

    @Override
    public void onInitialize() {
        ServerLifecycleEvents.SERVER_STARTED.register(server -> {
            currentServer = server;
        });
        ServerLifecycleEvents.SERVER_STOPPED.register(server -> {
            currentServer = null;
        });
        EntityDataSerializerModuleFabric.init();
        RibbitsCommon.init();

        ServerTickEvents.START_SERVER_TICK.register(server -> PlayerInstrumentTracker.onServerTick());
    }

    public static MinecraftServer getCurrentServer() {
        return currentServer;
    }
}
