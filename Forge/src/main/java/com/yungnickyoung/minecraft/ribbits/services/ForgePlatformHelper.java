package com.yungnickyoung.minecraft.ribbits.services;

import com.yungnickyoung.minecraft.ribbits.block.GiantLilyPadBlockForge;
import com.yungnickyoung.minecraft.ribbits.entity.RibbitEntity;
import com.yungnickyoung.minecraft.ribbits.module.NetworkModuleForge;
import com.yungnickyoung.minecraft.ribbits.network.RibbitMusicS2CPacket;
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
    public void sendRibbitMusicS2CPacketToAll(ServerLevel serverLevel, RibbitEntity newRibbit, RibbitEntity masterRibbit) {
        if (newRibbit.equals(masterRibbit)) {
            NetworkModuleForge.sendToAllClients(new RibbitMusicS2CPacket(newRibbit.getId(), masterRibbit.getTicksPlayingMusic()));

            for (RibbitEntity ribbit : masterRibbit.getRibbitsPlayingMusic()) {
                NetworkModuleForge.sendToAllClients(new RibbitMusicS2CPacket(ribbit.getId(), -1));
            }
        } else {
            NetworkModuleForge.sendToAllClients(new RibbitMusicS2CPacket(newRibbit.getId(), -1));
        }
    }

    @Override
    public void sendRibbitMusicS2CPacketToPlayer(ServerPlayer player, ServerLevel serverLevel, RibbitEntity newRibbit, RibbitEntity masterRibbit) {
        NetworkModuleForge.sendToClient(new RibbitMusicS2CPacket(newRibbit.getId(), masterRibbit.getTicksPlayingMusic()), player);

        for (RibbitEntity ribbit : masterRibbit.getRibbitsPlayingMusic()) {
            NetworkModuleForge.sendToClient(new RibbitMusicS2CPacket(ribbit.getId(), -1), player);
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
