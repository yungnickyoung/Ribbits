package com.yungnickyoung.minecraft.ribbits.mixin.interfaces.client;

import com.yungnickyoung.minecraft.ribbits.mixin.mixins.client.music.SoundManagerMixin;

import java.util.UUID;

/**
 * Duck interface for attaching data to Minecraft's {@link net.minecraft.client.sounds.SoundManager} class.
 *
 * @see SoundManagerMixin
 */
public interface ISoundManagerDuck {
    void ribbits$stopRibbitsMusic(UUID ribbitEntityId);

    void ribbits$stopMaraca(UUID playerId);
}
