package com.yungnickyoung.minecraft.ribbits.network;

import com.yungnickyoung.minecraft.ribbits.network.payload.ToggleSupporterHatPayloadC2S;
import com.yungnickyoung.minecraft.ribbits.network.payload.ToggleSupporterHatPayloadS2C;
import com.yungnickyoung.minecraft.ribbits.supporters.SupportersListServer;
import net.neoforged.neoforge.network.PacketDistributor;
import net.neoforged.neoforge.network.handling.IPayloadContext;

public class ServerPacketHandlerNeoForge {
    public static void handleToggleSupporterHatPayload(ToggleSupporterHatPayloadC2S payload, IPayloadContext context) {
        // Update the player's supporter hat status on the server
        SupportersListServer.toggleSupporterHat(payload.playerUUID(), payload.enabled());

        // Forward the payload to all clients
        PacketDistributor.sendToAllPlayers(new ToggleSupporterHatPayloadS2C(payload.playerUUID(), payload.enabled()));
    }
}
