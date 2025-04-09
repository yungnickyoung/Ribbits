package com.yungnickyoung.minecraft.ribbits.services;

import com.yungnickyoung.minecraft.ribbits.data.RibbitProfession;
import com.yungnickyoung.minecraft.ribbits.entity.RibbitEntity;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.block.Block;

import java.nio.file.Path;
import java.util.function.Supplier;

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

    Path getConfigPath();

    /**
     * Called when a ribbit starts playing music. This method should send a payload to all clients to play the music.
     */
    void onRibbitStartMusicGoal(ServerLevel serverLevel, RibbitEntity newRibbit, RibbitEntity masterRibbit);

    /**
     * Called when a player enters the band range of a ribbit. This method should send a payload to the player to play the music.
     */
    void onPlayerEnterBandRange(ServerPlayer player, ServerLevel serverLevel, RibbitEntity masterRibbit);

    void onPlayerExitBandRange(ServerPlayer player, ServerLevel serverLevel, RibbitEntity masterRibbit);

    void startHearingMaraca(ServerPlayer performer, ServerPlayer audienceMember);

    void stopHearingMaraca(ServerPlayer performer, ServerPlayer audienceMember);

    Supplier<Block> getGiantLilyPadBlock();

    Supplier<Item> getRibbitSpawnEggItem(RibbitProfession profession, int backgroundColor, int highlightColor);

    void setBlockAsFlammable(Block block, int igniteChance, int burnChance);
}
