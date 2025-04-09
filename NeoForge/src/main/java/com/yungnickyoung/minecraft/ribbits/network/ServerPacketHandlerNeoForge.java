package com.yungnickyoung.minecraft.ribbits.network;

import com.yungnickyoung.minecraft.ribbits.network.payload.ToggleSupporterHatPayload;
import com.yungnickyoung.minecraft.ribbits.supporters.SupportersListServer;
import net.neoforged.neoforge.network.PacketDistributor;
import net.neoforged.neoforge.network.handling.IPayloadContext;

public class ServerPacketHandlerNeoForge {
    public static void handleToggleSupporterHatPayload(ToggleSupporterHatPayload payload, IPayloadContext context) {
        // Update the player's supporter hat status on the server
        SupportersListServer.toggleSupporterHat(payload.playerUUID(), payload.enabled());

        // Forward the payload to all clients
        PacketDistributor.sendToAllPlayers(new ToggleSupporterHatPayload(payload.playerUUID(), payload.enabled()));
    }
}
