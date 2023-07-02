package com.yungnickyoung.minecraft.ribbits.network;

import com.yungnickyoung.minecraft.ribbits.client.sound.RibbitInstrumentSoundInstance;
import com.yungnickyoung.minecraft.ribbits.entity.RibbitEntity;
import com.yungnickyoung.minecraft.ribbits.module.SoundModule;
import com.yungnickyoung.minecraft.yungsapi.module.SoundEventModuleFabric;
import net.fabricmc.fabric.api.networking.v1.PacketSender;
import net.minecraft.client.Minecraft;
import net.minecraft.client.multiplayer.ClientPacketListener;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.world.entity.Entity;

public class RibbitMusicS2CPacket {
    public static void receive(Minecraft client,
                               ClientPacketListener clientPacketListener,
                               FriendlyByteBuf buf,
                               PacketSender responseSender) {
        RibbitEntity ribbit = (RibbitEntity) client.level.getEntity(buf.readInt());

        client.execute(() -> {
            if (!ribbit.getPlayingMusic()) {
                client.getSoundManager().play(new RibbitInstrumentSoundInstance(ribbit, ribbit.getRibbitData().getProfession().getInstrumentTrack()));
                ribbit.setPlayingMusic(true);
            }
        });
    }
}
