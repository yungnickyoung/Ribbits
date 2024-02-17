package com.yungnickyoung.minecraft.ribbits.mixin.interfaces.client;

import com.mojang.blaze3d.audio.Channel;
import com.mojang.blaze3d.audio.SoundBuffer;
import com.yungnickyoung.minecraft.ribbits.mixin.mixins.client.ChannelMixin;
import net.minecraft.client.resources.sounds.SoundInstance;

/**
 * Duck interface for attaching data to Minecraft's {@link net.minecraft.client.sounds.SoundManager} class.
 * @see com.yungnickyoung.minecraft.ribbits.mixin.mixins.client.SoundManagerMixin
 */
public interface ISoundManagerDuck {
    void stopRibbitsMusic(int ribbitEntityId);
}
