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
import net.minecraft.client.Minecraft;
import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.player.Player;
import net.neoforged.neoforge.event.entity.EntityJoinLevelEvent;
import net.neoforged.neoforge.event.level.LevelEvent;
import net.neoforged.neoforge.network.handling.IPayloadContext;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import java.util.function.Consumer;

public class ClientPacketHandlerNeoForge {
    /**
     * Maps entity UUIDs to a list of actions to perform once the entity is loaded on the client.
     */
    private static final Map<UUID, List<Consumer<Entity>>> pendingEntityActions = new HashMap<>();

    /**
     * Executes any pending actions for the given entity now that it has been loaded.
     */
    public static void onEntityJoinLevel(EntityJoinLevelEvent event) {
        if (!event.getLevel().isClientSide()) {
            return;
        }

        Entity entity = event.getEntity();
        UUID entityId = entity.getUUID();
        if (pendingEntityActions.containsKey(entityId)) {
            List<Consumer<Entity>> actions = pendingEntityActions.remove(entityId);
            for (Consumer<Entity> action : actions) {
                action.accept(entity);
            }
        }
    }

    public static void onLevelUnload(LevelEvent.Unload event) {
        if (!event.getLevel().isClientSide()) {
            return;
        }

        pendingEntityActions.clear();
    }

    /**
     * Queues the action to be performed on the entity with the given UUID once it is loaded,
     * or executes it immediately if the entity is already loaded.
     */
    private static void queueOrExecute(UUID entityId, Consumer<Entity> action) {
        ClientLevel clientLevel = Minecraft.getInstance().level;
        if (clientLevel == null) {
            return;
        }

        Entity entity = ((ClientLevelAccessor) clientLevel).callGetEntities().get(entityId);
        if (entity == null) {
            pendingEntityActions.computeIfAbsent(entityId, k -> new ArrayList<>()).add(action);
        } else {
            action.accept(entity);
        }
    }

    public static void handleStartMusicSinglePayload(RibbitStartMusicSinglePayload payload, IPayloadContext context) {
        UUID entityId = payload.ribbitUUID();
        RibbitInstrument instrument = RibbitInstrumentModule.getInstrument(payload.instrumentId());
        int tickOffset = payload.tickOffset();

        if (instrument == null) {
            RibbitsCommon.LOGGER.error("Tried to play music for a ribbit with null instrument!");
            return;
        }

        if (instrument == RibbitInstrumentModule.NONE) {
            RibbitsCommon.LOGGER.error("Tried to play music for a ribbit with NONE instrument!");
            return;
        }

        queueOrExecute(entityId, entity -> {
            if (!(entity instanceof RibbitEntity ribbit)) {
                RibbitsCommon.LOGGER.error("Tried to play music for a non-ribbit entity!");
                return;
            }

            context.enqueueWork(() -> ClientUtils.playSound(ribbit, tickOffset, instrument));
        });
    }

    public static void handleStartMusicAllPayload(RibbitStartMusicAllPayload payload, IPayloadContext context) {
        List<UUID> entityIds = payload.ribbitUUIDs();
        List<ResourceLocation> instrumentIds = payload.instrumentIds();
        int tickOffset = payload.tickOffset();

        if (entityIds.size() != instrumentIds.size()) {
            RibbitsCommon.LOGGER.error("Received Start Music All payload with {} ribbits and {} instruments!", entityIds.size(), instrumentIds.size());
            return;
        }

        for (int i = 0; i < entityIds.size(); i++) {
            RibbitInstrument instrument = RibbitInstrumentModule.getInstrument(payload.instrumentIds().get(i));

            if (instrument == null) {
                RibbitsCommon.LOGGER.error("Tried to play music in handleStartAllPacket for a ribbit with null instrument!");
                return;
            }

            if (instrument == RibbitInstrumentModule.NONE) {
                RibbitsCommon.LOGGER.error("Tried to play music in handleStartAllPacket for a ribbit with NONE instrument!");
                return;
            }

            queueOrExecute(entityIds.get(i), entity -> {
                if (!(entity instanceof RibbitEntity ribbit)) {
                    RibbitsCommon.LOGGER.error("Tried to play music in handleStartAllPacket for a non-ribbit entity!");
                    return;
                }

                context.enqueueWork(() -> ClientUtils.playSound(ribbit, tickOffset, instrument));
            });
        }
    }

    public static void handleStopMusicSinglePayload(RibbitStopMusicSinglePayload payload, IPayloadContext context) {
        UUID entityId = payload.ribbitUUID();
        queueOrExecute(entityId, entity -> {
            if (!(entity instanceof RibbitEntity)) {
                RibbitsCommon.LOGGER.error("Received Stop Music payload for a non-ribbit entity with UUID {}!", entityId);
                return;
            }

            context.enqueueWork(() -> ClientUtils.stopRibbitSound(entityId));
        });
    }

    public static void handleStartHearingMaracaPayload(StartHearingMaracaPayload payload, IPayloadContext context) {
        UUID performerId = payload.performerUUID();

        queueOrExecute(performerId, performer -> {
            if (!(performer instanceof Player playerPerformer)) {
                RibbitsCommon.LOGGER.error("Received Start Maraca packet for non-Player performer with UUID {}!", performerId);
                return;
            }

            context.enqueueWork(() -> ClientUtils.startHearingMaraca(playerPerformer));
        });
    }

    public static void handleStopHearingMaracaPayload(StopHearingMaracaPayload payload, IPayloadContext context) {
        UUID performerId = payload.performerUUID();

        queueOrExecute(performerId, performer -> {
            if (!(performer instanceof Player)) {
                RibbitsCommon.LOGGER.error("Received Stop Maraca payload for non-Player performer with UUID {}!", performerId);
                return;
            }

            context.enqueueWork(() -> ClientUtils.stopHearingMaraca(performerId));
        });
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
