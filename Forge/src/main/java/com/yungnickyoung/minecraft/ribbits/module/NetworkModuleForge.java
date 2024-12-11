package com.yungnickyoung.minecraft.ribbits.module;

import com.yungnickyoung.minecraft.ribbits.RibbitsCommon;
import com.yungnickyoung.minecraft.ribbits.network.*;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.chunk.LevelChunk;
import net.minecraftforge.fml.event.lifecycle.FMLCommonSetupEvent;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;
import net.minecraftforge.network.*;

public class NetworkModuleForge {
    private static final int PROTOCOL_VERSION = 1;
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
            INSTANCE = ChannelBuilder
                    .named(new ResourceLocation(RibbitsCommon.MOD_ID, "messages"))
                    .networkProtocolVersion(PROTOCOL_VERSION)
                    .clientAcceptedVersions(Channel.VersionTest.exact(PROTOCOL_VERSION))
                    .serverAcceptedVersions(Channel.VersionTest.exact(PROTOCOL_VERSION))
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

            INSTANCE.messageBuilder(PlayerMusicStartS2CPacket.class, id(), NetworkDirection.PLAY_TO_CLIENT)
                    .decoder(PlayerMusicStartS2CPacket::new)
                    .encoder(PlayerMusicStartS2CPacket::toBytes)
                    .consumerMainThread(PlayerMusicStartS2CPacket::handle)
                    .add();

            INSTANCE.messageBuilder(PlayerMusicStopS2CPacket.class, id(), NetworkDirection.PLAY_TO_CLIENT)
                    .decoder(PlayerMusicStopS2CPacket::new)
                    .encoder(PlayerMusicStopS2CPacket::toBytes)
                    .consumerMainThread(PlayerMusicStopS2CPacket::handle)
                    .add();

            INSTANCE.messageBuilder(ToggleSupporterHatC2SPacket.class, id(), NetworkDirection.PLAY_TO_SERVER)
                    .decoder(ToggleSupporterHatC2SPacket::new)
                    .encoder(ToggleSupporterHatC2SPacket::toBytes)
                    .consumerMainThread(ToggleSupporterHatC2SPacket::handle)
                    .add();

            INSTANCE.messageBuilder(ToggleSupporterHatS2CPacket.class, id(), NetworkDirection.PLAY_TO_CLIENT)
                    .decoder(ToggleSupporterHatS2CPacket::new)
                    .encoder(ToggleSupporterHatS2CPacket::toBytes)
                    .consumerMainThread(ToggleSupporterHatS2CPacket::handle)
                    .add();

            INSTANCE.messageBuilder(RequestSupporterHatStateS2CPacket.class, id(), NetworkDirection.PLAY_TO_CLIENT)
                    .decoder(RequestSupporterHatStateS2CPacket::new)
                    .encoder(RequestSupporterHatStateS2CPacket::toBytes)
                    .consumerMainThread(RequestSupporterHatStateS2CPacket::handle)
                    .add();
        });
    }

    public static <MSG> void sendToServer(MSG message) {
        INSTANCE.send(message, PacketDistributor.SERVER.noArg());
    }

    public static <MSG> void sendToClient(MSG message, ServerPlayer player) {
        INSTANCE.send(message, PacketDistributor.PLAYER.with(player));
    }

    public static <MSG> void sendToAllClients(MSG message) {
        INSTANCE.send(message, PacketDistributor.ALL.noArg());
    }

    public static <MSG> void sendToClientsTrackingChunk(MSG message, LevelChunk chunk) {
        INSTANCE.send(message, PacketDistributor.TRACKING_CHUNK.with(chunk));
    }

    public static <MSG> void sendToClientsInLevel(MSG message, ResourceKey<Level> levelResourceKey) {
        INSTANCE.send(message, PacketDistributor.DIMENSION.with(levelResourceKey));
    }
}
