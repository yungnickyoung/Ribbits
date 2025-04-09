package com.yungnickyoung.minecraft.ribbits.module;

import com.yungnickyoung.minecraft.ribbits.network.ClientPacketHandlerNeoForge;
import com.yungnickyoung.minecraft.ribbits.network.ServerPacketHandlerNeoForge;
import com.yungnickyoung.minecraft.ribbits.network.payload.RequestSupporterHatStatePayload;
import com.yungnickyoung.minecraft.ribbits.network.payload.RibbitStartMusicAllPayload;
import com.yungnickyoung.minecraft.ribbits.network.payload.RibbitStartMusicSinglePayload;
import com.yungnickyoung.minecraft.ribbits.network.payload.RibbitStopMusicSinglePayload;
import com.yungnickyoung.minecraft.ribbits.network.payload.StartHearingMaracaPayload;
import com.yungnickyoung.minecraft.ribbits.network.payload.StopHearingMaracaPayload;
import com.yungnickyoung.minecraft.ribbits.network.payload.ToggleSupporterHatPayload;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.neoforge.network.event.RegisterPayloadHandlersEvent;
import net.neoforged.neoforge.network.handling.DirectionalPayloadHandler;
import net.neoforged.neoforge.network.registration.PayloadRegistrar;

public class NetworkModuleNeoForge {
    private static final String PROTOCOL_VERSION = "1";

    public static void init(IEventBus eventBus) {
        eventBus.addListener(NetworkModuleNeoForge::registerHandlers);
    }

    private static void registerHandlers(final RegisterPayloadHandlersEvent event) {
        final PayloadRegistrar registrar = event.registrar(PROTOCOL_VERSION);
        registrar.playToClient(
                RibbitStartMusicSinglePayload.TYPE,
                RibbitStartMusicSinglePayload.STREAM_CODEC,
                ClientPacketHandlerNeoForge::handleStartMusicSinglePayload
        );
        registrar.playToClient(
                RibbitStopMusicSinglePayload.TYPE,
                RibbitStopMusicSinglePayload.STREAM_CODEC,
                ClientPacketHandlerNeoForge::handleStopMusicSinglePayload
        );
        registrar.playToClient(
                RibbitStartMusicAllPayload.TYPE,
                RibbitStartMusicAllPayload.STREAM_CODEC,
                ClientPacketHandlerNeoForge::handleStartMusicAllPayload
        );
        registrar.playToClient(
                StartHearingMaracaPayload.TYPE,
                StartHearingMaracaPayload.STREAM_CODEC,
                ClientPacketHandlerNeoForge::handleStartHearingMaracaPayload
        );
        registrar.playToClient(
                StopHearingMaracaPayload.TYPE,
                StopHearingMaracaPayload.STREAM_CODEC,
                ClientPacketHandlerNeoForge::handleStopHearingMaracaPayload
        );
        registrar.playToClient(
                RequestSupporterHatStatePayload.TYPE,
                RequestSupporterHatStatePayload.STREAM_CODEC,
                ClientPacketHandlerNeoForge::handleRequestSupporterHatStatePayload
        );
        registrar.playBidirectional(
                ToggleSupporterHatPayload.TYPE,
                ToggleSupporterHatPayload.STREAM_CODEC,
                new DirectionalPayloadHandler<>(
                        ClientPacketHandlerNeoForge::handleToggleSupporterHatPayload,
                        ServerPacketHandlerNeoForge::handleToggleSupporterHatPayload
                )
        );
    }
}
