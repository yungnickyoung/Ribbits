package com.yungnickyoung.minecraft.ribbits.network;

import com.yungnickyoung.minecraft.ribbits.client.sound.RibbitInstrumentSoundInstance;
import com.yungnickyoung.minecraft.ribbits.entity.RibbitEntity;
import net.minecraft.client.Minecraft;
import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.core.particles.SimpleParticleType;
import net.minecraftforge.network.NetworkEvent;

import java.util.Random;
import java.util.function.Supplier;

public class ClientPacketHandler {
    public static void handleRibbitMusic(RibbitMusicS2CPacket packet, Supplier<NetworkEvent.Context> ctx) {
        ClientLevel clientLevel = Minecraft.getInstance().level;
        if (clientLevel != null) {
            RibbitEntity ribbit = (RibbitEntity) clientLevel.getEntity(packet.getRibbitId());

            if (!ribbit.getPlayingMusic()) {
                Minecraft.getInstance().getSoundManager().play(new RibbitInstrumentSoundInstance(ribbit, ribbit.getRibbitData().getProfession().getInstrumentTrack()));
                ribbit.setPlayingMusic(true);
            }
        }
    }
}
