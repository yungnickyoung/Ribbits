package com.yungnickyoung.minecraft.ribbits.network;

import com.yungnickyoung.minecraft.ribbits.RibbitsCommon;
import com.yungnickyoung.minecraft.ribbits.client.sound.RibbitInstrumentSoundInstance;
import com.yungnickyoung.minecraft.ribbits.entity.RibbitEntity;
import net.fabricmc.fabric.api.networking.v1.PacketSender;
import net.minecraft.client.Minecraft;
import net.minecraft.client.multiplayer.ClientPacketListener;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.sounds.SoundEvent;

import java.util.Optional;

public class RibbitMusicS2CPacket {
    public static void receive(Minecraft client,
                               ClientPacketListener clientPacketListener,
                               FriendlyByteBuf buf,
                               PacketSender responseSender) {
        int entityId = buf.readInt();
        RibbitEntity ribbit = (RibbitEntity) client.level.getEntity(entityId);
        int ticksPlaying = buf.readInt();

        Optional<SoundEvent> instrumentTrackOptional = ribbit.getRibbitData().getProfession().getInstrumentSoundEvent();
        if (instrumentTrackOptional.isEmpty()) {
            RibbitsCommon.LOGGER.error("Tried to play music for a ribbit with no instrument track defined!");
            return;
        }

        client.execute(() -> {
            Minecraft.getInstance().getSoundManager().play(new RibbitInstrumentSoundInstance(ribbit, ticksPlaying, instrumentTrackOptional.get()));
        });
    }
}
