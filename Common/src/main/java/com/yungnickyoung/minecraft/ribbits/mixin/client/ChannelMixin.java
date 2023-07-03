package com.yungnickyoung.minecraft.ribbits.mixin.client;

import com.mojang.blaze3d.audio.Channel;
import com.mojang.blaze3d.audio.SoundBuffer;
import com.yungnickyoung.minecraft.ribbits.access.client.ChannelAccessor;
import com.yungnickyoung.minecraft.ribbits.client.sound.RibbitInstrumentSoundInstance;
import net.minecraft.client.resources.sounds.SoundInstance;
import org.lwjgl.BufferUtils;
import org.lwjgl.openal.AL10;
import org.lwjgl.openal.AL11;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;

import java.nio.IntBuffer;
import java.util.OptionalInt;


@Mixin(Channel.class)
public class ChannelMixin implements ChannelAccessor {

    @Shadow @Final private int source;

    @Override
    public void attachStaticBufferWithByteOffset(SoundInstance instance, SoundBuffer soundBuffer, int sourceId) {
        OptionalInt bufferId = ((SoundBufferAccessor) soundBuffer).callGetAlBuffer();

        if (bufferId.isEmpty()) return;

        AL10.alSourcei(this.source, 4105, bufferId.getAsInt());

        IntBuffer bytesToOffset = BufferUtils.createIntBuffer(1);

        AL11.alGetBufferiv(sourceId, AL11.AL_BYTE_OFFSET, bytesToOffset);
        bytesToOffset.rewind();

        IntBuffer byteOffset = BufferUtils.createIntBuffer(1).put(bytesToOffset.get());
        byteOffset.rewind();
        AL11.alSourceiv(this.source, AL11.AL_BYTE_OFFSET, byteOffset);

        if (instance instanceof RibbitInstrumentSoundInstance ribbitInstrumentSoundInstance) {
            ribbitInstrumentSoundInstance.setSourceId(this.source);
        }
    }

    @Override
    public void attachStaticBufferWithTickOffset(SoundInstance instance, SoundBuffer soundBuffer, int ticksToOffset) {
        IntBuffer frequency = BufferUtils.createIntBuffer(1);

        OptionalInt bufferId = ((SoundBufferAccessor) soundBuffer).callGetAlBuffer();

        if (bufferId.isEmpty()) return;

        AL10.alSourcei(this.source, 4105, bufferId.getAsInt());

        AL10.alGetBufferi(bufferId.getAsInt(), AL10.AL_FREQUENCY, frequency);

        frequency.rewind();

        int samplesToOffset = (int) ((ticksToOffset / 20.0f) * frequency.get());

        IntBuffer sampleOffset = BufferUtils.createIntBuffer(1).put(samplesToOffset);
        sampleOffset.rewind();
        AL11.alSourceiv(this.source, AL11.AL_SAMPLE_OFFSET, sampleOffset);

        if (instance instanceof RibbitInstrumentSoundInstance ribbitInstrumentSoundInstance) {
            ribbitInstrumentSoundInstance.setSourceId(this.source);
        }
    }
}
