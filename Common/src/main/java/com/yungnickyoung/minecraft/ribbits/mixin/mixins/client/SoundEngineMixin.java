package com.yungnickyoung.minecraft.ribbits.mixin.mixins.client;

import com.yungnickyoung.minecraft.ribbits.client.sound.RibbitInstrumentSoundInstance;
import com.yungnickyoung.minecraft.ribbits.mixin.interfaces.client.IChannelDuck;
import com.yungnickyoung.minecraft.ribbits.mixin.interfaces.client.ISoundEngineDuck;
import net.minecraft.client.resources.sounds.Sound;
import net.minecraft.client.resources.sounds.SoundInstance;
import net.minecraft.client.resources.sounds.TickableSoundInstance;
import net.minecraft.client.sounds.ChannelAccess;
import net.minecraft.client.sounds.SoundBufferLibrary;
import net.minecraft.client.sounds.SoundEngine;
import net.minecraft.client.sounds.WeighedSoundEvents;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.phys.Vec3;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.LocalCapture;

import java.util.List;
import java.util.concurrent.CompletableFuture;

@Mixin(SoundEngine.class)
public class SoundEngineMixin implements ISoundEngineDuck {

    @Shadow @Final private SoundBufferLibrary soundBuffers;

    @Shadow @Final private List<TickableSoundInstance> tickingSounds;

    @Inject(method = "play",
            at = @At(value = "INVOKE",
                    target = "Lnet/minecraft/client/sounds/SoundBufferLibrary;getCompleteBuffer(Lnet/minecraft/resources/ResourceLocation;)Ljava/util/concurrent/CompletableFuture;",
                    shift = At.Shift.BEFORE),
            locals = LocalCapture.CAPTURE_FAILHARD,
            cancellable = true)
    private void ribbits$handleRibbitsOffsetSounds(SoundInstance soundInstance, CallbackInfo ci, WeighedSoundEvents $$1x,
                                                   ResourceLocation $$2x, Sound sound, float $$4x, float $$5x, SoundSource soundSource,
                                                   float $$7x, float $$8x, SoundInstance.Attenuation attenuation, boolean $$10x,
                                                   Vec3 $$11x, boolean $$14, boolean $$15, CompletableFuture $$16, ChannelAccess.ChannelHandle channelAccess) {
        if (soundInstance instanceof RibbitInstrumentSoundInstance ribbitInstrumentSoundInstance) {
            // Stop any existing sound with the same ribbit ID
            List<RibbitInstrumentSoundInstance> soundsToStop = this.tickingSounds.stream()
                    .filter(instance -> instance instanceof RibbitInstrumentSoundInstance)
                    .map(instance -> (RibbitInstrumentSoundInstance) instance)
                    .filter(instance -> instance.getRibbit().getId() == ribbitInstrumentSoundInstance.getRibbit().getId())
                    .toList();
            soundsToStop.forEach(RibbitInstrumentSoundInstance::stopSound);

            // Play the sound with the offset
            this.soundBuffers.getCompleteBuffer(sound.getPath()).thenAccept((soundBuffer) -> {
                channelAccess.execute((channel) -> {
                    if (ribbitInstrumentSoundInstance.getTicksOffset() == -1) { // -1 tick offset means we should use a byte offset instead
                        // Play new sound with byte offset from existing sound
                        RibbitInstrumentSoundInstance instrumentSoundInstance = (RibbitInstrumentSoundInstance) this.tickingSounds.stream().filter((instance) -> instance instanceof RibbitInstrumentSoundInstance).findAny().orElse(null);
                        if (instrumentSoundInstance != null) {
                            ((IChannelDuck) channel).ribbits$attachStaticBufferWithByteOffset(ribbitInstrumentSoundInstance, soundBuffer, instrumentSoundInstance.getSourceId());
                        }
                    } else {
                        ((IChannelDuck) channel).ribbits$attachStaticBufferWithTickOffset(ribbitInstrumentSoundInstance, soundBuffer, ribbitInstrumentSoundInstance.getTicksOffset());
                    }

                    channel.play();
                });
            });

            this.tickingSounds.add(ribbitInstrumentSoundInstance);
            ci.cancel();
        }
    }

    @Override
    public void ribbits$stopRibbitsMusic(int ribbitEntityId) {
        List<RibbitInstrumentSoundInstance> soundsToStop = this.tickingSounds.stream()
            .filter(instance -> instance instanceof RibbitInstrumentSoundInstance)
            .map(instance -> (RibbitInstrumentSoundInstance) instance)
            .filter(instance -> instance.getRibbit().getId() == ribbitEntityId)
            .toList();
        soundsToStop.forEach(RibbitInstrumentSoundInstance::stopSound);
    }
}
