package com.yungnickyoung.minecraft.ribbits.services;

import com.yungnickyoung.minecraft.ribbits.entity.RibbitEntity;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.player.Player;

public interface IPlatformHelper {
    /**
     * Gets the name of the current platform
     *
     * @return The name of the current platform.
     */
    String getPlatformName();

    /**
     * Checks if a mod with the given id is loaded.
     *
     * @param modId The mod to check if it is loaded.
     * @return True if the mod is loaded, false otherwise.
     */
    boolean isModLoaded(String modId);

    /**
     * Check if the game is currently in a development environment.
     *
     * @return True if in a development environment, false otherwise.
     */
    boolean isDevelopmentEnvironment();

    void sendRibbitMusicS2CPacketToAll(ServerLevel serverLevel, RibbitEntity newRibbit, RibbitEntity masterRibbit);

    void sendRibbitMusicS2CPacketToPlayer(ServerPlayer player, ServerLevel serverLevel, RibbitEntity newRibbit, RibbitEntity masterRibbit);
}
