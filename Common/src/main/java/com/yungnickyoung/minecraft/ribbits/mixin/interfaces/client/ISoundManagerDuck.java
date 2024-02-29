package com.yungnickyoung.minecraft.ribbits.mixin.interfaces.client;

/**
 * Duck interface for attaching data to Minecraft's {@link net.minecraft.client.sounds.SoundManager} class.
 * @see com.yungnickyoung.minecraft.ribbits.mixin.mixins.client.SoundManagerMixin
 */
public interface ISoundManagerDuck {
    void ribbits$stopRibbitsMusic(int ribbitEntityId);
}
