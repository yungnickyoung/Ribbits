package com.yungnickyoung.minecraft.ribbits.network;

import com.yungnickyoung.minecraft.ribbits.client.sound.RibbitInstrumentSoundInstance;
import com.yungnickyoung.minecraft.ribbits.entity.RibbitEntity;
import net.fabricmc.fabric.api.networking.v1.PacketSender;
import net.minecraft.client.Minecraft;
import net.minecraft.client.multiplayer.ClientPacketListener;
import net.minecraft.network.FriendlyByteBuf;

public class RibbitMusicS2CPacket {
    public static void receive(Minecraft client,
                               ClientPacketListener clientPacketListener,
                               FriendlyByteBuf buf,
                               PacketSender responseSender) {
        RibbitEntity ribbit = (RibbitEntity) client.level.getEntity(buf.readInt());
        int ticksPlaying = buf.readInt();

        client.execute(() -> {
            Minecraft.getInstance().getSoundManager().play(new RibbitInstrumentSoundInstance(ribbit, ticksPlaying, ribbit.getRibbitData().getProfession().getInstrumentTrack()));
        });
    }
}
