package com.yungnickyoung.minecraft.ribbits.entity;

import com.yungnickyoung.minecraft.ribbits.data.RibbitData;
import com.yungnickyoung.minecraft.ribbits.entity.goal.RibbitPlayMusicGoal;
import com.yungnickyoung.minecraft.ribbits.entity.npc.RibbitProfession;
import com.yungnickyoung.minecraft.ribbits.entity.npc.RibbitProfessions;
import com.yungnickyoung.minecraft.ribbits.entity.npc.RibbitUmbrellaTypes;
import com.yungnickyoung.minecraft.ribbits.module.SoundModule;
import net.minecraft.core.BlockPos;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.NbtOps;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.network.syncher.EntityDataAccessor;
import net.minecraft.network.syncher.EntityDataSerializer;
import net.minecraft.network.syncher.EntityDataSerializers;
import net.minecraft.network.syncher.SynchedEntityData;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.world.Difficulty;
import net.minecraft.world.DifficultyInstance;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.AgeableMob;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.MobSpawnType;
import net.minecraft.world.entity.SpawnGroupData;
import net.minecraft.world.entity.ai.attributes.AttributeSupplier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.ai.goal.FloatGoal;
import net.minecraft.world.entity.ai.goal.LookAtPlayerGoal;
import net.minecraft.world.entity.ai.goal.RandomStrollGoal;
import net.minecraft.world.entity.npc.VillagerData;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.entity.projectile.ProjectileUtil;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.ServerLevelAccessor;
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
    private final RibbitPlayMusicGoal musicGoal = new RibbitPlayMusicGoal(this);

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

    // NOTE: This boolean will only be set and tracked on the Client!
    private boolean playingMusic = false;

    private static final EntityDataAccessor<RibbitData> RIBBIT_DATA = SynchedEntityData.defineId(RibbitEntity.class, RIBBIT_DATA_SERIALIZER);
    private static final EntityDataAccessor<Boolean> PLAYING_INSTRUMENT = SynchedEntityData.defineId(RibbitEntity.class, EntityDataSerializers.BOOLEAN);

    public RibbitEntity(EntityType<RibbitEntity> entityType, Level level) {
        super(entityType, level);
        this.reassessGoals();
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
        this.entityData.define(PLAYING_INSTRUMENT, false);
    }

    @Override
    public void readAdditionalSaveData(CompoundTag tag) {
        super.readAdditionalSaveData(tag);
        this.reassessGoals();
    }

    @Override
    public void addAdditionalSaveData(CompoundTag tag) {
        super.addAdditionalSaveData(tag);

    }

    @Override
    public SpawnGroupData finalizeSpawn(ServerLevelAccessor level, DifficultyInstance difficulty, MobSpawnType spawnType, @Nullable SpawnGroupData groupData, @Nullable CompoundTag tag) {
        SpawnGroupData data = super.finalizeSpawn(level, difficulty, spawnType, groupData, tag);
        this.reassessGoals();
        return data;
    }

    public void reassessGoals() {
        if (this.level.isClientSide) {
            return;
        }

        this.goalSelector.removeGoal(this.musicGoal);

        if (RibbitProfession.nitwitProfessions.contains(this.getRibbitData().getProfession())) {
            this.goalSelector.addGoal(1, this.musicGoal);
        }
    }

    @Nullable
    @Override
    public AgeableMob getBreedOffspring(ServerLevel level, AgeableMob parent) {
        return null;
    }

    public RibbitData getRibbitData() {
        return this.entityData.get(RIBBIT_DATA);
    }

    public void setRibbitData(RibbitData data) {
        this.entityData.set(RIBBIT_DATA, data);
    }

    public boolean getPlayingInstrument() {
        return this.entityData.get(PLAYING_INSTRUMENT);
    }

    public void setPlayingInstrument(boolean playingInstrument) {
        this.entityData.set(PLAYING_INSTRUMENT, playingInstrument);
    }

    public boolean getPlayingMusic() {
        return this.playingMusic;
    }

    public void setPlayingMusic(boolean playingMusic) {
        this.playingMusic = playingMusic;
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

    private PlayState predicate(AnimationEvent<RibbitEntity> event) {
        if (getPlayingInstrument()) {
          event.getController().setAnimation(new AnimationBuilder().addAnimation("play_bongo"));
        } else if (event.getLimbSwingAmount() > 0.15D || event.getLimbSwingAmount() < -0.15D) {
            event.getController().setAnimation(new AnimationBuilder().addAnimation(this.level.isRaining() && this.isInWaterOrRain() && !this.isInWater() ? "walk_holding_1" : "walk", ILoopType.EDefaultLoopTypes.LOOP));
        } else {
            event.getController().setAnimation(new AnimationBuilder().addAnimation(this.level.isRaining() && this.isInWaterOrRain() && !this.isInWater() ? "idle_holding_1" : "idle", ILoopType.EDefaultLoopTypes.LOOP));
        }
        return PlayState.CONTINUE;
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
