package com.yungnickyoung.minecraft.ribbits.fabric;

import com.yungnickyoung.minecraft.ribbits.RibbitsCommon;
import com.yungnickyoung.minecraft.ribbits.fabric.module.EntityDataSerializerModuleFabric;
import com.yungnickyoung.minecraft.ribbits.fabric.module.NetworkModuleFabric;
import com.yungnickyoung.minecraft.ribbits.network.payload.RequestSupporterHatStatePayload;
import com.yungnickyoung.minecraft.ribbits.player.PlayerInstrumentTracker;
import com.yungnickyoung.minecraft.ribbits.supporters.SupportersListServer;
import com.yungnickyoung.minecraft.ribbits.module.ConfigModule;
import net.fabricmc.api.ModInitializer;
import net.fabricmc.fabric.api.event.lifecycle.v1.ServerLifecycleEvents;
import net.fabricmc.fabric.api.event.lifecycle.v1.ServerTickEvents;
import net.fabricmc.fabric.api.networking.v1.ServerPlayConnectionEvents;
import net.fabricmc.fabric.api.networking.v1.ServerPlayNetworking;
import net.minecraft.server.MinecraftServer;

import java.util.List;
import java.util.UUID;

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
        NetworkModuleFabric.register();
        ConfigModule.init(null);
        RibbitsCommon.init();

        // Player join: send supporter hat state
        ServerPlayConnectionEvents.JOIN.register((handler, sender, server) -> {
            List<UUID> playersWithSupporterHat = SupportersListServer.getPlayersWithSupporterHat().stream().toList();
            ServerPlayNetworking.send(handler.getPlayer(), new RequestSupporterHatStatePayload(playersWithSupporterHat));
        });

        ServerTickEvents.START_SERVER_TICK.register(server -> PlayerInstrumentTracker.onServerTick());
    }

    public static MinecraftServer getCurrentServer() {
        return currentServer;
    }
}
