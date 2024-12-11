package com.yungnickyoung.minecraft.ribbits.module;

import com.yungnickyoung.minecraft.ribbits.RibbitsCommon;
import com.yungnickyoung.minecraft.ribbits.network.ClientPacketHandlerFabric;
import com.yungnickyoung.minecraft.ribbits.network.ServerPacketHandlerFabric;
import com.yungnickyoung.minecraft.ribbits.network.packet.RequestSupporterHatStatePacket;
import com.yungnickyoung.minecraft.ribbits.network.packet.ToggleSupporterPacket;
import net.fabricmc.fabric.api.client.networking.v1.ClientPlayNetworking;
import net.fabricmc.fabric.api.networking.v1.ServerPlayNetworking;
import net.minecraft.resources.ResourceLocation;

public class NetworkModuleFabric {
    public static final ResourceLocation RIBBIT_START_MUSIC_SINGLE_S2C = new ResourceLocation(RibbitsCommon.MOD_ID, "ribbit_start_music_single");
    public static final ResourceLocation RIBBIT_START_MUSIC_ALL_S2C = new ResourceLocation(RibbitsCommon.MOD_ID, "ribbit_start_music_all");
    public static final ResourceLocation RIBBIT_STOP_MUSIC_S2C = new ResourceLocation(RibbitsCommon.MOD_ID, "ribbit_stop_music");
    public static final ResourceLocation START_HEARING_MARACA_S2C = new ResourceLocation(RibbitsCommon.MOD_ID, "start_hearing_maraca");
    public static final ResourceLocation STOP_HEARING_MARACA_S2C = new ResourceLocation(RibbitsCommon.MOD_ID, "stop_hearing_maraca");

    public static void registerC2SPackets() {
        ServerPlayNetworking.registerGlobalReceiver(ToggleSupporterPacket.TYPE, ServerPacketHandlerFabric::receiveToggleSupporterHat);
    }

    public static void registerS2CPackets() {
        ClientPlayNetworking.registerGlobalReceiver(RIBBIT_START_MUSIC_SINGLE_S2C, ClientPacketHandlerFabric::receiveStartSingle);
        ClientPlayNetworking.registerGlobalReceiver(RIBBIT_START_MUSIC_ALL_S2C, ClientPacketHandlerFabric::receiveStartAll);
        ClientPlayNetworking.registerGlobalReceiver(RIBBIT_STOP_MUSIC_S2C, ClientPacketHandlerFabric::receiveStop);
        ClientPlayNetworking.registerGlobalReceiver(START_HEARING_MARACA_S2C, ClientPacketHandlerFabric::receiveStartMaraca);
        ClientPlayNetworking.registerGlobalReceiver(STOP_HEARING_MARACA_S2C, ClientPacketHandlerFabric::receiveStopMaraca);
        ClientPlayNetworking.registerGlobalReceiver(RequestSupporterHatStatePacket.TYPE, ClientPacketHandlerFabric::receiveSupporterHatStateRequest);

        // ToggleSupporterPacket is registered in both the client and server packet handlers.
        // When received on the server, it will update the server's SupportersListServer and forward the packet to all clients.
        // When received on the client, it will update the player's local SupportersListClient.
        ClientPlayNetworking.registerGlobalReceiver(ToggleSupporterPacket.TYPE, ClientPacketHandlerFabric::receiveToggleSupporterHat);
    }
}
