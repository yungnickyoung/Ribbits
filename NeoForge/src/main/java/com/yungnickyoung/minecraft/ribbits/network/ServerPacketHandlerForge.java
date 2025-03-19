package com.yungnickyoung.minecraft.ribbits.network;

import com.yungnickyoung.minecraft.ribbits.module.NetworkModuleNeoForge;
import com.yungnickyoung.minecraft.ribbits.supporters.SupportersListServer;
import net.minecraftforge.network.NetworkEvent;

import java.util.function.Supplier;

public class ServerPacketHandlerForge {
    public static void handleToggleSupporterHat(ToggleSupporterHatC2SPacket packet, Supplier<NetworkEvent.Context> ctx) {
        // Update the player's supporter hat status on the server
        SupportersListServer.toggleSupporterHat(packet.getPlayerUuid(), packet.getEnabled());

        // Forward the packet to all clients
        if (ctx.get().getSender() != null && ctx.get().getSender().getServer() != null) {
            NetworkModuleNeoForge.sendToAllClients(new ToggleSupporterHatS2CPacket(packet.getPlayerUuid(), packet.getEnabled()));
        }
    }
}
