package com.yungnickyoung.minecraft.ribbits.network;

import com.yungnickyoung.minecraft.ribbits.RibbitsCommon;
import com.yungnickyoung.minecraft.ribbits.client.sound.RibbitInstrumentSoundInstance;
import com.yungnickyoung.minecraft.ribbits.data.RibbitInstrument;
import com.yungnickyoung.minecraft.ribbits.entity.RibbitEntity;
import com.yungnickyoung.minecraft.ribbits.mixin.interfaces.client.ISoundManagerDuck;
import com.yungnickyoung.minecraft.ribbits.module.RibbitInstrumentModule;
import net.minecraft.client.Minecraft;
import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.sounds.SoundEvent;
import net.minecraftforge.network.NetworkEvent;

import java.util.List;
import java.util.function.Supplier;

public class ClientPacketHandlerForge {
    public static void handleStartSinglePacket(RibbitMusicStartSingleS2CPacket packet, Supplier<NetworkEvent.Context> ctx) {
        ClientLevel clientLevel = Minecraft.getInstance().level;
        int entityId = packet.getRibbitId();
        int tickOffset = packet.getTickOffset();

        if (clientLevel != null) {
            RibbitEntity ribbit = (RibbitEntity) clientLevel.getEntity(entityId);

            if (ribbit == null) {
                RibbitsCommon.LOGGER.error("Received Start Music packet for a ribbit with ID {} that doesn't exist!", entityId);
                return;
            }

            RibbitInstrument instrument = ribbit.getRibbitData().getInstrument();
            if (instrument == RibbitInstrumentModule.NONE) {
                RibbitsCommon.LOGGER.error("Tried to play music for a ribbit with NONE instrument!");
                return;
            }
            SoundEvent instrumentSoundEvent = instrument.getSoundEvent();

            Minecraft.getInstance().getSoundManager().play(new RibbitInstrumentSoundInstance(ribbit, tickOffset, instrumentSoundEvent));
        }
    }

    public static void handleStartAllPacket(RibbitMusicStartAllS2CPacket packet, Supplier<NetworkEvent.Context> ctx) {
        ClientLevel clientLevel = Minecraft.getInstance().level;
        List<Integer> entityIds = packet.getRibbitIds();
        int tickOffset = packet.getTickOffset();

        if (clientLevel != null) {
            for (int id : entityIds) {
                RibbitEntity ribbit = (RibbitEntity) clientLevel.getEntity(id);
                if (ribbit == null) {
                    RibbitsCommon.LOGGER.error("Received Start Music All packet for a ribbit with ID " + id + " that doesn't exist!");
                    return;
                }

                RibbitInstrument instrument = ribbit.getRibbitData().getInstrument();
                if (instrument == RibbitInstrumentModule.NONE) {
                    RibbitsCommon.LOGGER.error("Tried to play music in handleStartAllPacket for a ribbit with NONE instrument!");
                    return;
                }
                SoundEvent instrumentSoundEvent = instrument.getSoundEvent();

                Minecraft.getInstance().getSoundManager().play(new RibbitInstrumentSoundInstance(ribbit, tickOffset, instrumentSoundEvent));
            }
        }
    }

    public static void handleStopSinglePacket(RibbitMusicStopSingleS2CPacket packet, Supplier<NetworkEvent.Context> ctx) {
        ClientLevel clientLevel = Minecraft.getInstance().level;
        int entityId = packet.getRibbitId();

        if (clientLevel != null) {
            RibbitEntity ribbit = (RibbitEntity) clientLevel.getEntity(entityId);

            if (ribbit == null) {
                RibbitsCommon.LOGGER.error("Received Stop Music packet for a ribbit with ID {} that doesn't exist!", entityId);
                return;
            }

            ((ISoundManagerDuck) Minecraft.getInstance().getSoundManager()).stopRibbitsMusic(entityId);
        }
    }
}
