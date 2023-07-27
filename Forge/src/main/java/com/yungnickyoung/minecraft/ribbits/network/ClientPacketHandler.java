package com.yungnickyoung.minecraft.ribbits.network;

import com.yungnickyoung.minecraft.ribbits.RibbitsCommon;
import com.yungnickyoung.minecraft.ribbits.client.sound.RibbitInstrumentSoundInstance;
import com.yungnickyoung.minecraft.ribbits.entity.RibbitEntity;
import net.minecraft.client.Minecraft;
import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.sounds.SoundEvent;
import net.minecraftforge.network.NetworkEvent;

import java.util.Optional;
import java.util.function.Supplier;

public class ClientPacketHandler {
    public static void handleRibbitMusic(RibbitMusicS2CPacket packet, Supplier<NetworkEvent.Context> ctx) {
        ClientLevel clientLevel = Minecraft.getInstance().level;
        if (clientLevel != null) {
            RibbitEntity ribbit = (RibbitEntity) clientLevel.getEntity(packet.getRibbitId());
            int ticksPlaying = packet.getTickOffset();

            Optional<SoundEvent> instrumentTrackOptional = ribbit.getRibbitData().getProfession().getInstrumentSoundEvent();
            if (instrumentTrackOptional.isEmpty()) {
                RibbitsCommon.LOGGER.error("Tried to play music for a ribbit with no instrument track defined!");
                return;
            }

            Minecraft.getInstance().getSoundManager().play(new RibbitInstrumentSoundInstance(ribbit, ticksPlaying, instrumentTrackOptional.get()));
        }
    }
}
