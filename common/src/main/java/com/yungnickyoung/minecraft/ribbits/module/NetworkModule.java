package com.yungnickyoung.minecraft.ribbits.module;

import com.yungnickyoung.minecraft.ribbits.client.supporters.SupportersJSON;
import com.yungnickyoung.minecraft.ribbits.network.ClientNetworkHandler;
import com.yungnickyoung.minecraft.ribbits.network.ServerNetworkHandler;
import com.yungnickyoung.minecraft.ribbits.network.payload.*;
import dev.architectury.networking.NetworkManager;
import dev.architectury.platform.Platform;
import dev.architectury.utils.Env;

public class NetworkModule {
    public static void init() {
        SupportersJSON.populateSupportersList();

        // C2S receivers
        NetworkManager.registerReceiver(NetworkManager.Side.C2S, ToggleSupporterHatPayloadC2S.TYPE, ToggleSupporterHatPayloadC2S.STREAM_CODEC,
                ServerNetworkHandler::handleToggleSupporterHatC2S);

        // S2C senders (register types and codecs for server-side encoding)
        if (Platform.getEnvironment() == Env.SERVER) {
            NetworkManager.registerS2CPayloadType(RibbitStartMusicSinglePayload.TYPE, RibbitStartMusicSinglePayload.STREAM_CODEC);
            NetworkManager.registerS2CPayloadType(RibbitStopMusicSinglePayload.TYPE, RibbitStopMusicSinglePayload.STREAM_CODEC);
            NetworkManager.registerS2CPayloadType(RibbitStartMusicAllPayload.TYPE, RibbitStartMusicAllPayload.STREAM_CODEC);
            NetworkManager.registerS2CPayloadType(StartHearingMaracaPayload.TYPE, StartHearingMaracaPayload.STREAM_CODEC);
            NetworkManager.registerS2CPayloadType(StopHearingMaracaPayload.TYPE, StopHearingMaracaPayload.STREAM_CODEC);
            NetworkManager.registerS2CPayloadType(RequestSupporterHatStatePayload.TYPE, RequestSupporterHatStatePayload.STREAM_CODEC);
            NetworkManager.registerS2CPayloadType(ToggleSupporterHatPayloadS2C.TYPE, ToggleSupporterHatPayloadS2C.STREAM_CODEC);
        }
    }

    public static void initClient() {
        // S2C receivers
        NetworkManager.registerReceiver(NetworkManager.Side.S2C, RibbitStartMusicSinglePayload.TYPE, RibbitStartMusicSinglePayload.STREAM_CODEC,
                (payload, ctx) -> ClientNetworkHandler.handleStartMusicSingleS2C(payload));
        NetworkManager.registerReceiver(NetworkManager.Side.S2C, RibbitStopMusicSinglePayload.TYPE, RibbitStopMusicSinglePayload.STREAM_CODEC,
                (payload, ctx) -> ClientNetworkHandler.handleStopMusicSingleS2C(payload));
        NetworkManager.registerReceiver(NetworkManager.Side.S2C, RibbitStartMusicAllPayload.TYPE, RibbitStartMusicAllPayload.STREAM_CODEC,
                (payload, ctx) -> ClientNetworkHandler.handleStartMusicAllS2C(payload));
        NetworkManager.registerReceiver(NetworkManager.Side.S2C, StartHearingMaracaPayload.TYPE, StartHearingMaracaPayload.STREAM_CODEC,
                (payload, ctx) -> ClientNetworkHandler.handleStartHearingMaracaS2C(payload));
        NetworkManager.registerReceiver(NetworkManager.Side.S2C, StopHearingMaracaPayload.TYPE, StopHearingMaracaPayload.STREAM_CODEC,
                (payload, ctx) -> ClientNetworkHandler.handleStopHearingMaracaS2C(payload));
        NetworkManager.registerReceiver(NetworkManager.Side.S2C, RequestSupporterHatStatePayload.TYPE, RequestSupporterHatStatePayload.STREAM_CODEC,
                (payload, ctx) -> ClientNetworkHandler.handleRequestSupporterHatStateS2C(payload));
        NetworkManager.registerReceiver(NetworkManager.Side.S2C, ToggleSupporterHatPayloadS2C.TYPE, ToggleSupporterHatPayloadS2C.STREAM_CODEC,
                (payload, ctx) -> ClientNetworkHandler.handleToggleSupporterHatS2C(payload));
    }
}