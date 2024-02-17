package com.yungnickyoung.minecraft.ribbits.mixin.interfaces.client;

import com.yungnickyoung.minecraft.ribbits.mixin.mixins.client.ChannelMixin;

/**
 * Duck interface for attaching data to Minecraft's {@link net.minecraft.client.sounds.SoundEngine} class.
 * @see com.yungnickyoung.minecraft.ribbits.mixin.mixins.client.SoundEngineMixin
 */
public interface ISoundEngineDuck {
    void stopRibbitsMusic(int ribbitEntityId);
}
