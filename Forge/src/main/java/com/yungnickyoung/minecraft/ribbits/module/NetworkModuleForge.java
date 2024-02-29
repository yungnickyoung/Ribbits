package com.yungnickyoung.minecraft.ribbits.module;

import com.yungnickyoung.minecraft.ribbits.RibbitsCommon;
import com.yungnickyoung.minecraft.ribbits.network.RibbitMusicStartAllS2CPacket;
import com.yungnickyoung.minecraft.ribbits.network.RibbitMusicStartSingleS2CPacket;
import com.yungnickyoung.minecraft.ribbits.network.RibbitMusicStopSingleS2CPacket;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.chunk.LevelChunk;
import net.minecraftforge.fml.event.lifecycle.FMLCommonSetupEvent;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;
import net.minecraftforge.network.NetworkDirection;
import net.minecraftforge.network.NetworkRegistry;
import net.minecraftforge.network.PacketDistributor;
import net.minecraftforge.network.simple.SimpleChannel;

public class NetworkModuleForge {
    private static final String PROTOCOL_VERSION = "1.0";
    private static SimpleChannel INSTANCE;

    private static int packetId = 0;
    private static int id() {
        return packetId++;
    }

    public static void init() {
        FMLJavaModLoadingContext.get().getModEventBus().addListener(NetworkModuleForge::commonSetup);
    }

    private static void commonSetup(FMLCommonSetupEvent event) {
        event.enqueueWork(() -> {
            INSTANCE = NetworkRegistry.ChannelBuilder
                    .named(new ResourceLocation(RibbitsCommon.MOD_ID, "messages"))
                    .networkProtocolVersion(() -> PROTOCOL_VERSION)
                    .clientAcceptedVersions(PROTOCOL_VERSION::equals)
                    .serverAcceptedVersions(PROTOCOL_VERSION::equals)
                    .simpleChannel();

            INSTANCE.messageBuilder(RibbitMusicStartSingleS2CPacket.class, id(), NetworkDirection.PLAY_TO_CLIENT)
                    .decoder(RibbitMusicStartSingleS2CPacket::new)
                    .encoder(RibbitMusicStartSingleS2CPacket::toBytes)
                    .consumerMainThread(RibbitMusicStartSingleS2CPacket::handle)
                    .add();

            INSTANCE.messageBuilder(RibbitMusicStartAllS2CPacket.class, id(), NetworkDirection.PLAY_TO_CLIENT)
                    .decoder(RibbitMusicStartAllS2CPacket::new)
                    .encoder(RibbitMusicStartAllS2CPacket::toBytes)
                    .consumerMainThread(RibbitMusicStartAllS2CPacket::handle)
                    .add();

            INSTANCE.messageBuilder(RibbitMusicStopSingleS2CPacket.class, id(), NetworkDirection.PLAY_TO_CLIENT)
                    .decoder(RibbitMusicStopSingleS2CPacket::new)
                    .encoder(RibbitMusicStopSingleS2CPacket::toBytes)
                    .consumerMainThread(RibbitMusicStopSingleS2CPacket::handle)
                    .add();
        });
    }

    public static <MSG> void sendToServer(MSG message) {
        INSTANCE.sendToServer(message);
    }

    public static <MSG> void sendToClient(MSG message, ServerPlayer player) {
        INSTANCE.send(PacketDistributor.PLAYER.with(() -> player), message);
    }

    public static <MSG> void sendToAllClients(MSG message) {
        INSTANCE.send(PacketDistributor.ALL.noArg(), message);
    }

    public static <MSG> void sendToClientsTrackingChunk(MSG message, LevelChunk chunk) {
        INSTANCE.send(PacketDistributor.TRACKING_CHUNK.with(() -> chunk), message);
    }

    public static <MSG> void sendToClientsInLevel(MSG message, ResourceKey<Level> levelResourceKey) {
        INSTANCE.send(PacketDistributor.DIMENSION.with(() -> levelResourceKey), message);
    }
}
