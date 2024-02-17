package com.yungnickyoung.minecraft.ribbits.module;

import com.yungnickyoung.minecraft.ribbits.RibbitsCommon;
import com.yungnickyoung.minecraft.ribbits.network.ClientPacketHandlerFabric;
import net.fabricmc.fabric.api.client.networking.v1.ClientPlayNetworking;
import net.minecraft.resources.ResourceLocation;

public class NetworkModuleFabric {
    public static final ResourceLocation RIBBIT_START_MUSIC_SINGLE = new ResourceLocation(RibbitsCommon.MOD_ID, "ribbit_start_music_single");
    public static final ResourceLocation RIBBIT_START_MUSIC_ALL = new ResourceLocation(RibbitsCommon.MOD_ID, "ribbit_start_music_all");
    public static final ResourceLocation RIBBIT_STOP_MUSIC = new ResourceLocation(RibbitsCommon.MOD_ID, "ribbit_stop_music");

    public static void registerC2SPackets() {
    }

    public static void registerS2CPackets() {
        ClientPlayNetworking.registerGlobalReceiver(RIBBIT_START_MUSIC_SINGLE, ClientPacketHandlerFabric::receiveStartSingle);
        ClientPlayNetworking.registerGlobalReceiver(RIBBIT_START_MUSIC_ALL, ClientPacketHandlerFabric::receiveStartAll);
        ClientPlayNetworking.registerGlobalReceiver(RIBBIT_STOP_MUSIC, ClientPacketHandlerFabric::receiveStop);
    }
}
