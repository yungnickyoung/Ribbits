package com.yungnickyoung.minecraft.ribbits.network;

import com.yungnickyoung.minecraft.ribbits.RibbitsCommon;
import com.yungnickyoung.minecraft.ribbits.client.sound.PlayerInstrumentSoundInstance;
import com.yungnickyoung.minecraft.ribbits.client.sound.RibbitInstrumentSoundInstance;
import com.yungnickyoung.minecraft.ribbits.client.supporters.RibbitOptionsJSON;
import com.yungnickyoung.minecraft.ribbits.client.supporters.SupportersListClient;
import com.yungnickyoung.minecraft.ribbits.data.RibbitInstrument;
import com.yungnickyoung.minecraft.ribbits.entity.RibbitEntity;
import com.yungnickyoung.minecraft.ribbits.mixin.interfaces.client.ISoundManagerDuck;
import com.yungnickyoung.minecraft.ribbits.mixin.mixins.client.accessor.ClientLevelAccessor;
import com.yungnickyoung.minecraft.ribbits.module.RibbitInstrumentModule;
import com.yungnickyoung.minecraft.ribbits.module.SoundModule;
import com.yungnickyoung.minecraft.ribbits.network.payload.RequestSupporterHatStatePayload;
import com.yungnickyoung.minecraft.ribbits.network.payload.RibbitStartMusicAllPayload;
import com.yungnickyoung.minecraft.ribbits.network.payload.RibbitStartMusicSinglePayload;
import com.yungnickyoung.minecraft.ribbits.network.payload.RibbitStopMusicSinglePayload;
import com.yungnickyoung.minecraft.ribbits.network.payload.StartHearingMaracaPayload;
import com.yungnickyoung.minecraft.ribbits.network.payload.StopHearingMaracaPayload;
import com.yungnickyoung.minecraft.ribbits.network.payload.ToggleSupporterHatPayloadS2C;
import com.yungnickyoung.minecraft.ribbits.services.Services;
import net.fabricmc.fabric.api.client.networking.v1.ClientPlayNetworking;
import net.minecraft.client.Minecraft;
import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.player.Player;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import java.util.function.Consumer;

public class ClientPacketHandlerFabric {
    /**
     * Maps entity UUIDs to a list of actions to perform once the entity is loaded on the client.
     */
    private static final Map<UUID, List<Consumer<Entity>>> pendingEntityActions = new HashMap<>();

    /**
     * Executes any pending actions for the given entity now that it has been loaded.
     */
    public static void onEntityLoad(Entity entity) {
        UUID entityId = entity.getUUID();
        if (pendingEntityActions.containsKey(entityId)) {
            List<Consumer<Entity>> actions = pendingEntityActions.remove(entityId);
            for (Consumer<Entity> action : actions) {
                action.accept(entity);
            }
        }
    }

    public static void clearPendingActions() {
        pendingEntityActions.clear();
    }

    /**
     * Queues the action to be performed on the entity with the given UUID once it is loaded,
     * or executes it immediately if the entity is already loaded.
     */
    private static void queueOrExecute(Minecraft client, UUID entityId, Consumer<Entity> action) {
        ClientLevel clientLevel = client.level;
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

    public static void handleStartMusicSinglePayload(RibbitStartMusicSinglePayload payload, ClientPlayNetworking.Context context) {
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

        queueOrExecute(context.client(), entityId, entity -> {
            if (!(entity instanceof RibbitEntity ribbit)) {
                RibbitsCommon.LOGGER.error("Received Start Music payload for a non-ribbit entity with UUID {}!", entityId);
                return;
            }

            SoundEvent instrumentSoundEvent = instrument.getSoundEvent();

            context.client().execute(() -> {
                Minecraft.getInstance().getSoundManager().play(new RibbitInstrumentSoundInstance(ribbit, tickOffset, instrumentSoundEvent));
            });
        });
    }

    public static void handleStartMusicAllPayload(RibbitStartMusicAllPayload payload, ClientPlayNetworking.Context context) {
        List<UUID> entityIds = payload.ribbitUUIDs();
        List<ResourceLocation> instrumentIds = payload.instrumentIds();
        int tickOffset = payload.tickOffset();

        if (entityIds.size() != instrumentIds.size()) {
            RibbitsCommon.LOGGER.error("Received Start Music All payload with {} ribbits and {} instruments!", entityIds.size(), instrumentIds.size());
            return;
        }

        for (int i = 0; i < entityIds.size(); i++) {
            RibbitInstrument instrument = RibbitInstrumentModule.getInstrument(instrumentIds.get(i));

            if (instrument == null) {
                RibbitsCommon.LOGGER.error("Tried to play music in receiveStartAll for a ribbit with null instrument!");
                return;
            }

            if (instrument == RibbitInstrumentModule.NONE) {
                RibbitsCommon.LOGGER.error("Tried to play music in receiveStartAll for a ribbit with NONE instrument!");
                return;
            }

            queueOrExecute(context.client(), entityIds.get(i), entity -> {
                if (!(entity instanceof RibbitEntity ribbit)) {
                    RibbitsCommon.LOGGER.error("Tried to play music in receiveStartAll for a non-ribbit entity!");
                    return;
                }

                SoundEvent instrumentSoundEvent = instrument.getSoundEvent();

                context.client().execute(() -> {
                    Minecraft.getInstance().getSoundManager().play(new RibbitInstrumentSoundInstance(ribbit, tickOffset, instrumentSoundEvent));
                });
            });
        }
    }

    public static void handleStopMusicSinglePayload(RibbitStopMusicSinglePayload payload, ClientPlayNetworking.Context context) {
        UUID entityId = payload.ribbitUUID();

        queueOrExecute(context.client(), entityId, entity -> {
            context.client().execute(() -> {
                ((ISoundManagerDuck) Minecraft.getInstance().getSoundManager()).ribbits$stopRibbitsMusic(entityId);
            });
        });
    }

    public static void handleStartHearingMaracaPayload(StartHearingMaracaPayload payload, ClientPlayNetworking.Context context) {
        UUID performerId = payload.performerUUID();

        queueOrExecute(context.client(), performerId, performer -> {
            if (!(performer instanceof Player playerPerformer)) {
                RibbitsCommon.LOGGER.error("Received Start Maraca packet for non-Player performer with UUID {}!", performerId);
                return;
            }

            context.client().execute(() -> {
                Minecraft.getInstance().getSoundManager().play(new PlayerInstrumentSoundInstance(playerPerformer, -1, SoundModule.MUSIC_MARACA.get()));
            });
        });
    }

    public static void handleStopHearingMaracaPayload(StopHearingMaracaPayload payload, ClientPlayNetworking.Context context) {
        UUID performerId = payload.performerUUID();

        queueOrExecute(context.client(), performerId, performer -> {
            if (!(performer instanceof Player)) {
                RibbitsCommon.LOGGER.error("Received Stop Maraca payload for non-Player performer with UUID {}!", performerId);
                return;
            }

            context.client().execute(() -> {
                ((ISoundManagerDuck) Minecraft.getInstance().getSoundManager()).ribbits$stopMaraca(performerId);
            });
        });
    }

    public static void handleToggleSupporterHatPayload(ToggleSupporterHatPayloadS2C packet, ClientPlayNetworking.Context context) {
        // Update the player's supporter hat status on the client
        SupportersListClient.toggleSupporterHat(packet.playerUUID(), packet.enabled());
    }

    public static void handleRequestSupporterHatStatePayload(RequestSupporterHatStatePayload payload, ClientPlayNetworking.Context context) {
        // Populate local supporter hat list with the list from the server
        SupportersListClient.clear();
        payload.enabledSupporterHatPlayers().forEach(playerUUID ->
                SupportersListClient.toggleSupporterHat(playerUUID, true));

        // Send a ToggleSupporterPacket back to the server with this player's supporter hat state
        Services.SUPPORTER_HELPER.notifyServerOfSupporterHatState(RibbitOptionsJSON.get().isSupporterHatEnabled());
    }
}
