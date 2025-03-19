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
import net.minecraftforge.network.NetworkEvent;

import java.util.List;
import java.util.UUID;
import java.util.function.Supplier;

public class ClientPacketHandlerForge {
    public static void handleStartSingleRibbitInstrument(RibbitMusicStartSingleS2CPacket packet, Supplier<NetworkEvent.Context> ctx) {
        ClientLevel clientLevel = Minecraft.getInstance().level;
        UUID entityId = packet.getRibbitId();
        RibbitInstrument instrument = RibbitInstrumentModule.getInstrument(packet.getInstrumentId());
        int tickOffset = packet.getTickOffset();

        if (clientLevel != null) {
            RibbitEntity ribbit = (RibbitEntity) ((ClientLevelAccessor) clientLevel).callGetEntities().get(entityId);

            if (ribbit == null) {
                RibbitsCommon.LOGGER.error("Received Start Music packet for a ribbit with UUID {} that doesn't exist!", entityId);
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
            SoundEvent instrumentSoundEvent = instrument.getSoundEvent();

            Minecraft.getInstance().getSoundManager().play(new RibbitInstrumentSoundInstance(ribbit, tickOffset, instrumentSoundEvent));
        }
    }

    public static void handleStartAllRibbitInstruments(RibbitMusicStartAllS2CPacket packet, Supplier<NetworkEvent.Context> ctx) {
        ClientLevel clientLevel = Minecraft.getInstance().level;
        List<UUID> entityIds = packet.getRibbitIds();
        List<ResourceLocation> instrumentIds = packet.getInstrumentIds();
        int tickOffset = packet.getTickOffset();

        if (entityIds.size() != instrumentIds.size()) {
            RibbitsCommon.LOGGER.error("Received handleStartAllPacket with {} ribbits and {} instruments!", entityIds.size(), instrumentIds.size());
            return;
        }

        if (clientLevel != null) {
            for (int i = 0; i < entityIds.size(); i++) {
                RibbitEntity ribbit = (RibbitEntity) ((ClientLevelAccessor) clientLevel).callGetEntities().get(entityIds.get(i));
                if (ribbit == null) {
                    RibbitsCommon.LOGGER.error("Received handleStartAllPacket for a ribbit with UUID {} that doesn't exist!", entityIds.get(i));
                    return;
                }

                RibbitInstrument instrument = RibbitInstrumentModule.getInstrument(instrumentIds.get(i));
                if (instrument == null) {
                    RibbitsCommon.LOGGER.error("Tried to play music in handleStartAllPacket for a ribbit with null instrument!");
                    return;
                }

                if (instrument == RibbitInstrumentModule.NONE) {
                    RibbitsCommon.LOGGER.error("Tried to play music in handleStartAllPacket for a ribbit with NONE instrument!");
                    return;
                }
                SoundEvent instrumentSoundEvent = instrument.getSoundEvent();

                Minecraft.getInstance().getSoundManager().play(new RibbitInstrumentSoundInstance(ribbit, tickOffset, instrumentSoundEvent));
            }
        }
    }

    public static void handleStopSingleRibbitInstrument(RibbitMusicStopSingleS2CPacket packet, Supplier<NetworkEvent.Context> ctx) {
        ClientLevel clientLevel = Minecraft.getInstance().level;
        UUID entityId = packet.getRibbitId();

        if (clientLevel != null) {
            RibbitEntity ribbit = (RibbitEntity) ((ClientLevelAccessor) clientLevel).callGetEntities().get(entityId);

            if (ribbit == null) {
                RibbitsCommon.LOGGER.error("Received Stop Music packet for a ribbit with UUID {} that doesn't exist!", entityId);
                return;
            }

            ((ISoundManagerDuck) Minecraft.getInstance().getSoundManager()).ribbits$stopRibbitsMusic(entityId);
        }
    }

    public static void handleStartPlayerInstrument(PlayerMusicStartS2CPacket packet, Supplier<NetworkEvent.Context> ctx) {
        ClientLevel clientLevel = Minecraft.getInstance().level;
        UUID performerId = packet.getPerformerId();

        if (clientLevel != null) {
            Entity performer = ((ClientLevelAccessor) clientLevel).callGetEntities().get(performerId);

            if (performer == null) {
                RibbitsCommon.LOGGER.error("Received Start Maraca packet for Player performer with UUID {} that doesn't exist!", performerId);
                return;
            } else if (!(performer instanceof Player)) {
                RibbitsCommon.LOGGER.error("Received Start Maraca packet for non-Player performer with UUID {}!", performerId);
                return;
            }

            Minecraft.getInstance().getSoundManager().play(new PlayerInstrumentSoundInstance((Player) performer, -1, SoundModule.MUSIC_MARACA.get()));
        }
    }

    public static void handleStopPlayerInstrument(PlayerMusicStopS2CPacket packet, Supplier<NetworkEvent.Context> ctx) {
        ClientLevel clientLevel = Minecraft.getInstance().level;
        UUID performerId = packet.getPerformerId();

        if (clientLevel != null) {
            Entity performer = ((ClientLevelAccessor) clientLevel).callGetEntities().get(performerId);

            if (performer == null) {
                RibbitsCommon.LOGGER.error("Received Stop Maraca packet for Player performer with UUID {} that doesn't exist!", performerId);
                return;
            } else if (!(performer instanceof Player)) {
                RibbitsCommon.LOGGER.error("Received Stop Maraca packet for non-Player performer with UUID {}!", performerId);
                return;
            }

            ((ISoundManagerDuck) Minecraft.getInstance().getSoundManager()).ribbits$stopMaraca(performerId);
        }
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
