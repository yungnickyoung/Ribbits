package com.yungnickyoung.minecraft.ribbits.client;

import com.yungnickyoung.minecraft.ribbits.client.sound.PlayerInstrumentSoundInstance;
import com.yungnickyoung.minecraft.ribbits.client.sound.RibbitInstrumentSoundInstance;
import com.yungnickyoung.minecraft.ribbits.data.RibbitInstrument;
import com.yungnickyoung.minecraft.ribbits.entity.RibbitEntity;
import com.yungnickyoung.minecraft.ribbits.mixin.interfaces.client.ISoundManagerDuck;
import com.yungnickyoung.minecraft.ribbits.module.SoundModule;
import net.minecraft.client.Minecraft;
import net.minecraft.world.entity.player.Player;

import java.util.UUID;

public class ClientUtils {
    public static void playSound(RibbitEntity ribbit, int tickOffset, RibbitInstrument instrument) {
        Minecraft.getInstance().getSoundManager().play(
                new RibbitInstrumentSoundInstance(ribbit, tickOffset, instrument.getSoundEvent()));
    }

    public static void stopRibbitSound(UUID ribbitUUID) {
        ((ISoundManagerDuck) Minecraft.getInstance().getSoundManager()).ribbits$stopRibbitsMusic(ribbitUUID);
    }

    public static void startHearingMaraca(Player performer) {
        Minecraft.getInstance().getSoundManager().play(
                new PlayerInstrumentSoundInstance(performer, -1, SoundModule.MUSIC_MARACA.get()));
    }

    public static void stopHearingMaraca(UUID performerUUID) {
        ((ISoundManagerDuck) Minecraft.getInstance().getSoundManager()).ribbits$stopMaraca(performerUUID);
    }
}
