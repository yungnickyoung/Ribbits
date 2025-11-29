package com.yungnickyoung.minecraft.ribbits.platform;

import net.minecraft.network.protocol.common.custom.CustomPacketPayload;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.level.block.Block;

import java.nio.file.Path;
import java.util.List;

public interface IPlatformHelper {
    void setBlockAsFlammable(Block block, int igniteChance, int burnChance);

    MinecraftServer getCurrentServer();

    String getPlatformName();

    Path getConfigFolder();

    boolean isDevelopmentEnvironment();

    boolean isClient();

    boolean isServer();

    <T extends CustomPacketPayload> void sendToPlayer(ServerPlayer player, T payload);

    <T extends CustomPacketPayload> void sendToPlayers(List<ServerPlayer> players, T payload);

    <T extends CustomPacketPayload> void sendToServer(T payload);
}
