package com.yungnickyoung.minecraft.ribbits.client.sound;

import net.minecraft.sounds.SoundEvent;
import net.minecraft.world.entity.player.Player;

public class PlayerInstrumentSoundInstance extends InstrumentSoundInstance<Player> {
    public PlayerInstrumentSoundInstance(Player player, int ticksOffset, SoundEvent soundEvent) {
        super(player, ticksOffset, soundEvent);
    }

    @Override
    public void tick() {
        super.tick();
        if (this.entity.isRemoved()) {
            this.stop();
        }
    }

    public Player getPlayer() {
        return this.entity;
    }
}
