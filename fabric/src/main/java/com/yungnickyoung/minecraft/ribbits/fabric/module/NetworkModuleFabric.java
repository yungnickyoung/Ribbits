package com.yungnickyoung.minecraft.ribbits.fabric.module;

import com.yungnickyoung.minecraft.ribbits.network.ClientNetworkHandler;
import com.yungnickyoung.minecraft.ribbits.network.ServerNetworkHandler;
import com.yungnickyoung.minecraft.ribbits.network.payload.*;
import net.fabricmc.fabric.api.client.networking.v1.ClientPlayNetworking;
import net.fabricmc.fabric.api.networking.v1.PayloadTypeRegistry;
import net.fabricmc.fabric.api.networking.v1.ServerPlayNetworking;

public class NetworkModuleFabric {
    public static void register() {
        PayloadTypeRegistry.playS2C().register(RibbitStartMusicSinglePayload.TYPE, RibbitStartMusicSinglePayload.STREAM_CODEC);
        PayloadTypeRegistry.playS2C().register(RibbitStopMusicSinglePayload.TYPE, RibbitStopMusicSinglePayload.STREAM_CODEC);
        PayloadTypeRegistry.playS2C().register(RibbitStartMusicAllPayload.TYPE, RibbitStartMusicAllPayload.STREAM_CODEC);
        PayloadTypeRegistry.playS2C().register(StartHearingMaracaPayload.TYPE, StartHearingMaracaPayload.STREAM_CODEC);
        PayloadTypeRegistry.playS2C().register(StopHearingMaracaPayload.TYPE, StopHearingMaracaPayload.STREAM_CODEC);
        PayloadTypeRegistry.playS2C().register(RequestSupporterHatStatePayload.TYPE, RequestSupporterHatStatePayload.STREAM_CODEC);
        PayloadTypeRegistry.playS2C().register(ToggleSupporterHatPayloadS2C.TYPE, ToggleSupporterHatPayloadS2C.STREAM_CODEC);
        PayloadTypeRegistry.playC2S().register(ToggleSupporterHatPayloadC2S.TYPE, ToggleSupporterHatPayloadC2S.STREAM_CODEC);

        ServerPlayNetworking.registerGlobalReceiver(ToggleSupporterHatPayloadC2S.TYPE, (payload, context) ->
                ServerNetworkHandler.handleToggleSupporterHatC2S(payload));
    }

    public static void registerClient() {
        ClientPlayNetworking.registerGlobalReceiver(RibbitStartMusicSinglePayload.TYPE, (payload, context) ->
                ClientNetworkHandler.handleStartMusicSingleS2C(payload));
        ClientPlayNetworking.registerGlobalReceiver(RibbitStopMusicSinglePayload.TYPE, (payload, context) ->
                ClientNetworkHandler.handleStopMusicSingleS2C(payload));
        ClientPlayNetworking.registerGlobalReceiver(RibbitStartMusicAllPayload.TYPE, (payload, context) ->
                ClientNetworkHandler.handleStartMusicAllS2C(payload));
        ClientPlayNetworking.registerGlobalReceiver(StartHearingMaracaPayload.TYPE, (payload, context) ->
                ClientNetworkHandler.handleStartHearingMaracaS2C(payload));
        ClientPlayNetworking.registerGlobalReceiver(StopHearingMaracaPayload.TYPE, (payload, context) ->
                ClientNetworkHandler.handleStopHearingMaracaS2C(payload));
        ClientPlayNetworking.registerGlobalReceiver(RequestSupporterHatStatePayload.TYPE, (payload, context) ->
                ClientNetworkHandler.handleRequestSupporterHatStateS2C(payload));
        ClientPlayNetworking.registerGlobalReceiver(ToggleSupporterHatPayloadS2C.TYPE, (payload, context) ->
                ClientNetworkHandler.handleToggleSupporterHatS2C(payload));
    }
}
