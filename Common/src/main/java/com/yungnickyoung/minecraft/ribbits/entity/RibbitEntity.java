package com.yungnickyoung.minecraft.ribbits.entity;

import com.yungnickyoung.minecraft.ribbits.data.RibbitData;
import com.yungnickyoung.minecraft.ribbits.entity.npc.RibbitProfessions;
import com.yungnickyoung.minecraft.ribbits.entity.npc.RibbitUmbrellaTypes;
import com.yungnickyoung.minecraft.ribbits.module.SoundModule;
import net.minecraft.core.BlockPos;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.network.syncher.EntityDataAccessor;
import net.minecraft.network.syncher.EntityDataSerializer;
import net.minecraft.network.syncher.EntityDataSerializers;
import net.minecraft.network.syncher.SynchedEntityData;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.AgeableMob;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.ai.attributes.AttributeSupplier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.ai.goal.FloatGoal;
import net.minecraft.world.entity.ai.goal.LookAtPlayerGoal;
import net.minecraft.world.entity.ai.goal.RandomStrollGoal;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.state.BlockState;
import software.bernie.geckolib3.core.IAnimatable;
import software.bernie.geckolib3.core.PlayState;
import software.bernie.geckolib3.core.builder.AnimationBuilder;
import software.bernie.geckolib3.core.builder.ILoopType;
import software.bernie.geckolib3.core.controller.AnimationController;
import software.bernie.geckolib3.core.event.predicate.AnimationEvent;
import software.bernie.geckolib3.core.manager.AnimationData;
import software.bernie.geckolib3.core.manager.AnimationFactory;
import software.bernie.geckolib3.util.GeckoLibUtil;

import javax.annotation.Nullable;

public class RibbitEntity extends AgeableMob implements IAnimatable {
    private final AnimationFactory factory = GeckoLibUtil.createFactory(this);

    /**
     * Creating and then registering the data serializer for Ribbits. This mimics the serializer used for
     * vanilla VillagerData, but tweaked for our purposes. This can be moved elsewhere if necessary.
     */
    public static final EntityDataSerializer<RibbitData> RIBBIT_DATA_SERIALIZER = new EntityDataSerializer<>() {
        public void write(FriendlyByteBuf buff, RibbitData data) {
            buff.writeVarInt(data.getProfession().getId());
            buff.writeVarInt(data.getUmbrellaType().getId());
        }

        public RibbitData read(FriendlyByteBuf buf) {
            return new RibbitData(RibbitProfessions.getProfession(buf.readVarInt()), RibbitUmbrellaTypes.getUmbrellaType(buf.readVarInt()));
        }

        public RibbitData copy(RibbitData data) {
            return data;
        }
    };

    static {
        EntityDataSerializers.registerSerializer(RIBBIT_DATA_SERIALIZER);
    }

    private static final EntityDataAccessor<RibbitData> RIBBIT_DATA = SynchedEntityData.defineId(RibbitEntity.class, RIBBIT_DATA_SERIALIZER);

    public RibbitEntity(EntityType<RibbitEntity> entityType, Level level) {
        super(entityType, level);
    }

    @Override
    protected void registerGoals() {
        super.registerGoals();
        this.goalSelector.addGoal(0, new FloatGoal(this));
        this.goalSelector.addGoal(1, new LookAtPlayerGoal(this, Player.class, 8.0F));
        this.goalSelector.addGoal(2, new RandomStrollGoal(this, 1.0D));
    }

    @Override
    protected void defineSynchedData() {
        super.defineSynchedData();
        this.entityData.define(RIBBIT_DATA, new RibbitData(RibbitProfessions.getRandomProfession(), RibbitUmbrellaTypes.getRandomUmbrellaType()));
    }

    @Nullable
    @Override
    public AgeableMob getBreedOffspring(ServerLevel level, AgeableMob parent) {
        return null;
    }

    public RibbitData getRibbitData() {
        return this.entityData.get(RIBBIT_DATA);
    }

    private PlayState predicate(AnimationEvent event) {
        if (event.getLimbSwingAmount() > 0.15D || event.getLimbSwingAmount() < -0.15D) {
            event.getController().setAnimation(new AnimationBuilder().addAnimation(this.level.isRaining() && this.isInWaterOrRain() && !this.isInWater() ? "walk_holding_1" : "walk", ILoopType.EDefaultLoopTypes.LOOP));
        } else {
            event.getController().setAnimation(new AnimationBuilder().addAnimation(this.level.isRaining() && this.isInWaterOrRain() && !this.isInWater() ? "idle_holding_1" : "idle", ILoopType.EDefaultLoopTypes.LOOP));
        }
        return PlayState.CONTINUE;
    }

    public static AttributeSupplier.Builder createRibbitAttributes() {
        return createMobAttributes()
                .add(Attributes.MAX_HEALTH, 15.0D)
                .add(Attributes.MOVEMENT_SPEED, 0.2D)
                .add(Attributes.ATTACK_DAMAGE, 1.0D);
    }

    @Nullable
    @Override
    protected SoundEvent getAmbientSound() {
        return SoundModule.ENTITY_RIBBIT_AMBIENT.get();
    }

    @Nullable
    @Override
    protected SoundEvent getHurtSound(DamageSource $$0) {
        return SoundModule.ENTITY_RIBBIT_HURT.get();
    }

    @Nullable
    @Override
    protected SoundEvent getDeathSound() {
        return SoundModule.ENTITY_RIBBIT_DEATH.get();
    }

    @Override
    protected void playStepSound(BlockPos pos, BlockState blockstate) {
        super.playStepSound(pos, blockstate);
        this.playSound(SoundModule.ENTITY_RIBBIT_STEP.get(), 1.0F, 1.0F);
    }

    @Override
    public void registerControllers(AnimationData data) {
        data.addAnimationController(new AnimationController(this, "controller", 5, this::predicate));
    }

    @Override
    public AnimationFactory getFactory() {
        return this.factory;
    }
}
