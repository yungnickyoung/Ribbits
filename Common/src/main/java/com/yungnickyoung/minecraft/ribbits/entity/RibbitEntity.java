package com.yungnickyoung.minecraft.ribbits.entity;

import com.yungnickyoung.minecraft.ribbits.data.RibbitData;
import com.yungnickyoung.minecraft.ribbits.entity.goal.RibbitPlayMusicGoal;
import com.yungnickyoung.minecraft.ribbits.module.EntityDataSerializerModule;
import com.yungnickyoung.minecraft.ribbits.module.RibbitProfessionModule;
import com.yungnickyoung.minecraft.ribbits.module.RibbitUmbrellaTypeModule;
import com.yungnickyoung.minecraft.ribbits.module.SoundModule;
import net.minecraft.core.BlockPos;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.syncher.EntityDataAccessor;
import net.minecraft.network.syncher.EntityDataSerializers;
import net.minecraft.network.syncher.SynchedEntityData;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.sounds.SoundEvent;
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
import net.minecraft.world.entity.player.Player;
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
import java.util.HashSet;
import java.util.Set;

public class RibbitEntity extends AgeableMob implements IAnimatable {
    private final AnimationFactory factory = GeckoLibUtil.createFactory(this);
    private final RibbitPlayMusicGoal musicGoal = new RibbitPlayMusicGoal(this);

    private static final EntityDataAccessor<RibbitData> RIBBIT_DATA = SynchedEntityData.defineId(RibbitEntity.class, EntityDataSerializerModule.RIBBIT_DATA_SERIALIZER);
    private static final EntityDataAccessor<Boolean> PLAYING_INSTRUMENT = SynchedEntityData.defineId(RibbitEntity.class, EntityDataSerializers.BOOLEAN);

    // NOTE: Fields below here are used only on Server
    private int ticksPlayingMusic;
    private final Set<RibbitEntity> ribbitsPlayingMusic = new HashSet<>();
    private final Set<Player> playersHearingMusic = new HashSet<>();
    private RibbitEntity masterRibbit;

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
        this.entityData.define(RIBBIT_DATA, new RibbitData(RibbitProfessionModule.getRandomProfession(), RibbitUmbrellaTypeModule.getRandomUmbrellaType()));
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

        if (RibbitProfessionModule.isNitwit(this.getRibbitData().getProfession())) {
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

    public int getTicksPlayingMusic() {
        return this.ticksPlayingMusic;
    }

    public void setTicksPlayingMusic(int ticksPlayingMusic) {
        this.ticksPlayingMusic = ticksPlayingMusic;
    }

    public Set<RibbitEntity> getRibbitsPlayingMusic() {
        return ribbitsPlayingMusic;
    }

    public void addRibbitToPlayingMusic(RibbitEntity ribbit) {
        this.ribbitsPlayingMusic.add(ribbit);
    }

    public Set<Player> getPlayersHearingMusic() {
        return this.playersHearingMusic;
    }

    public RibbitEntity getMasterRibbit() {
        return this.masterRibbit;
    }

    public void setMasterRibbit(RibbitEntity masterRibbit) {
        this.masterRibbit = masterRibbit;
    }

    public void findNewMasterRibbit() {
        RibbitEntity newMaster = this.getRibbitsPlayingMusic().stream().findAny().orElse(null);

        if (newMaster != null) {
            for (RibbitEntity ribbit : this.getRibbitsPlayingMusic()) {
                ribbit.setMasterRibbit(newMaster);
            }

            this.getRibbitsPlayingMusic().remove(newMaster);
            newMaster.getRibbitsPlayingMusic().addAll(this.getRibbitsPlayingMusic());
            newMaster.getPlayersHearingMusic().addAll(this.getPlayersHearingMusic());
            newMaster.setTicksPlayingMusic(this.getTicksPlayingMusic());
        }
    }

    @Override
    public void remove(RemovalReason reason) {
        super.remove(reason);

        if (this.equals(this.getMasterRibbit())) {
            findNewMasterRibbit();
        }
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
