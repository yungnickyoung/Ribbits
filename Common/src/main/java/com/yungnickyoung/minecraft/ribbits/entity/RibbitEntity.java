package com.yungnickyoung.minecraft.ribbits.entity;

import com.mojang.serialization.DataResult;
import com.mojang.serialization.Dynamic;
import com.yungnickyoung.minecraft.ribbits.RibbitsCommon;
import com.yungnickyoung.minecraft.ribbits.data.RibbitData;
import com.yungnickyoung.minecraft.ribbits.data.RibbitInstrument;
import com.yungnickyoung.minecraft.ribbits.data.RibbitProfession;
import com.yungnickyoung.minecraft.ribbits.entity.goal.RibbitApplyBuffGoal;
import com.yungnickyoung.minecraft.ribbits.entity.goal.RibbitFishGoal;
import com.yungnickyoung.minecraft.ribbits.entity.goal.RibbitPlayMusicGoal;
import com.yungnickyoung.minecraft.ribbits.entity.goal.RibbitWaterCropsGoal;
import com.yungnickyoung.minecraft.ribbits.module.EntityDataSerializerModule;
import com.yungnickyoung.minecraft.ribbits.module.RibbitInstrumentModule;
import com.yungnickyoung.minecraft.ribbits.module.RibbitProfessionModule;
import com.yungnickyoung.minecraft.ribbits.module.RibbitUmbrellaTypeModule;
import com.yungnickyoung.minecraft.ribbits.module.SoundModule;
import net.minecraft.core.BlockPos;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.NbtOps;
import net.minecraft.network.syncher.EntityDataAccessor;
import net.minecraft.network.syncher.EntityDataSerializers;
import net.minecraft.network.syncher.SynchedEntityData;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.world.DifficultyInstance;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.entity.AgeableMob;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.MobSpawnType;
import net.minecraft.world.entity.SpawnGroupData;
import net.minecraft.world.entity.ai.attributes.AttributeSupplier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.ai.goal.FloatGoal;
import net.minecraft.world.entity.ai.goal.LookAtPlayerGoal;
import net.minecraft.world.entity.ai.goal.RandomStrollGoal;
import net.minecraft.world.entity.ai.navigation.GroundPathNavigation;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.ServerLevelAccessor;
import net.minecraft.world.level.block.state.BlockState;
import org.jetbrains.annotations.NotNull;
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
    private final RibbitPlayMusicGoal musicGoal = new RibbitPlayMusicGoal(this, 1.0f, 2000, 3000);
    private final RibbitWaterCropsGoal waterCropsGoal = new RibbitWaterCropsGoal(this, 8.0d, 100);
    private final RibbitFishGoal fishGoal = new RibbitFishGoal(this, 16.0d);
    private final RibbitApplyBuffGoal applyBuffGoal = new RibbitApplyBuffGoal(this, 16.0d, 100, 600, MobEffects.REGENERATION, MobEffects.DAMAGE_RESISTANCE, MobEffects.DAMAGE_BOOST, MobEffects.JUMP, MobEffects.DIG_SPEED, MobEffects.HEALTH_BOOST);

    private static final EntityDataAccessor<RibbitData> RIBBIT_DATA = SynchedEntityData.defineId(RibbitEntity.class, EntityDataSerializerModule.RIBBIT_DATA_SERIALIZER);
    private static final EntityDataAccessor<Boolean> PLAYING_INSTRUMENT = SynchedEntityData.defineId(RibbitEntity.class, EntityDataSerializers.BOOLEAN);
    private static final EntityDataAccessor<Boolean> UMBRELLA_FALLING = SynchedEntityData.defineId(RibbitEntity.class, EntityDataSerializers.BOOLEAN);
    private static final EntityDataAccessor<Boolean> WATERING = SynchedEntityData.defineId(RibbitEntity.class, EntityDataSerializers.BOOLEAN);
    private static final EntityDataAccessor<Boolean> FISHING = SynchedEntityData.defineId(RibbitEntity.class, EntityDataSerializers.BOOLEAN);

    // NOTE: Fields below here are used only on Server
    private int ticksPlayingMusic;

    /**
     * Set of Ribbits playing music with this Ribbit as the master.
     * Only used if this Ribbit is the master.
     * Does not include the master Ribbit itself.
     */
    private Set<RibbitEntity> ribbitsPlayingMusic = new HashSet<>();
    private Set<Player> playersHearingMusic = new HashSet<>();
    private Set<RibbitInstrument> bandMembers = new HashSet<>();
    private RibbitEntity masterRibbit;

    private int buffCooldown = 0;

    public RibbitEntity(EntityType<RibbitEntity> entityType, Level level) {
        super(entityType, level);

        ((GroundPathNavigation)this.getNavigation()).setCanOpenDoors(true);

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
    public void aiStep() {
        super.aiStep();

        if (!this.level.isClientSide) {
            if (this.onGround && this.getUmbrellaFalling()) {
                this.setUmbrellaFalling(false);
            }

            if (this.fallDistance >= 4 || this.getUmbrellaFalling()) {
                this.resetFallDistance();
                this.push(0.0f, 0.075f, 0.0f);
                this.setUmbrellaFalling(true);
            }

            if (this.buffCooldown > 0) {
                this.buffCooldown--;
            }
        }
    }

    @Override
    protected void defineSynchedData() {
        super.defineSynchedData();
        this.entityData.define(RIBBIT_DATA, new RibbitData(RibbitProfessionModule.NITWIT, RibbitUmbrellaTypeModule.UMBRELLA_1, RibbitInstrumentModule.NONE));
        this.entityData.define(PLAYING_INSTRUMENT, false);
        this.entityData.define(UMBRELLA_FALLING, false);
        this.entityData.define(WATERING, false);
        this.entityData.define(FISHING, false);
    }

    @Override
    public void readAdditionalSaveData(@NotNull CompoundTag tag) {
        super.readAdditionalSaveData(tag);
        if (tag.contains("RibbitData", 10)) {
            DataResult<RibbitData> dataResult = RibbitData.CODEC.parse(new Dynamic<>(NbtOps.INSTANCE, tag.get("RibbitData")));
            dataResult.resultOrPartial(RibbitsCommon.LOGGER::error).ifPresent(this::setRibbitData);
        }

        this.reassessGoals();
    }

    @Override
    public void addAdditionalSaveData(@NotNull CompoundTag tag) {
        super.addAdditionalSaveData(tag);
        RibbitData.CODEC.encodeStart(NbtOps.INSTANCE, this.getRibbitData())
                .resultOrPartial(RibbitsCommon.LOGGER::error)
                .ifPresent(t -> tag.put("RibbitData", t));
    }

    @Override
    public SpawnGroupData finalizeSpawn(ServerLevelAccessor level, DifficultyInstance difficulty, MobSpawnType spawnType, @Nullable SpawnGroupData groupData, @Nullable CompoundTag tag) {
        SpawnGroupData data = super.finalizeSpawn(level, difficulty, spawnType, groupData, tag);

        if (spawnType == MobSpawnType.SPAWN_EGG) {
            if (tag.contains("Profession")) {
                String[] professionId = tag.getString("Profession").split(":");
                RibbitProfession profession = RibbitProfessionModule.getProfession(new ResourceLocation(professionId[0], professionId[1]));
                this.setRibbitData(new RibbitData(profession, RibbitUmbrellaTypeModule.getRandomUmbrellaType(), RibbitInstrumentModule.NONE));
            }
        } else {
            CompoundTag ribbitDataTag = tag != null ? tag.getCompound("RibbitData") : new CompoundTag();
            RibbitProfession profession = RibbitProfessionModule.NITWIT;

            if (ribbitDataTag.contains("profession", CompoundTag.TAG_STRING)) {
                profession = RibbitProfessionModule.getProfession(new ResourceLocation(ribbitDataTag.getString("profession")));
            }
            this.setRibbitData(new RibbitData(profession, RibbitUmbrellaTypeModule.getRandomUmbrellaType(), RibbitInstrumentModule.NONE));
        }

        this.reassessGoals();
        return data;
    }

    public void reassessGoals() {
        if (this.level.isClientSide) {
            return;
        }

        this.goalSelector.removeGoal(this.musicGoal);
        this.goalSelector.removeGoal(this.waterCropsGoal);
        this.goalSelector.removeGoal(this.fishGoal);
        this.goalSelector.removeGoal(this.applyBuffGoal);

        if (this.getRibbitData().getProfession().equals(RibbitProfessionModule.NITWIT)) {
            this.goalSelector.addGoal(3, this.musicGoal);
        } else if (this.getRibbitData().getProfession().equals(RibbitProfessionModule.GARDENER)) {
            this.goalSelector.addGoal(3, this.waterCropsGoal);
        } else if (this.getRibbitData().getProfession().equals(RibbitProfessionModule.FISHERMAN)) {
            this.goalSelector.addGoal(3, this.fishGoal);
        } else if (this.getRibbitData().getProfession().equals(RibbitProfessionModule.SORCERER)) {
            this.goalSelector.addGoal(3, this.applyBuffGoal);
        }
    }

    @Nullable
    @Override
    public AgeableMob getBreedOffspring(ServerLevel level, AgeableMob parent) {
        return null;
    }

    public int getBuffCooldown() {
        return this.buffCooldown;
    }

    public void setBuffCooldown(int cooldown) {
        this.buffCooldown = cooldown;
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

    public boolean getUmbrellaFalling() {
        return this.entityData.get(UMBRELLA_FALLING);
    }

    public void setUmbrellaFalling(boolean umbrellaFalling) {
        this.entityData.set(UMBRELLA_FALLING, umbrellaFalling);
    }

    public boolean getWatering() {
        return this.entityData.get(WATERING);
    }

    public void setWatering(boolean isWatering) {
        this.entityData.set(WATERING, isWatering);
    }

    public boolean getFishing() {
        return this.entityData.get(FISHING);
    }

    public void setFishing(boolean isFishing) {
        this.entityData.set(FISHING, isFishing);
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

    public void setRibbitsPlayingMusic(Set<RibbitEntity> ribbitsPlayingMusic) {
        this.ribbitsPlayingMusic = new HashSet<>(ribbitsPlayingMusic);
    }


    public void addRibbitToPlayingMusic(RibbitEntity ribbit) {
        this.ribbitsPlayingMusic.add(ribbit);
    }

    public void removeRibbitFromPlayingMusic(RibbitEntity ribbit) {
        this.ribbitsPlayingMusic.remove(ribbit);
    }

    public Set<Player> getPlayersHearingMusic() {
        return this.playersHearingMusic;
    }

    public void setPlayersHearingMusic(Set<Player> playersHearingMusic) {
        this.playersHearingMusic = new HashSet<>(playersHearingMusic);
    }

    public RibbitEntity getMasterRibbit() {
        return this.masterRibbit;
    }

    public void setMasterRibbit(RibbitEntity masterRibbit) {
        this.masterRibbit = masterRibbit;
    }

    public boolean isMasterRibbit() {
        return this.equals(this.getMasterRibbit());
    }

    public void findNewMasterRibbit() {
        RibbitEntity newMaster = this.getRibbitsPlayingMusic().stream().findAny().orElse(null);

        if (newMaster != null) {
            for (RibbitEntity ribbit : this.getRibbitsPlayingMusic()) {
                ribbit.setMasterRibbit(newMaster);
            }

            this.getRibbitsPlayingMusic().remove(this);
            this.removeBandMember(this.getRibbitData().getInstrument());

            newMaster.setRibbitsPlayingMusic(this.getRibbitsPlayingMusic());
            newMaster.setPlayersHearingMusic(this.getPlayersHearingMusic());
            newMaster.setTicksPlayingMusic(this.getTicksPlayingMusic());
            newMaster.setBandMembers(this.getBandMembers());
        }

        this.getRibbitsPlayingMusic().clear();
        this.getPlayersHearingMusic().clear();;
        this.setTicksPlayingMusic(0);
        this.clearBandMembers();
    }

    public boolean isBandFull() {
        return this.bandMembers.size() == RibbitInstrumentModule.getNumInstruments();
    }

    public void addBandMember(RibbitInstrument instrument) {
        this.bandMembers.add(instrument);
    }

    public void removeBandMember(RibbitInstrument instrument) {
        this.bandMembers.remove(instrument);
    }

    public void clearBandMembers() {
        this.bandMembers.clear();
    }

    public Set<RibbitInstrument> getBandMembers() {
        return this.bandMembers;
    }

    public void setBandMembers(Set<RibbitInstrument> bandMembers) {
        this.bandMembers = new HashSet<>(bandMembers);
    }

    @Override
    public void remove(RemovalReason reason) {
        super.remove(reason);

        if (this.isMasterRibbit()) {
            findNewMasterRibbit();
        }
    }

    public static AttributeSupplier.Builder createRibbitAttributes() {
        return createMobAttributes()
                .add(Attributes.MAX_HEALTH, 15.0D)
                .add(Attributes.MOVEMENT_SPEED, 0.15D)
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
        if (this.getUmbrellaFalling()) {
            event.getController().setAnimation(new AnimationBuilder().addAnimation(this.getRibbitData().getProfession().equals(RibbitProfessionModule.FISHERMAN) ? "idle_holding_2" : "idle_holding_1"));
        } else if (getPlayingInstrument() && this.getRibbitData().getInstrument() != RibbitInstrumentModule.NONE) {
            event.getController().setAnimation(new AnimationBuilder().addAnimation(this.getRibbitData().getInstrument().getAnimationName()));
        } else if (event.getLimbSwingAmount() > 0.05D || event.getLimbSwingAmount() < -0.05D) {
            if (this.getRibbitData().getProfession().equals(RibbitProfessionModule.FISHERMAN)) {
                event.getController().setAnimation(new AnimationBuilder().addAnimation("walk_holding_2"));
            } else {
                event.getController().setAnimation(new AnimationBuilder().addAnimation(this.level.isRaining() && this.isInWaterOrRain() && !this.isInWater() ? "walk_holding_1" : "walk", ILoopType.EDefaultLoopTypes.LOOP));
            }
          } else {
            if (this.getRibbitData().getProfession().equals(RibbitProfessionModule.FISHERMAN)) {
                event.getController().setAnimation(new AnimationBuilder().addAnimation("idle_holding_2"));
            } else {
                event.getController().setAnimation(new AnimationBuilder().addAnimation(this.level.isRaining() && this.isInWaterOrRain() && !this.isInWater() ? "idle_holding_1" : "idle", ILoopType.EDefaultLoopTypes.LOOP));
            }
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
