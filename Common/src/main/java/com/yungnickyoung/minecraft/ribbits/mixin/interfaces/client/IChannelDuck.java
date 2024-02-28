package com.yungnickyoung.minecraft.ribbits.mixin.interfaces.client;

import com.mojang.blaze3d.audio.Channel;
import com.mojang.blaze3d.audio.SoundBuffer;
import com.yungnickyoung.minecraft.ribbits.mixin.mixins.client.ChannelMixin;
import net.minecraft.client.resources.sounds.SoundInstance;

/**
 * Duck interface for attaching data to Minecraft's {@link Channel} class.
 * @see ChannelMixin
 */
public interface IChannelDuck {
    void ribbits$attachStaticBufferWithByteOffset(SoundInstance instance, SoundBuffer soundBuffer, int sourceId);

    void ribbits$attachStaticBufferWithTickOffset(SoundInstance instance, SoundBuffer soundBuffer, int ticksToOffset);
}
