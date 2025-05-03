package com.yungnickyoung.minecraft.ribbits.network;

import com.yungnickyoung.minecraft.ribbits.network.payload.ToggleSupporterHatPayloadC2S;
import com.yungnickyoung.minecraft.ribbits.network.payload.ToggleSupporterHatPayloadS2C;
import com.yungnickyoung.minecraft.ribbits.supporters.SupportersListServer;
import net.fabricmc.fabric.api.networking.v1.PlayerLookup;
import net.fabricmc.fabric.api.networking.v1.ServerPlayNetworking;

public class ServerPacketHandlerFabric {
    public static void receiveToggleSupporterHat(ToggleSupporterHatPayloadC2S packet, ServerPlayNetworking.Context context) {
        // Update the player's supporter hat status on the server
        SupportersListServer.toggleSupporterHat(packet.playerUUID(), packet.enabled());

        // Forward the payload to all clients
        if (context.player().getServer() != null) {
            ToggleSupporterHatPayloadS2C forwardPacket = new ToggleSupporterHatPayloadS2C(packet.playerUUID(), packet.enabled());
            PlayerLookup.all(context.server()).forEach(p -> ServerPlayNetworking.send(p, forwardPacket));
        }
    }
}
