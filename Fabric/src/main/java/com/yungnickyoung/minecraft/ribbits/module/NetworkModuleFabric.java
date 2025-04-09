package com.yungnickyoung.minecraft.ribbits.module;

import com.yungnickyoung.minecraft.ribbits.network.ClientPacketHandlerFabric;
import com.yungnickyoung.minecraft.ribbits.network.ServerPacketHandlerFabric;
import com.yungnickyoung.minecraft.ribbits.network.payload.RequestSupporterHatStatePayload;
import com.yungnickyoung.minecraft.ribbits.network.payload.RibbitStartMusicAllPayload;
import com.yungnickyoung.minecraft.ribbits.network.payload.RibbitStartMusicSinglePayload;
import com.yungnickyoung.minecraft.ribbits.network.payload.RibbitStopMusicSinglePayload;
import com.yungnickyoung.minecraft.ribbits.network.payload.StartHearingMaracaPayload;
import com.yungnickyoung.minecraft.ribbits.network.payload.StopHearingMaracaPayload;
import com.yungnickyoung.minecraft.ribbits.network.payload.ToggleSupporterHatPayload;
import net.fabricmc.fabric.api.client.networking.v1.ClientPlayNetworking;
import net.fabricmc.fabric.api.networking.v1.PayloadTypeRegistry;
import net.fabricmc.fabric.api.networking.v1.ServerPlayNetworking;

public class NetworkModuleFabric {
    public static void init() {
        PayloadTypeRegistry.playS2C().register(RibbitStartMusicSinglePayload.TYPE, RibbitStartMusicSinglePayload.STREAM_CODEC);
        PayloadTypeRegistry.playS2C().register(RibbitStopMusicSinglePayload.TYPE, RibbitStopMusicSinglePayload.STREAM_CODEC);
        PayloadTypeRegistry.playS2C().register(RibbitStartMusicAllPayload.TYPE, RibbitStartMusicAllPayload.STREAM_CODEC);
        PayloadTypeRegistry.playS2C().register(StartHearingMaracaPayload.TYPE, StartHearingMaracaPayload.STREAM_CODEC);
        PayloadTypeRegistry.playS2C().register(StopHearingMaracaPayload.TYPE, StopHearingMaracaPayload.STREAM_CODEC);
        PayloadTypeRegistry.playS2C().register(RequestSupporterHatStatePayload.TYPE, RequestSupporterHatStatePayload.STREAM_CODEC);
        PayloadTypeRegistry.playS2C().register(ToggleSupporterHatPayload.TYPE, ToggleSupporterHatPayload.STREAM_CODEC);
        PayloadTypeRegistry.playC2S().register(ToggleSupporterHatPayload.TYPE, ToggleSupporterHatPayload.STREAM_CODEC);
    }

    public static void registerC2SHandlers() {
        ServerPlayNetworking.registerGlobalReceiver(ToggleSupporterHatPayload.TYPE, ServerPacketHandlerFabric::receiveToggleSupporterHat);
    }

    public static void registerS2CHandlers() {
        ClientPlayNetworking.registerGlobalReceiver(RibbitStartMusicSinglePayload.TYPE, ClientPacketHandlerFabric::handleStartMusicSinglePayload);
        ClientPlayNetworking.registerGlobalReceiver(RibbitStopMusicSinglePayload.TYPE, ClientPacketHandlerFabric::handleStopMusicSinglePayload);
        ClientPlayNetworking.registerGlobalReceiver(RibbitStartMusicAllPayload.TYPE, ClientPacketHandlerFabric::handleStartMusicAllPayload);
        ClientPlayNetworking.registerGlobalReceiver(StartHearingMaracaPayload.TYPE, ClientPacketHandlerFabric::handleStartHearingMaracaPayload);
        ClientPlayNetworking.registerGlobalReceiver(StopHearingMaracaPayload.TYPE, ClientPacketHandlerFabric::handleStopHearingMaracaPayload);
        ClientPlayNetworking.registerGlobalReceiver(RequestSupporterHatStatePayload.TYPE, ClientPacketHandlerFabric::handleRequestSupporterHatStatePayload);

        // ToggleSupporterPacket is handled in both the client and server handlers.
        // When received on the server, it will update the server's SupportersListServer and forward the payload to all clients.
        // When received on the client, it will update the player's local SupportersListClient.
        ClientPlayNetworking.registerGlobalReceiver(ToggleSupporterHatPayload.TYPE, ClientPacketHandlerFabric::handleToggleSupporterHatPayload);
    }
}
