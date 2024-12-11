package com.yungnickyoung.minecraft.ribbits.network;

import com.yungnickyoung.minecraft.ribbits.module.NetworkModuleForge;
import com.yungnickyoung.minecraft.ribbits.supporters.SupportersListServer;
import net.minecraftforge.event.network.CustomPayloadEvent;

public class ServerPacketHandlerForge {
    public static void handleToggleSupporterHat(ToggleSupporterHatC2SPacket packet, CustomPayloadEvent.Context ctx) {
        // Update the player's supporter hat status on the server
        SupportersListServer.toggleSupporterHat(packet.getPlayerUuid(), packet.getEnabled());

        // Forward the packet to all clients
        if (ctx.getSender() != null && ctx.getSender().getServer() != null) {
            NetworkModuleForge.sendToAllClients(new ToggleSupporterHatS2CPacket(packet.getPlayerUuid(), packet.getEnabled()));
        }
    }
}
