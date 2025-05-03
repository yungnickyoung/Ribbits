package com.yungnickyoung.minecraft.ribbits.network;

import com.yungnickyoung.minecraft.ribbits.RibbitsCommon;
import com.yungnickyoung.minecraft.ribbits.client.ClientUtils;
import com.yungnickyoung.minecraft.ribbits.client.supporters.RibbitOptionsJSON;
import com.yungnickyoung.minecraft.ribbits.client.supporters.SupportersListClient;
import com.yungnickyoung.minecraft.ribbits.data.RibbitInstrument;
import com.yungnickyoung.minecraft.ribbits.entity.RibbitEntity;
import com.yungnickyoung.minecraft.ribbits.mixin.mixins.client.accessor.ClientLevelAccessor;
import com.yungnickyoung.minecraft.ribbits.module.RibbitInstrumentModule;
import com.yungnickyoung.minecraft.ribbits.network.payload.RequestSupporterHatStatePayload;
import com.yungnickyoung.minecraft.ribbits.network.payload.RibbitStartMusicAllPayload;
import com.yungnickyoung.minecraft.ribbits.network.payload.RibbitStartMusicSinglePayload;
import com.yungnickyoung.minecraft.ribbits.network.payload.RibbitStopMusicSinglePayload;
import com.yungnickyoung.minecraft.ribbits.network.payload.StartHearingMaracaPayload;
import com.yungnickyoung.minecraft.ribbits.network.payload.StopHearingMaracaPayload;
import com.yungnickyoung.minecraft.ribbits.network.payload.ToggleSupporterHatPayloadS2C;
import com.yungnickyoung.minecraft.ribbits.services.Services;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.player.Player;
import net.neoforged.neoforge.network.handling.IPayloadContext;

public class ClientPacketHandlerNeoForge {
    public static void handleStartMusicSinglePayload(RibbitStartMusicSinglePayload payload, IPayloadContext context) {
        context.enqueueWork(() -> {
            RibbitEntity ribbit = (RibbitEntity) ((ClientLevelAccessor) context.player().level())
                    .callGetEntities()
                    .get(payload.ribbitUUID());

            RibbitInstrument instrument = RibbitInstrumentModule.getInstrument(payload.instrumentId());

            if (ribbit == null) {
                RibbitsCommon.LOGGER.error("Received Start Music payload for a ribbit with UUID {} that doesn't exist!", payload.ribbitUUID());
                return;
            }

            if (instrument == null) {
                RibbitsCommon.LOGGER.error("Tried to play music for a ribbit with null instrument!");
                return;
            }

            if (instrument == RibbitInstrumentModule.NONE) {
                RibbitsCommon.LOGGER.error("Tried to play music for a ribbit with NONE instrument!");
                return;
            }

            ClientUtils.playSound(ribbit, payload.tickOffset(), instrument);
        });
    }

    public static void handleStartMusicAllPayload(RibbitStartMusicAllPayload payload, IPayloadContext context) {
        if (payload.ribbitUUIDs().size() != payload.instrumentIds().size()) {
            RibbitsCommon.LOGGER.error("Received Start Music All payload with {} ribbits and {} instruments!",
                    payload.ribbitUUIDs().size(), payload.instrumentIds().size());
            return;
        }

        for (int i = 0; i < payload.ribbitUUIDs().size(); i++) {
            RibbitEntity ribbit = (RibbitEntity) ((ClientLevelAccessor) context.player().level())
                    .callGetEntities()
                    .get(payload.ribbitUUIDs().get(i));
            if (ribbit == null) {
                RibbitsCommon.LOGGER.error("Received Start Music All payload for a ribbit with UUID {} that doesn't exist!",
                        payload.ribbitUUIDs().get(i));
                return;
            }

            RibbitInstrument instrument = RibbitInstrumentModule.getInstrument(payload.instrumentIds().get(i));
            if (instrument == null) {
                RibbitsCommon.LOGGER.error("Tried to play music in handleStartAllPacket for a ribbit with null instrument!");
                return;
            }

            if (instrument == RibbitInstrumentModule.NONE) {
                RibbitsCommon.LOGGER.error("Tried to play music in handleStartAllPacket for a ribbit with NONE instrument!");
                return;
            }

            ClientUtils.playSound(ribbit, payload.tickOffset(), instrument);
        }
    }

    public static void handleStopMusicSinglePayload(RibbitStopMusicSinglePayload payload, IPayloadContext context) {
        RibbitEntity ribbit = (RibbitEntity) ((ClientLevelAccessor) context.player().level())
                .callGetEntities()
                .get(payload.ribbitUUID());

        if (ribbit == null) {
            RibbitsCommon.LOGGER.error("Received Stop Music payload for a ribbit with UUID {} that doesn't exist!", payload.ribbitUUID());
            return;
        }

        ClientUtils.stopRibbitSound(payload.ribbitUUID());
    }

    public static void handleStartHearingMaracaPayload(StartHearingMaracaPayload payload, IPayloadContext context) {
        Entity performer = ((ClientLevelAccessor) context.player().level()).callGetEntities().get(payload.performerUUID());

        if (performer == null) {
            RibbitsCommon.LOGGER.error("Received Start Maraca payload for Player performer with UUID {} that doesn't exist!",
                    payload.performerUUID());
            return;
        } else if (!(performer instanceof Player)) {
            RibbitsCommon.LOGGER.error("Received Start Maraca payload for non-Player performer with UUID {}!",
                    payload.performerUUID());
            return;
        }

        ClientUtils.startHearingMaraca((Player) performer);
    }

    public static void handleStopHearingMaracaPayload(StopHearingMaracaPayload payload, IPayloadContext context) {
        Entity performer = ((ClientLevelAccessor) context.player().level()).callGetEntities().get(payload.performerUUID());

        if (performer == null) {
            RibbitsCommon.LOGGER.error("Received Stop Maraca payload for Player performer with UUID {} that doesn't exist!",
                    payload.performerUUID());
            return;
        } else if (!(performer instanceof Player)) {
            RibbitsCommon.LOGGER.error("Received Stop Maraca payload for non-Player performer with UUID {}!",
                    payload.performerUUID());
            return;
        }

        ClientUtils.stopHearingMaraca(payload.performerUUID());
    }

    public static void handleToggleSupporterHatPayload(ToggleSupporterHatPayloadS2C payload, IPayloadContext context) {
        // Update the player's supporter hat status on the client
        SupportersListClient.toggleSupporterHat(payload.playerUUID(), payload.enabled());
    }

    public static void handleRequestSupporterHatStatePayload(RequestSupporterHatStatePayload payload, IPayloadContext context) {
        // Populate local supporter hat list with the list from the server
        SupportersListClient.clear();
        payload.enabledSupporterHatPlayers().forEach(playerUUID ->
                SupportersListClient.toggleSupporterHat(playerUUID, true));

        // Send a ToggleSupporterPacket back to the server with this player's supporter hat state
        Services.SUPPORTER_HELPER.notifyServerOfSupporterHatState(RibbitOptionsJSON.get().isSupporterHatEnabled());
    }
}
