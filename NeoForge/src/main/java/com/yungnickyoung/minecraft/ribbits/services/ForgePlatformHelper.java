package com.yungnickyoung.minecraft.ribbits.services;

import com.yungnickyoung.minecraft.ribbits.block.GiantLilyPadBlockForge;
import com.yungnickyoung.minecraft.ribbits.data.RibbitProfession;
import com.yungnickyoung.minecraft.ribbits.entity.RibbitEntity;
import com.yungnickyoung.minecraft.ribbits.item.RibbitSpawnEggItemForge;
import com.yungnickyoung.minecraft.ribbits.module.EntityTypeModule;
import com.yungnickyoung.minecraft.ribbits.module.NetworkModuleNeoForge;
import com.yungnickyoung.minecraft.ribbits.network.PlayerMusicStartS2CPacket;
import com.yungnickyoung.minecraft.ribbits.network.PlayerMusicStopS2CPacket;
import com.yungnickyoung.minecraft.ribbits.network.RibbitMusicStartAllS2CPacket;
import com.yungnickyoung.minecraft.ribbits.network.RibbitMusicStartSingleS2CPacket;
import com.yungnickyoung.minecraft.ribbits.network.RibbitMusicStopSingleS2CPacket;
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

import java.nio.file.Path;
import java.util.function.Supplier;

public class ForgePlatformHelper implements IPlatformHelper {
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
        NetworkModuleNeoForge.sendToAllClients(new RibbitMusicStartSingleS2CPacket(newRibbit.getUUID(), newRibbit.getRibbitData().getInstrument().getId(), tickOffset));
    }

    @Override
    public void onPlayerEnterBandRange(ServerPlayer player, ServerLevel serverLevel, RibbitEntity masterRibbit) {
        NetworkModuleNeoForge.sendToClient(new RibbitMusicStartAllS2CPacket(masterRibbit, masterRibbit.getTicksPlayingMusic()), player);
    }

    @Override
    public void onPlayerExitBandRange(ServerPlayer player, ServerLevel serverLevel, RibbitEntity masterRibbit) {
        NetworkModuleNeoForge.sendToClient(new RibbitMusicStopSingleS2CPacket(masterRibbit.getUUID()), player);

        for (RibbitEntity ribbit : masterRibbit.getRibbitsPlayingMusic()) {
            NetworkModuleNeoForge.sendToClient(new RibbitMusicStopSingleS2CPacket(ribbit.getUUID()), player);
        }
    }

    @Override
    public void startHearingMaraca(ServerPlayer performer, ServerPlayer audienceMember) {
        NetworkModuleNeoForge.sendToClient(new PlayerMusicStartS2CPacket(performer.getUUID()), audienceMember);
    }

    @Override
    public void stopHearingMaraca(ServerPlayer performer, ServerPlayer audienceMember) {
        NetworkModuleNeoForge.sendToClient(new PlayerMusicStopS2CPacket(performer.getUUID()), audienceMember);

    }

    @Override
    public Supplier<Block> getGiantLilyPadBlock() {
        return () -> new GiantLilyPadBlockForge(
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
        return () -> new RibbitSpawnEggItemForge(
                EntityTypeModule.RIBBIT.get(), profession, backgroundColor, highlightColor, new Item.Properties());
    }

    @Override
    public void setBlockAsFlammable(Block block, int igniteChance, int burnChance) {
        FireBlock fireBlock = (FireBlock) Blocks.FIRE;
        fireBlock.setFlammable(block, igniteChance, burnChance);
    }
}
