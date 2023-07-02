package com.yungnickyoung.minecraft.ribbits.module;

import com.yungnickyoung.minecraft.ribbits.RibbitsCommon;
import com.yungnickyoung.minecraft.ribbits.network.RibbitMusicS2CPacket;
import net.fabricmc.fabric.api.client.networking.v1.ClientPlayNetworking;
import net.minecraft.resources.ResourceLocation;

public class NetworkModuleFabric {
    public static final ResourceLocation RIBBIT_MUSIC_ID = new ResourceLocation(RibbitsCommon.MOD_ID, "ribbit_music");

    public static void registerC2SPackets() {
    }

    public static void registerS2CPackets() {
        ClientPlayNetworking.registerGlobalReceiver(RIBBIT_MUSIC_ID, RibbitMusicS2CPacket::receive);
    }
}
