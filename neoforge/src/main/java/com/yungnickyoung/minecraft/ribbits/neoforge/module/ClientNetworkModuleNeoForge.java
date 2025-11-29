package com.yungnickyoung.minecraft.ribbits.neoforge.module;

import com.yungnickyoung.minecraft.ribbits.RibbitsCommon;
import com.yungnickyoung.minecraft.ribbits.network.ClientNetworkHandler;
import com.yungnickyoung.minecraft.ribbits.network.payload.*;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.client.network.event.RegisterClientPayloadHandlersEvent;

@EventBusSubscriber(modid = RibbitsCommon.MOD_ID, value = Dist.CLIENT)
public class ClientNetworkModuleNeoForge {
    @SubscribeEvent
    public static void registerClientPayloadHandlers(RegisterClientPayloadHandlersEvent event) {
        event.register(RibbitStartMusicSinglePayload.TYPE, (payload, context) -> ClientNetworkHandler.handleStartMusicSingleS2C(payload));
        event.register(RibbitStopMusicSinglePayload.TYPE, (payload, context) -> ClientNetworkHandler.handleStopMusicSingleS2C(payload));
        event.register(RibbitStartMusicAllPayload.TYPE, (payload, context) -> ClientNetworkHandler.handleStartMusicAllS2C(payload));
        event.register(StartHearingMaracaPayload.TYPE, (payload, context) -> ClientNetworkHandler.handleStartHearingMaracaS2C(payload));
        event.register(StopHearingMaracaPayload.TYPE, (payload, context) -> ClientNetworkHandler.handleStopHearingMaracaS2C(payload));
        event.register(RequestSupporterHatStatePayload.TYPE, (payload, context) -> ClientNetworkHandler.handleRequestSupporterHatStateS2C(payload));
        event.register(ToggleSupporterHatPayloadS2C.TYPE, (payload, context) -> ClientNetworkHandler.handleToggleSupporterHatS2C(payload));
    }
}
