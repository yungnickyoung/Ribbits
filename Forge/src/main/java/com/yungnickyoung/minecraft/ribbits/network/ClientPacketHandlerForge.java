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
import com.yungnickyoung.minecraft.ribbits.services.Services;
import net.minecraft.client.Minecraft;
import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.player.Player;
import net.minecraftforge.event.entity.EntityJoinLevelEvent;
import net.minecraftforge.event.level.LevelEvent;
import net.minecraftforge.network.NetworkEvent;

import java.util.*;
import java.util.function.Consumer;
import java.util.function.Supplier;

public class ClientPacketHandlerForge {
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

    public static void handleStartSingleRibbitInstrument(RibbitMusicStartSingleS2CPacket packet, Supplier<NetworkEvent.Context> ctx) {
        UUID entityId = packet.getRibbitId();
        RibbitInstrument instrument = RibbitInstrumentModule.getInstrument(packet.getInstrumentId());
        int tickOffset = packet.getTickOffset();

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

            SoundEvent instrumentSoundEvent = instrument.getSoundEvent();
            Minecraft.getInstance().getSoundManager().play(new RibbitInstrumentSoundInstance(ribbit, tickOffset, instrumentSoundEvent));
        });
    }

    public static void handleStartAllRibbitInstruments(RibbitMusicStartAllS2CPacket packet, Supplier<NetworkEvent.Context> ctx) {
        List<UUID> entityIds = packet.getRibbitIds();
        List<ResourceLocation> instrumentIds = packet.getInstrumentIds();
        int tickOffset = packet.getTickOffset();

        if (entityIds.size() != instrumentIds.size()) {
            RibbitsCommon.LOGGER.error("Received handleStartAllPacket with {} ribbits and {} instruments!", entityIds.size(), instrumentIds.size());
            return;
        }

        for (int i = 0; i < entityIds.size(); i++) {
            RibbitInstrument instrument = RibbitInstrumentModule.getInstrument(instrumentIds.get(i));
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

                SoundEvent instrumentSoundEvent = instrument.getSoundEvent();
                Minecraft.getInstance().getSoundManager().play(new RibbitInstrumentSoundInstance(ribbit, tickOffset, instrumentSoundEvent));
            });
        }
    }

    public static void handleStopSingleRibbitInstrument(RibbitMusicStopSingleS2CPacket packet, Supplier<NetworkEvent.Context> ctx) {
        UUID entityId = packet.getRibbitId();
        queueOrExecute(entityId, entity -> ((ISoundManagerDuck) Minecraft.getInstance().getSoundManager()).ribbits$stopRibbitsMusic(entityId));
    }

    public static void handleStartPlayerInstrument(PlayerMusicStartS2CPacket packet, Supplier<NetworkEvent.Context> ctx) {
        UUID performerId = packet.getPerformerId();

        queueOrExecute(performerId, performer -> {
            if (!(performer instanceof Player playerPerformer)) {
                RibbitsCommon.LOGGER.error("Received Start Maraca packet for non-Player performer with UUID {}!", performerId);
                return;
            }

            Minecraft.getInstance().getSoundManager().play(new PlayerInstrumentSoundInstance(playerPerformer, -1, SoundModule.MUSIC_MARACA.get()));
        });
    }

    public static void handleStopPlayerInstrument(PlayerMusicStopS2CPacket packet, Supplier<NetworkEvent.Context> ctx) {
        UUID performerId = packet.getPerformerId();

        queueOrExecute(performerId, performer -> {
            ((ISoundManagerDuck) Minecraft.getInstance().getSoundManager()).ribbits$stopMaraca(performerId);
        });
    }

    public static void handleToggleSupporterHat(ToggleSupporterHatS2CPacket packet, Supplier<NetworkEvent.Context> ctx) {
        // Update the player's supporter hat status on the client
        SupportersListClient.toggleSupporterHat(packet.getPlayerUuid(), packet.getEnabled());
    }

    public static void handleSupporterHatStateRequest(RequestSupporterHatStateS2CPacket packet, Supplier<NetworkEvent.Context> ctx) {
        // Populate local supporter hat list with the list from the server
        SupportersListClient.clear();
        packet.getEnabledSupporterHatPlayers().forEach(playerUUID -> SupportersListClient.toggleSupporterHat(playerUUID, true));

        // Send a ToggleSupporterPacket back to the server with this player's supporter hat state
        Services.SUPPORTER_HELPER.notifyServerOfSupporterHatState(RibbitOptionsJSON.get().isSupporterHatEnabled());
    }
}
