package com.yungnickyoung.minecraft.ribbits.neoforge.supporters;

import com.yungnickyoung.minecraft.ribbits.network.payload.RequestSupporterHatStatePayload;
import com.yungnickyoung.minecraft.ribbits.supporters.SupportersListServer;
import net.minecraft.server.level.ServerPlayer;
import net.neoforged.neoforge.event.entity.player.PlayerEvent;
import net.neoforged.neoforge.network.PacketDistributor;

import java.util.List;
import java.util.UUID;

public class SupporterEventsNeoForge {
    public static void onPlayerJoin(PlayerEvent.PlayerLoggedInEvent event) {
        if (!(event.getEntity() instanceof ServerPlayer serverPlayer)) return;

        // Get the server's current list of players with the supporter hat enabled
        List<UUID> playersWithSupporterHat = SupportersListServer.getPlayersWithSupporterHat().stream().toList();

        // Send the list of players with the supporter hat enabled to the new player, and request their own supporter hat state
        PacketDistributor.sendToPlayer(serverPlayer, new RequestSupporterHatStatePayload(playersWithSupporterHat));
    }

}
