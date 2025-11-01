package com.yungnickyoung.minecraft.ribbits.mixin.mixins.client.music;

import com.mojang.blaze3d.audio.Channel;
import com.mojang.blaze3d.audio.SoundBuffer;
import com.yungnickyoung.minecraft.ribbits.client.sound.InstrumentSoundInstance;
import com.yungnickyoung.minecraft.ribbits.mixin.interfaces.client.IChannelDuck;
import com.yungnickyoung.minecraft.ribbits.mixin.mixins.client.accessor.SoundBufferAccessor;
import net.minecraft.client.resources.sounds.SoundInstance;
import org.lwjgl.BufferUtils;
import org.lwjgl.openal.AL10;
import org.lwjgl.openal.AL11;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;

import java.nio.IntBuffer;
import java.util.OptionalInt;

@Mixin(Channel.class)
public class ChannelMixin implements IChannelDuck {

    @Shadow
    @Final
    private int source;

    /**
     * Attaches a buffer to the current source using a byte offset grabbed from another existing source.
     *
     * @param instance            the sound instance about to be started
     * @param soundBuffer         the buffer that the sound should be played on
     * @param existingSoundSource the source id of the source that the offset should be fetched from
     */
    @Override
    public void ribbits$attachStaticBufferWithByteOffset(SoundInstance instance, SoundBuffer soundBuffer, int existingSoundSource) {
        OptionalInt bufferId = ((SoundBufferAccessor) soundBuffer).callGetAlBuffer();
        if (bufferId.isEmpty()) return;

        // Set the buffer on the source
        AL10.alSourcei(this.source, AL10.AL_BUFFER, bufferId.getAsInt());

        // Get the byte offset from the existing sound's source. Note that AL_BYTE_OFFSET requires AL11
        IntBuffer bytesToOffset = BufferUtils.createIntBuffer(1);
        if (existingSoundSource != 0) {
            AL10.alGetSourcei(existingSoundSource, AL11.AL_BYTE_OFFSET, bytesToOffset);
        }
        bytesToOffset.rewind();

        // Copy the byte offset to a new buffer and set it on the source
        IntBuffer byteOffset = BufferUtils.createIntBuffer(1).put(existingSoundSource != 0 ? bytesToOffset.get() : 0);
        byteOffset.rewind();
        AL11.alSourceiv(this.source, AL11.AL_BYTE_OFFSET, byteOffset);

        // Attach the source id to the sound instance.
        // This is so that other sounds can grab the byte offset from this sound when they start.
        if (instance instanceof InstrumentSoundInstance<?> ribbitInstrumentSoundInstance) {
            ribbitInstrumentSoundInstance.setSourceId(this.source);
        }
    }

    /**
     * Attaches a buffer to the current source using a sample offset that is matched as closely as possible to an
     * existing source by the number of ticks the other sound has been playing.
     *
     * @param instance      the sound instance about to be started
     * @param soundBuffer   the buffer that the sound should be played on
     * @param ticksToOffset the number of ticks the already playing sound has been active
     */
    @Override
    public void ribbits$attachStaticBufferWithTickOffset(SoundInstance instance, SoundBuffer soundBuffer, int ticksToOffset) {
        OptionalInt bufferId = ((SoundBufferAccessor) soundBuffer).callGetAlBuffer();
        if (bufferId.isEmpty()) return;

        // Set the buffer on the source
        AL10.alSourcei(this.source, AL10.AL_BUFFER, bufferId.getAsInt());

        // Read the sound parameters from the buffer
        int frequency = getBufferParameter(bufferId.getAsInt(), AL10.AL_FREQUENCY); // Samples per second

        // Calculate the number of samples to offset, based on the tick offset and the frequency of the sound
        int samplesToOffset = (int) ((ticksToOffset / 20.0f) * frequency);

        // TODO - test this (modulo'ing the samples by total song length) and see if it's even necessary?
        int sizeInBytes = getBufferParameter(bufferId.getAsInt(), AL10.AL_SIZE);
        int bitsPerSample = getBufferParameter(bufferId.getAsInt(), AL10.AL_BITS);
        int channels = getBufferParameter(bufferId.getAsInt(), AL10.AL_CHANNELS);
        int lengthInSamples = sizeInBytes * 8 / (bitsPerSample * channels);
        samplesToOffset = samplesToOffset % lengthInSamples;

        // Set the sample offset on the source
        IntBuffer sampleOffset = BufferUtils.createIntBuffer(1).put(samplesToOffset);
        sampleOffset.rewind();
        AL11.alSourceiv(this.source, AL11.AL_SAMPLE_OFFSET, sampleOffset);

        // Attach the source id to the sound instance.
        // This is so that other sounds can grab the byte offset from this sound when they start.
        if (instance instanceof InstrumentSoundInstance<?> ribbitInstrumentSoundInstance) {
            ribbitInstrumentSoundInstance.setSourceId(this.source);
        }
    }

    @Unique
    private int getBufferParameter(int buffer, int param) {
        IntBuffer bufferParam = BufferUtils.createIntBuffer(1);
        AL10.alGetBufferi(buffer, param, bufferParam);
        bufferParam.rewind();
        return bufferParam.get();
    }
}
