package com.yungnickyoung.minecraft.ribbits.network;

import com.yungnickyoung.minecraft.ribbits.network.payload.ToggleSupporterHatPayload;
import com.yungnickyoung.minecraft.ribbits.supporters.SupportersListServer;
import net.fabricmc.fabric.api.networking.v1.PlayerLookup;
import net.fabricmc.fabric.api.networking.v1.ServerPlayNetworking;

public class ServerPacketHandlerFabric {
    public static void receiveToggleSupporterHat(ToggleSupporterHatPayload packet, ServerPlayNetworking.Context context) {
        // Update the player's supporter hat status on the server
        SupportersListServer.toggleSupporterHat(packet.playerUUID(), packet.enabled());

        // Forward the payload to all clients
        if (context.player().getServer() != null) {
            PlayerLookup.all(context.server()).forEach(p -> ServerPlayNetworking.send(p, packet));
        }
    }
}
