package com.yungnickyoung.minecraft.ribbits.entity.goal;

import com.yungnickyoung.minecraft.ribbits.entity.RibbitEntity;
import com.yungnickyoung.minecraft.ribbits.module.SoundModule;
import net.minecraft.core.Holder;
import net.minecraft.world.effect.MobEffect;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.entity.ai.goal.Goal;
import net.minecraft.world.entity.ai.targeting.TargetingConditions;
import net.minecraft.world.entity.player.Player;

import java.util.EnumSet;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class RibbitApplyBuffGoal extends Goal {
    private static final int TICKS_UNTIL_BUFF_APPLIED = 22;

    private final RibbitEntity ribbit;
    private final double range;
    private final int cooldownTicks;

    /**
     * Map of effects to duration, in ticks.
     */
    private final Map<Holder<MobEffect>, Integer> effects;

    private int ticksSinceStart;

    public RibbitApplyBuffGoal(RibbitEntity ribbit, double range, int cooldownTicks) {
        this.ribbit = ribbit;
        this.range = range;
        this.cooldownTicks = cooldownTicks;
        this.effects = new HashMap<>();
        this.effects.put(MobEffects.REGENERATION, 1200);
        this.effects.put(MobEffects.DAMAGE_RESISTANCE, 2400);
        this.effects.put(MobEffects.DAMAGE_BOOST, 2400);
        this.effects.put(MobEffects.JUMP, 2400);
        this.effects.put(MobEffects.DIG_SPEED, 2400);
        this.effects.put(MobEffects.HEALTH_BOOST, 2400);

        this.setFlags(EnumSet.of(Flag.MOVE));
    }

    @Override
    public boolean canUse() {
        return this.ribbit.getBuffCooldown() == 0 && !this.ribbit.level().getNearbyPlayers(TargetingConditions.forCombat().range(this.range), this.ribbit, this.ribbit.getBoundingBox().inflate(this.range, 5.0d, this.range)).isEmpty();
    }

    @Override
    public boolean canContinueToUse() {
        return this.ticksSinceStart >= 0;
    }

    @Override
    public boolean requiresUpdateEveryTick() {
        return true;
    }

    @Override
    public void tick() {
        if (this.ticksSinceStart >= TICKS_UNTIL_BUFF_APPLIED) {
            this.applyBuffs();
        } else if (this.ticksSinceStart >= 0) {
            this.ticksSinceStart++;
        }
    }

    @Override
    public void start() {
        this.ticksSinceStart = 0;
        this.ribbit.setBuffing(true);
        this.ribbit.playSound(SoundModule.ENTITY_RIBBIT_MAGIC.get());
    }

    @Override
    public void stop() {
        this.ribbit.setBuffCooldown(this.cooldownTicks);
        this.ticksSinceStart = 0;
    }

    @Override
    public boolean isInterruptable() {
        return false;
    }

    private void applyBuffs() {
        this.ticksSinceStart = -1;

        List<Player> nearbyPlayers = this.ribbit.level().getNearbyPlayers(TargetingConditions.forCombat().range(this.range), this.ribbit, this.ribbit.getBoundingBox().inflate(this.range, 5.0d, this.range));

        Holder<MobEffect> randomEffect = this.effects.keySet().stream().toList().get(this.ribbit.getRandom().nextInt(this.effects.size()));
        int effectDuration = this.effects.get(randomEffect);
        for (Player player : nearbyPlayers) {
            player.addEffect(new MobEffectInstance(randomEffect, effectDuration, 0));
        }

        this.ribbit.setBuffing(false);
    }
}
