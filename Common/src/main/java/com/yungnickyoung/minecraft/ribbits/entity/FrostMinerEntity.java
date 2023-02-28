package com.yungnickyoung.minecraft.ribbits.entity;

import net.minecraft.network.syncher.EntityDataAccessor;
import net.minecraft.network.syncher.EntityDataSerializers;
import net.minecraft.network.syncher.SynchedEntityData;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.Mob;
import net.minecraft.world.entity.PathfinderMob;
import net.minecraft.world.entity.ai.attributes.AttributeSupplier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.ai.goal.FloatGoal;
import net.minecraft.world.entity.ai.goal.MeleeAttackGoal;
import net.minecraft.world.entity.ai.goal.RandomLookAroundGoal;
import net.minecraft.world.entity.ai.goal.RandomStrollGoal;
import net.minecraft.world.entity.ai.goal.target.HurtByTargetGoal;
import net.minecraft.world.entity.ai.goal.target.NearestAttackableTargetGoal;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;
import software.bernie.geckolib3.core.IAnimatable;
import software.bernie.geckolib3.core.PlayState;
import software.bernie.geckolib3.core.builder.AnimationBuilder;
import software.bernie.geckolib3.core.builder.ILoopType;
import software.bernie.geckolib3.core.controller.AnimationController;
import software.bernie.geckolib3.core.event.predicate.AnimationEvent;
import software.bernie.geckolib3.core.manager.AnimationData;
import software.bernie.geckolib3.core.manager.AnimationFactory;
import software.bernie.geckolib3.util.GeckoLibUtil;

public class FrostMinerEntity extends PathfinderMob implements IAnimatable {
    private final AnimationFactory factory = GeckoLibUtil.createFactory(this);

    private static final EntityDataAccessor<Boolean> DAMAGED = SynchedEntityData.defineId(FrostMinerEntity.class, EntityDataSerializers.BOOLEAN);
    private static final EntityDataAccessor<Boolean> ATTACK = SynchedEntityData.defineId(FrostMinerEntity.class, EntityDataSerializers.BOOLEAN);

    private int damageTicks = 0;
    private int attackTicks = 0;

    public FrostMinerEntity(EntityType<FrostMinerEntity> entityType, Level world) {
        super(entityType, world);
    }

    @Override
    protected void defineSynchedData() {
        super.defineSynchedData();
        this.getEntityData().define(DAMAGED, false);
        this.getEntityData().define(ATTACK, false);
    }

    @Override
    protected void registerGoals() {
        super.registerGoals();
        this.goalSelector.addGoal(0, new FloatGoal(this));
        this.goalSelector.addGoal(1, new AnimatedMeleeAttackGoal(this, 1.0D, false));
        this.goalSelector.addGoal(2, new RandomLookAroundGoal(this));
        this.goalSelector.addGoal(3, new RandomStrollGoal(this, 1.0D));
        this.targetSelector.addGoal(0, new HurtByTargetGoal(this));
        this.targetSelector.addGoal(1, new NearestAttackableTargetGoal<>(this, Player.class, true));
    }

    public static AttributeSupplier.Builder createAttributes() {
        return Mob.createMobAttributes()
                .add(Attributes.MAX_HEALTH, 20.0)
                .add(Attributes.KNOCKBACK_RESISTANCE)
                .add(Attributes.MOVEMENT_SPEED, 0.3)
                .add(Attributes.ARMOR)
                .add(Attributes.ARMOR_TOUGHNESS)
                .add(Attributes.FOLLOW_RANGE, 16.0)
                .add(Attributes.ATTACK_DAMAGE, 3.0)
                .add(Attributes.ATTACK_KNOCKBACK);
    }

    /**
     * Tracks animation timers and performs attack at the correct animation time.
     */
    @Override
    public void tick() {
        super.tick();

        if (!this.level.isClientSide) {

            if (this.getDamaged()) {
                this.damageTicks--;

                if (this.damageTicks <= 0) {
                    this.setDamaged(false);
                }
            }

            if (this.getAttack()) {
                this.attackTicks--;

                if (this.attackTicks <= 0) {
                    this.setAttack(false);
                } else if (this.attackTicks == 10 && this.getTarget() != null) {
                    LivingEntity target = this.getTarget();
                    double distance = this.distanceToSqr(target.getX(), target.getY(), target.getZ());

                    this.attack(target, distance);
                }
            }
        }
    }

    @Override
    public boolean hurt(DamageSource source, float amount) {
        boolean damaged = super.hurt(source, amount);

        if (damaged) {
            this.setDamaged(true);
        }

        return damaged;
    }

    private void setDamaged(boolean damaged) {
        this.getEntityData().set(DAMAGED, damaged);
        this.damageTicks = damaged ? 7 : 0;
    }

    private boolean getDamaged() {
        return this.getEntityData().get(DAMAGED);
    }

    private void setAttack(boolean attacking) {
        this.getEntityData().set(ATTACK, attacking);
        this.attackTicks = attacking ? 40 : 0;
    }

    private boolean getAttack() {
        return this.getEntityData().get(ATTACK);
    }

    /**
     * Performs a melee attack on the target
     * @param target entity to hit
     * @param squaredDistance distance to target
     */
    private void attack(LivingEntity target, double squaredDistance) {
        double d = this.getBbWidth() * 2.0f * (this.getBbWidth() * 2.0f) + target.getBbWidth();;
        if (squaredDistance <= d) {
            this.swing(InteractionHand.MAIN_HAND);
            this.doHurtTarget(target);
        }
    }

    private <E extends IAnimatable> PlayState predicate(AnimationEvent<E> event) {

        if (event.getController().getCurrentAnimation() != null && event.getController().getCurrentAnimation().animationName.equals("animation.model.attack") && !this.getAttack()) {
            event.getController().markNeedsReload();
        }

        if (this.getAttack()) {
            event.getController().setAnimation(new AnimationBuilder().addAnimation("animation.model.attack", ILoopType.EDefaultLoopTypes.PLAY_ONCE));
        } else if (this.getDamaged()) {
            event.getController().setAnimation(new AnimationBuilder().addAnimation("animation.model.damage", ILoopType.EDefaultLoopTypes.PLAY_ONCE));
        } else if (event.getLimbSwingAmount() <= -0.05D || event.getLimbSwingAmount() >= 0.05D) {
            event.getController().setAnimation(new AnimationBuilder().addAnimation("animation.model.walk", ILoopType.EDefaultLoopTypes.LOOP));
        } else {
            return PlayState.STOP;
        }

        return PlayState.CONTINUE;
    }

    @Override
    public void registerControllers(AnimationData animationData) {
        animationData.addAnimationController(new AnimationController<>(this, "controller", 0, this::predicate));
    }

    @Override
    public AnimationFactory getFactory() {
        return this.factory;
    }

    /**
     * AI Goal for a melee attack with an associated animation.
     * Sets animation data on the entity and then the tick of the entity will
     * perform the attack at the correct time.
     */
    private static class AnimatedMeleeAttackGoal extends MeleeAttackGoal {
        private final FrostMinerEntity miner;

        public AnimatedMeleeAttackGoal(FrostMinerEntity miner, double speed, boolean pauseWhenMobIdle) {
            super(miner, speed, pauseWhenMobIdle);
            this.miner = miner;
        }

        @Override
        protected void checkAndPerformAttack(LivingEntity target, double squaredDistance) {
            double d = this.getAttackReachSqr(target);
            if (squaredDistance <= d && this.getTicksUntilNextAttack() <= 0 && !this.miner.getAttack()) {
                this.miner.setAttack(true);
                this.resetAttackCooldown();
            }
        }
    }
}
