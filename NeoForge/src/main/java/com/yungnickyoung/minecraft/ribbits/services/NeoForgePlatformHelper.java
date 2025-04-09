package com.yungnickyoung.minecraft.ribbits.services;

import com.yungnickyoung.minecraft.ribbits.block.GiantLilyPadBlockNeoForge;
import com.yungnickyoung.minecraft.ribbits.data.RibbitProfession;
import com.yungnickyoung.minecraft.ribbits.entity.RibbitEntity;
import com.yungnickyoung.minecraft.ribbits.item.RibbitSpawnEggItemNeoForge;
import com.yungnickyoung.minecraft.ribbits.module.EntityTypeModule;
import com.yungnickyoung.minecraft.ribbits.network.payload.RibbitStartMusicAllPayload;
import com.yungnickyoung.minecraft.ribbits.network.payload.RibbitStartMusicSinglePayload;
import com.yungnickyoung.minecraft.ribbits.network.payload.RibbitStopMusicSinglePayload;
import com.yungnickyoung.minecraft.ribbits.network.payload.StartHearingMaracaPayload;
import com.yungnickyoung.minecraft.ribbits.network.payload.StopHearingMaracaPayload;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.FireBlock;
import net.minecraft.world.level.block.SoundType;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.material.MapColor;
import net.minecraft.world.level.material.PushReaction;
import net.neoforged.fml.ModList;
import net.neoforged.fml.loading.FMLLoader;
import net.neoforged.fml.loading.FMLPaths;
import net.neoforged.neoforge.network.PacketDistributor;

import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
import java.util.function.Supplier;

public class NeoForgePlatformHelper implements IPlatformHelper {
    @Override
    public String getPlatformName() {
        return "NeoForge";
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
    public Path getConfigPath() {
        return FMLPaths.CONFIGDIR.get();
    }

    @Override
    public void onRibbitStartMusicGoal(ServerLevel serverLevel, RibbitEntity newRibbit, RibbitEntity masterRibbit) {
        // If this ribbit is the master ribbit, use its stored tick value, since there is no existing ticking sound to grab the byte offset from.
        // Otherwise, use -1 to indicate that the client should use a byte offset instead, which will be fetched from the existing ticking sound.
        int tickOffset = newRibbit.equals(masterRibbit) ? masterRibbit.getTicksPlayingMusic() : -1;
        RibbitStartMusicSinglePayload payload = new RibbitStartMusicSinglePayload(
                newRibbit.getUUID(),
                newRibbit.getRibbitData().getInstrument().getId(),
                tickOffset);
        PacketDistributor.sendToAllPlayers(payload);
    }

    @Override
    public void onPlayerEnterBandRange(ServerPlayer player, ServerLevel serverLevel, RibbitEntity masterRibbit) {
        List<RibbitEntity> ribbitsPlayingMusic = masterRibbit.getRibbitsPlayingMusic().stream().toList();

        List<UUID> ribbitIds = new ArrayList<>();
        ribbitIds.add(masterRibbit.getUUID());
        ribbitIds.addAll(ribbitsPlayingMusic.stream().map(RibbitEntity::getUUID).toList());

        List<ResourceLocation> instrumentIds = new ArrayList<>();
        instrumentIds.add(masterRibbit.getRibbitData().getInstrument().getId());
        instrumentIds.addAll(ribbitsPlayingMusic.stream().map((ribbit) -> ribbit.getRibbitData().getInstrument().getId()).toList());

        RibbitStartMusicAllPayload payload = new RibbitStartMusicAllPayload(
                ribbitIds,
                instrumentIds,
                masterRibbit.getTicksPlayingMusic()
        );

        PacketDistributor.sendToPlayer(player, payload);
    }

    @Override
    public void onPlayerExitBandRange(ServerPlayer player, ServerLevel serverLevel, RibbitEntity masterRibbit) {
        PacketDistributor.sendToPlayer(player, new RibbitStopMusicSinglePayload(masterRibbit.getUUID()));
        for (RibbitEntity ribbit : masterRibbit.getRibbitsPlayingMusic()) {
            PacketDistributor.sendToPlayer(player, new RibbitStopMusicSinglePayload(ribbit.getUUID()));
        }
    }

    @Override
    public void startHearingMaraca(ServerPlayer performer, ServerPlayer audienceMember) {
        PacketDistributor.sendToPlayer(audienceMember, new StartHearingMaracaPayload(performer.getUUID()));
    }

    @Override
    public void stopHearingMaraca(ServerPlayer performer, ServerPlayer audienceMember) {
        PacketDistributor.sendToPlayer(audienceMember, new StopHearingMaracaPayload(performer.getUUID()));
    }

    @Override
    public Supplier<Block> getGiantLilyPadBlock() {
        return () -> new GiantLilyPadBlockNeoForge(
                BlockBehaviour.Properties
                        .of()
                        .mapColor(MapColor.PLANT)
                        .instabreak()
                        .sound(SoundType.LILY_PAD)
                        .noOcclusion()
                        .pushReaction(PushReaction.DESTROY));
    }

    @Override
    public Supplier<Item> getRibbitSpawnEggItem(RibbitProfession profession, int backgroundColor, int highlightColor) {
        return () -> new RibbitSpawnEggItemNeoForge(
                EntityTypeModule.RIBBIT.get(), profession, backgroundColor, highlightColor, new Item.Properties());
    }

    @Override
    public void setBlockAsFlammable(Block block, int igniteChance, int burnChance) {
        FireBlock fireBlock = (FireBlock) Blocks.FIRE;
        fireBlock.setFlammable(block, igniteChance, burnChance);
    }
}
