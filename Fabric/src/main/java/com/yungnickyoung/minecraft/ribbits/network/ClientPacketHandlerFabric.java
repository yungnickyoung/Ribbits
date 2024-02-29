package com.yungnickyoung.minecraft.ribbits.network;

import com.yungnickyoung.minecraft.ribbits.RibbitsCommon;
import com.yungnickyoung.minecraft.ribbits.client.sound.RibbitInstrumentSoundInstance;
import com.yungnickyoung.minecraft.ribbits.data.RibbitInstrument;
import com.yungnickyoung.minecraft.ribbits.entity.RibbitEntity;
import com.yungnickyoung.minecraft.ribbits.mixin.interfaces.client.ISoundManagerDuck;
import com.yungnickyoung.minecraft.ribbits.module.RibbitInstrumentModule;
import com.yungnickyoung.minecraft.ribbits.util.BufferUtils;
import net.fabricmc.fabric.api.networking.v1.PacketSender;
import net.minecraft.client.Minecraft;
import net.minecraft.client.multiplayer.ClientPacketListener;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.sounds.SoundEvent;

import java.util.List;

public class ClientPacketHandlerFabric {
    public static void receiveStartSingle(Minecraft client,
                                          ClientPacketListener clientPacketListener,
                                          FriendlyByteBuf buf,
                                          PacketSender responseSender) {
        int entityId = buf.readInt();
        RibbitEntity ribbit = (RibbitEntity) client.level.getEntity(entityId);
        int tickOffset = buf.readInt();

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

        client.execute(() -> {
            Minecraft.getInstance().getSoundManager().play(new RibbitInstrumentSoundInstance(ribbit, tickOffset, instrumentSoundEvent));
        });
    }

    public static void receiveStartAll(Minecraft client,
                                       ClientPacketListener clientPacketListener,
                                       FriendlyByteBuf buf,
                                       PacketSender responseSender) {
        List<Integer> entityIds = BufferUtils.readIntList(buf);
        int tickOffset = buf.readInt();

        for (int id : entityIds) {
            RibbitEntity ribbit = (RibbitEntity) client.level.getEntity(id);
            if (ribbit == null) {
                RibbitsCommon.LOGGER.error("Received Start Music All packet for a ribbit with ID {} that doesn't exist!", id);
                return;
            }

            RibbitInstrument instrument = ribbit.getRibbitData().getInstrument();
            if (instrument == RibbitInstrumentModule.NONE) {
                RibbitsCommon.LOGGER.error("Tried to play music in receiveStartAll for a ribbit with NONE instrument!");
                return;
            }
            SoundEvent instrumentSoundEvent = instrument.getSoundEvent();

            client.execute(() -> {
                Minecraft.getInstance().getSoundManager().play(new RibbitInstrumentSoundInstance(ribbit, tickOffset, instrumentSoundEvent));
            });
        }
    }

    public static void receiveStop(Minecraft client,
                                   ClientPacketListener clientPacketListener,
                                   FriendlyByteBuf buf,
                                   PacketSender responseSender) {
        int entityId = buf.readInt();
        RibbitEntity ribbit = (RibbitEntity) client.level.getEntity(entityId);

        if (ribbit == null) {
            RibbitsCommon.LOGGER.error("Received Stop Music packet for a ribbit with ID {} that doesn't exist!", entityId);
            return;
        }

        client.execute(() -> {
            ((ISoundManagerDuck) Minecraft.getInstance().getSoundManager()).ribbits$stopRibbitsMusic(entityId);
        });
    }
}
