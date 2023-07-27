package com.yungnickyoung.minecraft.ribbits.mixin.mixins.client;

import com.mojang.blaze3d.audio.Channel;
import com.mojang.blaze3d.audio.SoundBuffer;
import com.yungnickyoung.minecraft.ribbits.mixin.interfaces.client.IChannelDuck;
import com.yungnickyoung.minecraft.ribbits.client.sound.RibbitInstrumentSoundInstance;
import com.yungnickyoung.minecraft.ribbits.mixin.mixins.client.accessor.SoundBufferAccessor;
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
public class ChannelMixin implements IChannelDuck {

    @Shadow @Final private int source;

    /**
     * Attaches a buffer to the current source using a byte offset grabbed from another existing source.
     * @param instance the sound instance about to be started
     * @param soundBuffer the buffer that the sound should be played on
     * @param sourceId the source id of the source that the offset should be fetched from
     */
    @Override
    public void attachStaticBufferWithByteOffset(SoundInstance instance, SoundBuffer soundBuffer, int sourceId) {
        OptionalInt bufferId = ((SoundBufferAccessor) soundBuffer).callGetAlBuffer();

        if (bufferId.isEmpty()) return;

        AL10.alSourcei(this.source, 4105, bufferId.getAsInt());
        IntBuffer bytesToOffset = BufferUtils.createIntBuffer(1);

        if (sourceId != 0) {
            AL10.alGetSourcei(sourceId, AL11.AL_BYTE_OFFSET, bytesToOffset);
        }

        bytesToOffset.rewind();

        IntBuffer byteOffset = BufferUtils.createIntBuffer(1).put(sourceId != 0 ? bytesToOffset.get() : 0);
        byteOffset.rewind();
        AL11.alSourceiv(this.source, AL11.AL_BYTE_OFFSET, byteOffset);

        if (instance instanceof RibbitInstrumentSoundInstance ribbitInstrumentSoundInstance) {
            ribbitInstrumentSoundInstance.setSourceId(this.source);
        }
    }

    /**
     * Attaches a buffer to the current source using a sample offset that is matched as closely as possible to an
     * existing source by the number of ticks the other sound has been playing.
     * @param instance the sound instance about to be started
     * @param soundBuffer the buffer that the sound should be played on
     * @param ticksToOffset the number of ticks the already playing sound has been active
     */
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
