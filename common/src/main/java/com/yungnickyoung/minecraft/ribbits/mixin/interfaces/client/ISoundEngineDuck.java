package com.yungnickyoung.minecraft.ribbits.mixin.interfaces.client;

import com.yungnickyoung.minecraft.ribbits.mixin.mixins.client.music.SoundEngineMixin;

import java.util.UUID;

/**
 * Duck interface for attaching data to Minecraft's {@link net.minecraft.client.sounds.SoundEngine} class.
 *
 * @see SoundEngineMixin
 */
public interface ISoundEngineDuck {
    void ribbits$stopRibbitsMusic(UUID ribbitEntityId);

    void ribbits$stopMaraca(UUID playerId);
}
