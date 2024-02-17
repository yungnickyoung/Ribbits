package com.yungnickyoung.minecraft.ribbits.services;

import com.yungnickyoung.minecraft.ribbits.block.GiantLilyPadBlockForge;
import com.yungnickyoung.minecraft.ribbits.entity.RibbitEntity;
import com.yungnickyoung.minecraft.ribbits.module.NetworkModuleForge;
import com.yungnickyoung.minecraft.ribbits.network.RibbitMusicStartAllS2CPacket;
import com.yungnickyoung.minecraft.ribbits.network.RibbitMusicStartSingleS2CPacket;
import com.yungnickyoung.minecraft.ribbits.network.RibbitMusicStopSingleS2CPacket;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.SoundType;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.material.Material;
import net.minecraftforge.fml.ModList;
import net.minecraftforge.fml.loading.FMLLoader;

import java.util.function.Supplier;

public class ForgePlatformHelper implements IPlatformHelper {
    @Override
    public String getPlatformName() {
        return "Forge";
    }

    @Override
    public boolean isModLoaded(String modId) {
        return ModList.get().isLoaded(modId);
    }

    @Override
    public boolean isDevelopmentEnvironment() {
        return !FMLLoader.isProduction();
    }

    @Override
    public void onRibbitStartMusicGoal(ServerLevel serverLevel, RibbitEntity newRibbit, RibbitEntity masterRibbit) {
        // If this ribbit is the master ribbit, use its stored tick value, since there is no existing ticking sound to grab the byte offset from.
        // Otherwise, use -1 to indicate that the client should use a byte offset instead, which will be fetched from the existing ticking sound.
        int tickOffset = newRibbit.equals(masterRibbit) ? masterRibbit.getTicksPlayingMusic() : -1;
        NetworkModuleForge.sendToAllClients(new RibbitMusicStartSingleS2CPacket(newRibbit.getId(), tickOffset));
    }

    @Override
    public void onPlayerEnterBandRange(ServerPlayer player, ServerLevel serverLevel, RibbitEntity masterRibbit) {
        NetworkModuleForge.sendToClient(new RibbitMusicStartAllS2CPacket(masterRibbit, masterRibbit.getTicksPlayingMusic()), player);
    }

    @Override
    public void onPlayerExitBandRange(ServerPlayer player, ServerLevel serverLevel, RibbitEntity masterRibbit) {
        NetworkModuleForge.sendToClient(new RibbitMusicStopSingleS2CPacket(masterRibbit.getId()), player);

        for (RibbitEntity ribbit : masterRibbit.getRibbitsPlayingMusic()) {
            NetworkModuleForge.sendToClient(new RibbitMusicStopSingleS2CPacket(ribbit.getId()), player);
        }
    }

    @Override
    public Supplier<Block> getGiantLilyPadBlock() {
        return () -> new GiantLilyPadBlockForge(
                BlockBehaviour.Properties
                        .of(Material.PLANT)
                        .instabreak()
                        .sound(SoundType.LILY_PAD)
                        .noOcclusion());
    }
}
