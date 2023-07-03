package com.yungnickyoung.minecraft.ribbits.access.client;

import com.mojang.blaze3d.audio.SoundBuffer;
import net.minecraft.client.resources.sounds.SoundInstance;

public interface ChannelAccessor {
    void attachStaticBufferWithByteOffset(SoundInstance instance, SoundBuffer soundBuffer, int sourceId);

    void attachStaticBufferWithTickOffset(SoundInstance instance, SoundBuffer soundBuffer, int ticksToOffset);
}
