package com.yungnickyoung.minecraft.ribbits.services;

import com.yungnickyoung.minecraft.ribbits.block.GiantLilyPadBlock;
import com.yungnickyoung.minecraft.ribbits.entity.RibbitEntity;
import com.yungnickyoung.minecraft.ribbits.module.NetworkModuleFabric;
import com.yungnickyoung.minecraft.ribbits.util.BufferUtils;
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

import java.util.ArrayList;
import java.util.List;
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
    public void onRibbitStartMusicGoal(ServerLevel serverLevel, RibbitEntity newRibbit, RibbitEntity masterRibbit) {
        FriendlyByteBuf buf = PacketByteBufs.create();

        // If this ribbit is the master ribbit, use its stored tick value, since there is no existing ticking sound to grab the byte offset from.
        // Otherwise, use -1 to indicate that the client should use a byte offset instead, which will be fetched from the existing ticking sound.
        int tickOffset = newRibbit.equals(masterRibbit) ? masterRibbit.getTicksPlayingMusic() : -1;

        buf.writeInt(newRibbit.getId());
        buf.writeInt(tickOffset);
        PlayerLookup.all(serverLevel.getServer()).forEach(player -> ServerPlayNetworking.send(player, NetworkModuleFabric.RIBBIT_START_MUSIC_SINGLE, buf));
    }

    @Override
    public void onPlayerEnterBandRange(ServerPlayer player, ServerLevel serverLevel, RibbitEntity newRibbit, RibbitEntity masterRibbit) {
        FriendlyByteBuf buf = PacketByteBufs.create();

        List<Integer> ribbitIds = new ArrayList<>();
        ribbitIds.add(masterRibbit.getId());
        ribbitIds.addAll(masterRibbit.getRibbitsPlayingMusic().stream().map(RibbitEntity::getId).toList());

        BufferUtils.writeIntList(ribbitIds, buf);
        buf.writeInt(masterRibbit.getTicksPlayingMusic());
        ServerPlayNetworking.send(player, NetworkModuleFabric.RIBBIT_START_MUSIC_ALL, buf);
    }

    @Override
    public void onPlayerExitBandRange(ServerPlayer player, ServerLevel serverLevel, RibbitEntity masterRibbit) {
        FriendlyByteBuf buf = PacketByteBufs.create();
        buf.writeInt(masterRibbit.getId());
        ServerPlayNetworking.send(player, NetworkModuleFabric.RIBBIT_STOP_MUSIC, buf);

        for (RibbitEntity ribbit : masterRibbit.getRibbitsPlayingMusic()) {
            buf = PacketByteBufs.create();
            buf.writeInt(ribbit.getId());
            ServerPlayNetworking.send(player, NetworkModuleFabric.RIBBIT_STOP_MUSIC, buf);
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
