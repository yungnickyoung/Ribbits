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
import com.yungnickyoung.minecraft.ribbits.network.packet.RequestSupporterHatStatePacket;
import com.yungnickyoung.minecraft.ribbits.network.packet.ToggleSupporterPacket;
import com.yungnickyoung.minecraft.ribbits.services.Services;
import com.yungnickyoung.minecraft.ribbits.util.BufferUtils;
import net.fabricmc.fabric.api.networking.v1.PacketSender;
import net.minecraft.client.Minecraft;
import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.client.multiplayer.ClientPacketListener;
import net.minecraft.client.player.LocalPlayer;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.player.Player;

import java.util.*;
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

    public static void receiveStartSingle(Minecraft client,
                                          ClientPacketListener clientPacketListener,
                                          FriendlyByteBuf buf,
                                          PacketSender responseSender) {
        UUID entityId = buf.readUUID();

        RibbitInstrument instrument = RibbitInstrumentModule.getInstrument(buf.readResourceLocation());
        int tickOffset = buf.readInt();

        if (instrument == null) {
            RibbitsCommon.LOGGER.error("Tried to play music for a ribbit with null instrument!");
            return;
        }

        if (instrument == RibbitInstrumentModule.NONE) {
            RibbitsCommon.LOGGER.error("Tried to play music for a ribbit with NONE instrument!");
            return;
        }

        queueOrExecute(client, entityId, entity -> {
            if (!(entity instanceof RibbitEntity ribbit)) {
                RibbitsCommon.LOGGER.error("Tried to play music for a non-ribbit entity!");
                return;
            }

            SoundEvent instrumentSoundEvent = instrument.getSoundEvent();

            client.execute(() -> {
                Minecraft.getInstance().getSoundManager().play(new RibbitInstrumentSoundInstance(ribbit, tickOffset, instrumentSoundEvent));
            });
        });
    }

    public static void receiveStartAll(Minecraft client,
                                       ClientPacketListener clientPacketListener,
                                       FriendlyByteBuf buf,
                                       PacketSender responseSender) {
        List<UUID> entityIds = BufferUtils.readUUIDList(buf);
        List<ResourceLocation> instrumentIds = BufferUtils.readResourceLocationList(buf);
        int tickOffset = buf.readInt();

        if (entityIds.size() != instrumentIds.size()) {
            RibbitsCommon.LOGGER.error("Received Start Music All packet with {} ribbits and {} instruments!", entityIds.size(), instrumentIds.size());
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

            queueOrExecute(client, entityIds.get(i), entity -> {
                if (!(entity instanceof RibbitEntity ribbit)) {
                    RibbitsCommon.LOGGER.error("Tried to play music in receiveStartAll for a non-ribbit entity!");
                    return;
                }

                SoundEvent instrumentSoundEvent = instrument.getSoundEvent();

                client.execute(() -> {
                    Minecraft.getInstance().getSoundManager().play(new RibbitInstrumentSoundInstance(ribbit, tickOffset, instrumentSoundEvent));
                });
            });
        }
    }

    public static void receiveStop(Minecraft client,
                                   ClientPacketListener clientPacketListener,
                                   FriendlyByteBuf buf,
                                   PacketSender responseSender) {
        UUID entityId = buf.readUUID();

        queueOrExecute(client, entityId, entity -> {
            client.execute(() -> {
                ((ISoundManagerDuck) Minecraft.getInstance().getSoundManager()).ribbits$stopRibbitsMusic(entityId);
            });
        });
    }

    public static void receiveStartMaraca(Minecraft client,
                                          ClientPacketListener clientPacketListener,
                                          FriendlyByteBuf buf,
                                          PacketSender responseSender) {
        UUID performerId = buf.readUUID();

        queueOrExecute(client, performerId, performer -> {
            if (!(performer instanceof Player playerPerformer)) {
                RibbitsCommon.LOGGER.error("Received Start Maraca packet for non-Player performer with UUID {}!", performerId);
                return;
            }

            client.execute(() -> {
                Minecraft.getInstance().getSoundManager().play(new PlayerInstrumentSoundInstance(playerPerformer, -1, SoundModule.MUSIC_MARACA.get()));
            });
        });
    }

    public static void receiveStopMaraca(Minecraft client,
                                         ClientPacketListener clientPacketListener,
                                         FriendlyByteBuf buf,
                                         PacketSender responseSender) {
        UUID performerId = buf.readUUID();

        queueOrExecute(client, performerId, performer -> {
            client.execute(() -> {
                ((ISoundManagerDuck) Minecraft.getInstance().getSoundManager()).ribbits$stopMaraca(performerId);
            });
        });
    }

    public static void receiveToggleSupporterHat(ToggleSupporterPacket packet, LocalPlayer player, PacketSender responseSender) {
        // Update the player's supporter hat status on the client
        SupportersListClient.toggleSupporterHat(packet.playerUUID(), packet.enabled());
    }

    public static void receiveSupporterHatStateRequest(RequestSupporterHatStatePacket packet, LocalPlayer player, PacketSender responseSender) {
        // Populate local supporter hat list with the list from the server
        SupportersListClient.clear();
        packet.enabledSupporterHatPlayers().forEach(playerUUID -> SupportersListClient.toggleSupporterHat(playerUUID, true));

        // Send a ToggleSupporterPacket back to the server with this player's supporter hat state
        Services.SUPPORTER_HELPER.notifyServerOfSupporterHatState(RibbitOptionsJSON.get().isSupporterHatEnabled());
    }
}
