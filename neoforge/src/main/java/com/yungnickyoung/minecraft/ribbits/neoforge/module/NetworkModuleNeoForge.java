package com.yungnickyoung.minecraft.ribbits.neoforge.module;

import com.yungnickyoung.minecraft.ribbits.RibbitsCommon;
import com.yungnickyoung.minecraft.ribbits.network.ServerNetworkHandler;
import com.yungnickyoung.minecraft.ribbits.network.payload.*;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.network.event.RegisterPayloadHandlersEvent;

@EventBusSubscriber(modid = RibbitsCommon.MOD_ID)
public class NetworkModuleNeoForge {

    private static final String PROTOCOL_VERSION = "1";

    @SubscribeEvent
    public static void registerPayloadHandlers(RegisterPayloadHandlersEvent event) {
        var registrar = event.registrar(PROTOCOL_VERSION);
        registrar.playToClient(RibbitStartMusicSinglePayload.TYPE, RibbitStartMusicSinglePayload.STREAM_CODEC);
        registrar.playToClient(RibbitStopMusicSinglePayload.TYPE, RibbitStopMusicSinglePayload.STREAM_CODEC);
        registrar.playToClient(RibbitStartMusicAllPayload.TYPE, RibbitStartMusicAllPayload.STREAM_CODEC);
        registrar.playToClient(StartHearingMaracaPayload.TYPE, StartHearingMaracaPayload.STREAM_CODEC);
        registrar.playToClient(StopHearingMaracaPayload.TYPE, StopHearingMaracaPayload.STREAM_CODEC);
        registrar.playToClient(RequestSupporterHatStatePayload.TYPE, RequestSupporterHatStatePayload.STREAM_CODEC);
        registrar.playToClient(ToggleSupporterHatPayloadS2C.TYPE, ToggleSupporterHatPayloadS2C.STREAM_CODEC);

        registrar.playToServer(ToggleSupporterHatPayloadC2S.TYPE, ToggleSupporterHatPayloadC2S.STREAM_CODEC,
                (payload, context) -> ServerNetworkHandler.handleToggleSupporterHatC2S(payload));
    }
}
