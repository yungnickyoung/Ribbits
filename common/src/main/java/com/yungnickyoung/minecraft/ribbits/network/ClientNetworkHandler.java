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
import com.yungnickyoung.minecraft.ribbits.network.payload.*;
import dev.architectury.networking.NetworkManager;
import net.minecraft.client.Minecraft;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.player.Player;

import java.util.UUID;

public class ClientNetworkHandler {

    public static void handleStartMusicSingleS2C(RibbitStartMusicSinglePayload payload) {
        Entity e = ((ClientLevelAccessor) Minecraft.getInstance().level).callGetEntities().get(payload.ribbitUUID());
        if (!(e instanceof RibbitEntity ribbit)) {
            RibbitsCommon.LOGGER.error("StartMusicSingle: ribbit {} not found", payload.ribbitUUID());
            return;
        }
        RibbitInstrument instrument = RibbitInstrumentModule.getInstrument(payload.instrumentId());
        if (instrument == null || instrument == RibbitInstrumentModule.NONE) {
            RibbitsCommon.LOGGER.error("StartMusicSingle: invalid instrument {}", payload.instrumentId());
            return;
        }
        SoundEvent evt = instrument.soundEvent();
        Minecraft.getInstance().getSoundManager().play(
                new RibbitInstrumentSoundInstance(ribbit, payload.tickOffset(), evt));
    }

    public static void handleStopMusicSingleS2C(RibbitStopMusicSinglePayload payload) {
        ((ISoundManagerDuck)
                Minecraft.getInstance().getSoundManager()).ribbits$stopRibbitsMusic(payload.ribbitUUID());
    }

    public static void handleStartMusicAllS2C(RibbitStartMusicAllPayload payload) {
        if (payload.ribbitUUIDs().size() != payload.instrumentIds().size()) {
            RibbitsCommon.LOGGER.error("StartMusicAll: {} ribbits != {} instruments",
                    payload.ribbitUUIDs().size(), payload.instrumentIds().size());
            return;
        }
        for (int i = 0; i < payload.ribbitUUIDs().size(); i++) {
            Entity e = ((ClientLevelAccessor) Minecraft.getInstance().level).callGetEntities().get(payload.ribbitUUIDs().get(i));
            if (!(e instanceof RibbitEntity ribbit)) {
                RibbitsCommon.LOGGER.error("StartMusicAll: ribbit {} not found", payload.ribbitUUIDs().get(i));
                return;
            }
            RibbitInstrument instrument = RibbitInstrumentModule.getInstrument(payload.instrumentIds().get(i));
            if (instrument == null || instrument == RibbitInstrumentModule.NONE) {
                RibbitsCommon.LOGGER.error("StartMusicAll: invalid instrument {}", payload.instrumentIds().get(i));
                return;
            }
            SoundEvent evt = instrument.soundEvent();
            Minecraft.getInstance().getSoundManager().play(
                    new RibbitInstrumentSoundInstance(ribbit, payload.tickOffset(), evt));
        }
    }

    public static void handleStartHearingMaracaS2C(StartHearingMaracaPayload payload) {
        Entity performer = ((ClientLevelAccessor) Minecraft.getInstance().level).callGetEntities().get(payload.performerUUID());
        if (!(performer instanceof Player)) {
            RibbitsCommon.LOGGER.error("StartMaraca: performer {} invalid", payload.performerUUID());
            return;
        }
        Minecraft.getInstance().getSoundManager().play(
                new PlayerInstrumentSoundInstance(
                        (Player) performer, -1, SoundModule.MUSIC_MARACA.get()));
    }

    public static void handleStopHearingMaracaS2C(StopHearingMaracaPayload payload) {
        ((ISoundManagerDuck)
                Minecraft.getInstance().getSoundManager()).ribbits$stopMaraca(payload.performerUUID());
    }

    public static void handleRequestSupporterHatStateS2C(RequestSupporterHatStatePayload payload) {
        SupportersListClient.clear();
        payload.enabledSupporterHatPlayers().forEach(uuid -> SupportersListClient.toggleSupporterHat(uuid, true));
        notifyServerOfSupporterHatState(RibbitOptionsJSON.get().isSupporterHatEnabled());
    }

    public static void handleToggleSupporterHatS2C(ToggleSupporterHatPayloadS2C payload) {
        SupportersListClient.toggleSupporterHat(payload.playerUUID(), payload.enabled());
    }

    public static void notifyServerOfSupporterHatState(boolean enabled) {
        UUID playerUUID = Minecraft.getInstance().getUser().getProfileId();
        if (Minecraft.getInstance().getConnection() == null) return;
        NetworkManager.sendToServer(new ToggleSupporterHatPayloadC2S(playerUUID, enabled));
    }
}