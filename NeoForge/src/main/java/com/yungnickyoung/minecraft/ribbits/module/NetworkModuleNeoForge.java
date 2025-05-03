package com.yungnickyoung.minecraft.ribbits.module;

import com.yungnickyoung.minecraft.ribbits.network.ClientPacketHandlerNeoForge;
import com.yungnickyoung.minecraft.ribbits.network.ServerPacketHandlerNeoForge;
import com.yungnickyoung.minecraft.ribbits.network.payload.RequestSupporterHatStatePayload;
import com.yungnickyoung.minecraft.ribbits.network.payload.RibbitStartMusicAllPayload;
import com.yungnickyoung.minecraft.ribbits.network.payload.RibbitStartMusicSinglePayload;
import com.yungnickyoung.minecraft.ribbits.network.payload.RibbitStopMusicSinglePayload;
import com.yungnickyoung.minecraft.ribbits.network.payload.StartHearingMaracaPayload;
import com.yungnickyoung.minecraft.ribbits.network.payload.StopHearingMaracaPayload;
import com.yungnickyoung.minecraft.ribbits.network.payload.ToggleSupporterHatPayloadC2S;
import com.yungnickyoung.minecraft.ribbits.network.payload.ToggleSupporterHatPayloadS2C;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.neoforge.network.event.RegisterPayloadHandlersEvent;
import net.neoforged.neoforge.network.handling.IPayloadContext;
import net.neoforged.neoforge.network.handling.IPayloadHandler;
import net.neoforged.neoforge.network.registration.PayloadRegistrar;

import java.util.function.BiConsumer;

public class NetworkModuleNeoForge {
    private static final String PROTOCOL_VERSION = "1";

    public static void init(IEventBus eventBus) {
        eventBus.addListener(NetworkModuleNeoForge::registerServerHandlers);
        eventBus.addListener(NetworkModuleNeoForge::registerClientHandlers);
    }

    private static void registerClientHandlers(final RegisterPayloadHandlersEvent event) {
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
        registrar.playToClient(
                ToggleSupporterHatPayloadS2C.TYPE,
                ToggleSupporterHatPayloadS2C.STREAM_CODEC,
                ClientPacketHandlerNeoForge::handleToggleSupporterHatPayload
        );
    }

    private static void registerServerHandlers(final RegisterPayloadHandlersEvent event) {
        final PayloadRegistrar registrar = event.registrar(PROTOCOL_VERSION);
        registrar.playToServer(
                ToggleSupporterHatPayloadC2S.TYPE,
                ToggleSupporterHatPayloadC2S.STREAM_CODEC,
                ServerPacketHandlerNeoForge::handleToggleSupporterHatPayload
        );
    }

    private static <T extends CustomPacketPayload> IPayloadHandler<T> wrapClientHandler(BiConsumer<T, IPayloadContext> consumer) {
        return (payload, payloadContext) -> {
            payloadContext.enqueueWork(() -> {
                consumer.accept(payload, payloadContext);
            });
        };
    }
}
