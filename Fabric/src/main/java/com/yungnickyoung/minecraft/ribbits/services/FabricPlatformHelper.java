package com.yungnickyoung.minecraft.ribbits.services;

import com.yungnickyoung.minecraft.ribbits.block.GiantLilyPadBlock;
import com.yungnickyoung.minecraft.ribbits.entity.RibbitEntity;
import com.yungnickyoung.minecraft.ribbits.module.NetworkModuleFabric;
import net.fabricmc.fabric.api.networking.v1.PacketByteBufs;
import net.fabricmc.fabric.api.networking.v1.PlayerLookup;
import net.fabricmc.fabric.api.networking.v1.ServerPlayNetworking;
import net.fabricmc.loader.api.FabricLoader;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.SoundType;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.material.Material;

import java.util.function.Supplier;

public class FabricPlatformHelper implements IPlatformHelper {
    @Override
    public String getPlatformName() {
        return "Fabric";
    }

    @Override
    public boolean isModLoaded(String modId) {
        return FabricLoader.getInstance().isModLoaded(modId);
    }

    @Override
    public boolean isDevelopmentEnvironment() {
        return FabricLoader.getInstance().isDevelopmentEnvironment();
    }

    @Override
    public void sendRibbitMusicS2CPacketToAll(ServerLevel serverLevel, RibbitEntity newRibbit, RibbitEntity masterRibbit) {
        FriendlyByteBuf buf = PacketByteBufs.create();

        if (newRibbit.equals(masterRibbit)) {
            buf.writeInt(newRibbit.getId());
            buf.writeInt(masterRibbit.getTicksPlayingMusic());

            for (ServerPlayer player : PlayerLookup.all(serverLevel.getServer())) {
                ServerPlayNetworking.send(player, NetworkModuleFabric.RIBBIT_MUSIC_ID, buf);
            }
        } else {
            buf.writeInt(newRibbit.getId());
            buf.writeInt(-1);

            for (ServerPlayer player : PlayerLookup.all(serverLevel.getServer())) {
                ServerPlayNetworking.send(player, NetworkModuleFabric.RIBBIT_MUSIC_ID, buf);
            }
        }
    }

    @Override
    public void sendRibbitMusicS2CPacketToPlayer(ServerPlayer player, ServerLevel serverLevel, RibbitEntity newRibbit, RibbitEntity masterRibbit) {
        FriendlyByteBuf buf = PacketByteBufs.create();

        buf.writeInt(newRibbit.getId());
        buf.writeInt(masterRibbit.getTicksPlayingMusic());
        ServerPlayNetworking.send(player, NetworkModuleFabric.RIBBIT_MUSIC_ID, buf);

        for (RibbitEntity ribbit : masterRibbit.getRibbitsPlayingMusic()) {
            buf = PacketByteBufs.create();

            buf.writeInt(ribbit.getId());
            buf.writeInt(-1);
            ServerPlayNetworking.send(player, NetworkModuleFabric.RIBBIT_MUSIC_ID, buf);
        }
    }

    @Override
    public Supplier<Block> getGiantLilyPadBlock() {
        return () -> new GiantLilyPadBlock(
                BlockBehaviour.Properties
                        .of(Material.PLANT)
                        .instabreak()
                        .sound(SoundType.LILY_PAD)
                        .noOcclusion());
    }
}
