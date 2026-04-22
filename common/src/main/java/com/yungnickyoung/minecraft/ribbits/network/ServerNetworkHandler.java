package com.yungnickyoung.minecraft.ribbits.network;

import com.yungnickyoung.minecraft.ribbits.RibbitsCommon;
import com.yungnickyoung.minecraft.ribbits.client.supporters.SupportersJSON;
import com.yungnickyoung.minecraft.ribbits.entity.RibbitEntity;
import com.yungnickyoung.minecraft.ribbits.network.payload.*;
import com.yungnickyoung.minecraft.ribbits.platform.PlatformHelper;
import com.yungnickyoung.minecraft.ribbits.supporters.SupportersListServer;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;
import net.minecraft.resources.Identifier;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public class ServerNetworkHandler {

    public static void handleToggleSupporterHatC2S(ToggleSupporterHatPayloadC2S payload) {

        UUID senderUUID = payload.playerUUID();

        if (!SupportersJSON.get().isSupporter(senderUUID)) {
            RibbitsCommon.LOGGER.warn("Non-supporter {} attempted to toggle supporter hat.", senderUUID);
            sendToAllPlayers(new ToggleSupporterHatPayloadS2C(senderUUID, false));
            return;
        }

        SupportersListServer.toggleSupporterHat(senderUUID, payload.enabled());
        sendToAllPlayers(new ToggleSupporterHatPayloadS2C(senderUUID, payload.enabled()));
    }

    public static void onRibbitStartMusicGoal(ServerLevel serverLevel, RibbitEntity newRibbit, RibbitEntity masterRibbit) {
        int tickOffset = newRibbit.equals(masterRibbit) ? masterRibbit.getTicksPlayingMusic() : -1;
        Identifier instrumentId = newRibbit.getRibbitData().getInstrument().id();
        sendToAllPlayers(new RibbitStartMusicSinglePayload(newRibbit.getUUID(), instrumentId, tickOffset));
    }

    public static void onPlayerEnterBandRange(ServerPlayer player, ServerLevel serverLevel, RibbitEntity masterRibbit) {
        List<RibbitEntity> members = masterRibbit.getRibbitsPlayingMusic().stream().toList();
        List<UUID> ribbitIds = new ArrayList<>();
        List<Identifier> instrumentIds = new ArrayList<>();
        ribbitIds.add(masterRibbit.getUUID());
        instrumentIds.add(masterRibbit.getRibbitData().getInstrument().id());
        members.forEach(r -> {
            ribbitIds.add(r.getUUID());
            instrumentIds.add(r.getRibbitData().getInstrument().id());
        });
        PlatformHelper.sendToPlayer(player, new RibbitStartMusicAllPayload(ribbitIds, instrumentIds, masterRibbit.getTicksPlayingMusic()));
    }

    public static void onPlayerExitBandRange(ServerPlayer player, ServerLevel serverLevel, RibbitEntity masterRibbit) {
        PlatformHelper.sendToPlayer(player, new RibbitStopMusicSinglePayload(masterRibbit.getUUID()));
        masterRibbit.getRibbitsPlayingMusic().forEach(r -> PlatformHelper.sendToPlayer(player, new RibbitStopMusicSinglePayload(r.getUUID())));
    }

    public static void startHearingMaraca(ServerPlayer performer, ServerPlayer audienceMember) {
        PlatformHelper.sendToPlayer(audienceMember, new StartHearingMaracaPayload(performer.getUUID()));
    }

    public static void stopHearingMaraca(ServerPlayer performer, ServerPlayer audienceMember) {
        PlatformHelper.sendToPlayer(audienceMember, new StopHearingMaracaPayload(performer.getUUID()));
    }

    public static <T extends CustomPacketPayload> void sendToAllPlayers(T payload) {
        PlatformHelper.sendToPlayers(PlatformHelper.getCurrentServer().getPlayerList().getPlayers(), payload);
    }
}