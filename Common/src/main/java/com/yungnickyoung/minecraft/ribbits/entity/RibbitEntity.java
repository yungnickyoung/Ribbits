package com.yungnickyoung.minecraft.ribbits.entity;

import com.mojang.serialization.DataResult;
import com.mojang.serialization.Dynamic;
import com.yungnickyoung.minecraft.ribbits.RibbitsCommon;
import com.yungnickyoung.minecraft.ribbits.data.RibbitData;
import com.yungnickyoung.minecraft.ribbits.data.RibbitInstrument;
import com.yungnickyoung.minecraft.ribbits.data.RibbitProfession;
import com.yungnickyoung.minecraft.ribbits.entity.goal.RibbitApplyBuffGoal;
import com.yungnickyoung.minecraft.ribbits.entity.goal.RibbitFishGoal;
import com.yungnickyoung.minecraft.ribbits.entity.goal.RibbitGoHomeGoal;
import com.yungnickyoung.minecraft.ribbits.entity.goal.RibbitPlayMusicGoal;
import com.yungnickyoung.minecraft.ribbits.entity.goal.RibbitStopAndStareAtFrogGoal;
import com.yungnickyoung.minecraft.ribbits.entity.goal.RibbitStrollGoal;
import com.yungnickyoung.minecraft.ribbits.entity.goal.RibbitWaterCropsGoal;
import com.yungnickyoung.minecraft.ribbits.module.EntityDataSerializerModule;
import com.yungnickyoung.minecraft.ribbits.module.RibbitInstrumentModule;
import com.yungnickyoung.minecraft.ribbits.module.RibbitProfessionModule;
import com.yungnickyoung.minecraft.ribbits.module.RibbitTradeModule;
import com.yungnickyoung.minecraft.ribbits.module.RibbitUmbrellaTypeModule;
import com.yungnickyoung.minecraft.ribbits.module.SoundModule;
import net.minecraft.core.BlockPos;
import net.minecraft.core.particles.ParticleOptions;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.NbtOps;
import net.minecraft.network.syncher.EntityDataAccessor;
import net.minecraft.network.syncher.EntityDataSerializers;
import net.minecraft.network.syncher.SynchedEntityData;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.world.DifficultyInstance;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.AgeableMob;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.ExperienceOrb;
import net.minecraft.world.entity.MobSpawnType;
import net.minecraft.world.entity.SpawnGroupData;
import net.minecraft.world.entity.ai.attributes.AttributeSupplier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.ai.goal.FloatGoal;
import net.minecraft.world.entity.ai.goal.LookAtPlayerGoal;
import net.minecraft.world.entity.ai.goal.OpenDoorGoal;
import net.minecraft.world.entity.ai.goal.PanicGoal;
import net.minecraft.world.entity.ai.navigation.GroundPathNavigation;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.trading.Merchant;
import net.minecraft.world.item.trading.MerchantOffer;
import net.minecraft.world.item.trading.MerchantOffers;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.ServerLevelAccessor;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.Vec3;
import org.jetbrains.annotations.NotNull;
import software.bernie.geckolib.animatable.GeoEntity;
import software.bernie.geckolib.core.animatable.GeoAnimatable;
import software.bernie.geckolib.core.animatable.instance.AnimatableInstanceCache;
import software.bernie.geckolib.core.animation.AnimatableManager;
import software.bernie.geckolib.core.animation.AnimationController;
import software.bernie.geckolib.core.animation.AnimationState;
import software.bernie.geckolib.core.animation.RawAnimation;
import software.bernie.geckolib.core.object.PlayState;
import software.bernie.geckolib.util.GeckoLibUtil;

import javax.annotation.Nullable;
import java.time.LocalDate;
import java.time.temporal.ChronoField;
import java.util.HashSet;
import java.util.Random;
import java.util.Set;

public class RibbitEntity extends AgeableMob implements GeoEntity, Merchant {
    private final AnimatableInstanceCache cache = GeckoLibUtil.createInstanceCache(this);

    private static final RawAnimation IDLE = RawAnimation.begin().thenPlay("idle");
    private static final RawAnimation IDLE_HOLDING_1 = RawAnimation.begin().thenPlay("idle_holding_1");
    private static final RawAnimation IDLE_HOLDING_2 = RawAnimation.begin().thenPlay("idle_holding_2");
    private static final RawAnimation IDLE_HOLDING_HAT = RawAnimation.begin().thenPlay("idle_holding_hat");
    private static final RawAnimation IDLE_HOLDING_FISHERMAN = RawAnimation.begin().thenPlay("idle_holding_fisherman");
    private static final RawAnimation WALK = RawAnimation.begin().thenPlay("walk");
    private static final RawAnimation WALK_HOLDING_1 = RawAnimation.begin().thenPlay("walk_holding_1");
    private static final RawAnimation WALK_HOLDING_2 = RawAnimation.begin().thenPlay("walk_holding_2");
    private static final RawAnimation WALK_HOLDING_HAT = RawAnimation.begin().thenPlay("walk_holding_hat");
    private static final RawAnimation WALK_HOLDING_FISHERMAN = RawAnimation.begin().thenPlay("walk_holding_fisherman");
    private static final RawAnimation SORCERER_BUFF = RawAnimation.begin().thenPlay("spell");
    private static final RawAnimation SORCERER_BUFF_HOLDING = RawAnimation.begin().thenPlay("spell_holding");
    private static final RawAnimation FISH = RawAnimation.begin().thenPlay("fishing");
    private static final RawAnimation FISH_HOLDING = RawAnimation.begin().thenPlay("fishing_holding");
    private static final RawAnimation WATER_CROPS = RawAnimation.begin().thenPlay("water_crops");
    private static final RawAnimation WATER_CROPS_HOLDING = RawAnimation.begin().thenPlay("water_crops_holding");

    @Nullable
    private Player tradingPlayer;
    @Nullable
    protected MerchantOffers offers;
    private long lastRestockGameTime;
    private int numberOfRestocksToday;
    private long lastRestockCheckDayTime;

    // How much to multiply movement speed when in water
    public static final float WATER_SPEED_MULTIPLIER = 2.0f;

    private final RibbitPlayMusicGoal musicGoal = new RibbitPlayMusicGoal(this, 1.0f, 2000, 3000);
    private final RibbitWaterCropsGoal waterCropsGoal = new RibbitWaterCropsGoal(this, 16.0d, 1.0f, 1200);
    private final RibbitFishGoal fishGoal = new RibbitFishGoal(this, 16.0d, 1.0f, 600, 1800);
    private final RibbitApplyBuffGoal applyBuffGoal = new RibbitApplyBuffGoal(this, 32.0d, 12000);

    private static final EntityDataAccessor<RibbitData> RIBBIT_DATA = SynchedEntityData.defineId(RibbitEntity.class, EntityDataSerializerModule.RIBBIT_DATA_SERIALIZER);
    private static final EntityDataAccessor<Boolean> PLAYING_INSTRUMENT = SynchedEntityData.defineId(RibbitEntity.class, EntityDataSerializers.BOOLEAN);
    private static final EntityDataAccessor<Boolean> UMBRELLA_FALLING = SynchedEntityData.defineId(RibbitEntity.class, EntityDataSerializers.BOOLEAN);
    private static final EntityDataAccessor<Boolean> WATERING = SynchedEntityData.defineId(RibbitEntity.class, EntityDataSerializers.BOOLEAN);
    private static final EntityDataAccessor<Boolean> FISHING = SynchedEntityData.defineId(RibbitEntity.class, EntityDataSerializers.BOOLEAN);
    private static final EntityDataAccessor<Boolean> BUFFING = SynchedEntityData.defineId(RibbitEntity.class, EntityDataSerializers.BOOLEAN);

    // These fields are used to prevent threadlocking by accessing entityData on rendering thread
    private RibbitData sidedRibbitData = new RibbitData(RibbitProfessionModule.NITWIT, RibbitUmbrellaTypeModule.UMBRELLA_1, RibbitInstrumentModule.NONE);
    private boolean isPlayingInstrument = false;
    private boolean isUmbrellaFalling = false;
    private boolean isWatering = false;
    private boolean isFishing = false;
    private boolean isBuffing = false;

    // NOTE: Fields below here are used only on Server
    private int ticksPlayingMusic;

    private BlockPos homePosition;

    /**
     * Set of Ribbits playing music with this Ribbit as the master.
     * Only used if this Ribbit is the master.
     * Does not include the master Ribbit itself.
     */
    private Set<RibbitEntity> ribbitsPlayingMusic = new HashSet<>();
    private Set<Player> playersHearingMusic = new HashSet<>();
    private Set<RibbitInstrument> bandMembers = new HashSet<>();
    @Nullable private RibbitEntity masterRibbit;

    private int buffCooldown = 0;
    private int waterCropsCooldown = 0;

    public RibbitEntity(EntityType<RibbitEntity> entityType, Level level) {
        super(entityType, level);

        ((GroundPathNavigation)this.getNavigation()).setCanOpenDoors(true);

        this.reassessGoals();
    }

    @Override
    protected void registerGoals() {
        super.registerGoals();
        this.goalSelector.addGoal(0, new OpenDoorGoal(this, true));
        this.goalSelector.addGoal(0, new FloatGoal(this));
        this.goalSelector.addGoal(1, new RibbitGoHomeGoal(this, 1.8f, 1f, 60));
        this.goalSelector.addGoal(2, new PanicGoal(this, 1.5D));
        this.goalSelector.addGoal(3, new RibbitStopAndStareAtFrogGoal(this, 4.0F));
        this.goalSelector.addGoal(4, new LookAtPlayerGoal(this, Player.class, 8.0F));
        this.goalSelector.addGoal(5, new RibbitStrollGoal(this, 1.0D, 16));
    }

    @Override
    public void aiStep() {
        super.aiStep();

        if (!this.level().isClientSide) {
            if (this.tickCount % 600 == 0 && this.getHealth() < this.getMaxHealth()) {
                this.setHealth(Math.min(this.getHealth() + 1, this.getMaxHealth()));
            }

            if (this.onGround() && this.getUmbrellaFalling()) {
                this.setUmbrellaFalling(false);
            }

            if (this.fallDistance >= 2 || this.getUmbrellaFalling()) {
                Vec3 velocity = this.getDeltaMovement();
                this.resetFallDistance();
                this.setDeltaMovement(velocity.x, -0.1d, velocity.z);
                this.setUmbrellaFalling(true);
            }

            if (this.buffCooldown > 0) {
                this.buffCooldown--;
            }

            if (this.waterCropsCooldown > 0) {
                this.waterCropsCooldown--;
            }

            if (this.shouldRestock()) {
                this.restock();
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
        this.entityData.define(BUFFING, false);
    }

    @Override
    public void readAdditionalSaveData(@NotNull CompoundTag tag) {
        super.readAdditionalSaveData(tag);
        if (tag.contains("RibbitData", 10)) {
            DataResult<RibbitData> dataResult = RibbitData.CODEC.parse(new Dynamic<>(NbtOps.INSTANCE, tag.get("RibbitData")));
            dataResult.resultOrPartial(RibbitsCommon.LOGGER::error).ifPresent(this::setRibbitData);
        }

        if (tag.contains("Offers", 10)) {
            this.offers = new MerchantOffers(tag.getCompound("Offers"));
        }

        if (tag.contains("HomePosX") && tag.contains("HomePosY") && tag.contains("HomePosZ")) {
            this.homePosition = new BlockPos(tag.getInt("HomePosX"), tag.getInt("HomePosY"), tag.getInt("HomePosZ"));
        } else {
            this.homePosition = new BlockPos(this.blockPosition());
        }

        this.reassessGoals();
    }

    @Override
    public void addAdditionalSaveData(@NotNull CompoundTag tag) {
        super.addAdditionalSaveData(tag);
        RibbitData.CODEC.encodeStart(NbtOps.INSTANCE, this.getRibbitData())
                .resultOrPartial(RibbitsCommon.LOGGER::error)
                .ifPresent(t -> tag.put("RibbitData", t));

        MerchantOffers merchantOffers = this.getOffers();
        if (!merchantOffers.isEmpty()) {
            tag.put("Offers", merchantOffers.createTag());
        }

        if (this.homePosition != null) {
            tag.putInt("HomePosX", this.homePosition.getX());
            tag.putInt("HomePosY", this.homePosition.getY());
            tag.putInt("HomePosZ", this.homePosition.getZ());
        }
    }

    @Override
    public void onSyncedDataUpdated(EntityDataAccessor<?> dataAccessor) {
        super.onSyncedDataUpdated(dataAccessor);

        if (RIBBIT_DATA.equals(dataAccessor)) {
            this.sidedRibbitData = this.entityData.get(RIBBIT_DATA);
        } else if (UMBRELLA_FALLING.equals(dataAccessor)) {
            this.isUmbrellaFalling = this.entityData.get(UMBRELLA_FALLING);
        } else if (PLAYING_INSTRUMENT.equals(dataAccessor)) {
            this.isPlayingInstrument = this.entityData.get(PLAYING_INSTRUMENT);
        } else if (FISHING.equals(dataAccessor)) {
            this.isFishing = this.entityData.get(FISHING);
        } else if (WATERING.equals(dataAccessor)) {
            this.isWatering = this.entityData.get(WATERING);
        } else if (BUFFING.equals(dataAccessor)) {
            this.isBuffing = this.entityData.get(BUFFING);
        }
    }

    @Override
    public SpawnGroupData finalizeSpawn(ServerLevelAccessor level, DifficultyInstance difficulty, MobSpawnType spawnType, @Nullable SpawnGroupData groupData, @Nullable CompoundTag tag) {
        SpawnGroupData data = super.finalizeSpawn(level, difficulty, spawnType, groupData, tag);

        if (spawnType == MobSpawnType.SPAWN_EGG || spawnType == MobSpawnType.DISPENSER) {
            if (tag.contains("Profession")) {
                String[] professionId = tag.getString("Profession").split(":");
                RibbitProfession profession = RibbitProfessionModule.getProfession(new ResourceLocation(professionId[0], professionId[1]));
                if (profession == null) {
                    RibbitsCommon.LOGGER.error("Invalid Ribbit profession ID: {}", tag.getString("Profession"));
                    profession = RibbitProfessionModule.NITWIT; // default to nitwit
                }
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

        this.homePosition = this.blockPosition();
        return data;
    }

    @Override
    public boolean removeWhenFarAway(double $$0) {
        return false;
    }

    @Override
    public InteractionResult mobInteract(Player player, InteractionHand interactionHand) {
        ItemStack itemStack = player.getItemInHand(interactionHand);

        if (player.isSecondaryUseActive() && itemStack.is(Items.AMETHYST_SHARD)) {
            this.homePosition = this.blockPosition();
            this.level().broadcastEntityEvent(this, (byte) 12);

            if (!player.getAbilities().instabuild) {
                itemStack.shrink(1);
            }
        } else if (this.isAlive() && !this.isTrading() && !this.isSleeping()) {
            if (this.isBaby()) {
                return InteractionResult.PASS;
            }
            boolean bl = this.getOffers().isEmpty();

            if (bl) {
                return InteractionResult.PASS;
            }

            if (!this.level().isClientSide && !this.offers.isEmpty()) {
                this.startTrading(player);
            }

            return InteractionResult.sidedSuccess(this.level().isClientSide);
        }
        return super.mobInteract(player, interactionHand);
    }

    public void reassessGoals() {
        if (this.level().isClientSide) {
            return;
        }

        this.goalSelector.removeGoal(this.musicGoal);
        this.goalSelector.removeGoal(this.waterCropsGoal);
        this.goalSelector.removeGoal(this.fishGoal);
        this.goalSelector.removeGoal(this.applyBuffGoal);

        if (this.getRibbitData().getProfession().equals(RibbitProfessionModule.NITWIT)) {
            this.goalSelector.addGoal(6, this.musicGoal);
        } else if (this.getRibbitData().getProfession().equals(RibbitProfessionModule.GARDENER)) {
            this.goalSelector.addGoal(6, this.waterCropsGoal);
        } else if (this.getRibbitData().getProfession().equals(RibbitProfessionModule.FISHERMAN)) {
            this.goalSelector.addGoal(6, this.fishGoal);
        } else if (this.getRibbitData().getProfession().equals(RibbitProfessionModule.SORCERER)) {
            this.goalSelector.addGoal(6, this.applyBuffGoal);
        }
    }

    @Override
    public float getSpeed() {
        return super.getSpeed();
    }

    @Override
    public void handleEntityEvent(byte flag) {
        if (flag == 12) {
            this.addParticlesAroundSelf(ParticleTypes.HEART);
        }

        super.handleEntityEvent(flag);
    }

    protected void addParticlesAroundSelf(ParticleOptions particleOptions) {
        for (int i = 0; i < 5; ++i) {
            double d = this.random.nextGaussian() * 0.02;
            double e = this.random.nextGaussian() * 0.02;
            double f = this.random.nextGaussian() * 0.02;
            this.level().addParticle(particleOptions, this.getRandomX(1.0), this.getRandomY() + 1.0, this.getRandomZ(1.0), d, e, f);
        }
    }

    @Override
    public int getMaxHeadXRot() {
        return 0;
    }

    @Override
    public int getMaxHeadYRot() {
        return 0;
    }

    @Nullable
    @Override
    public AgeableMob getBreedOffspring(ServerLevel level, AgeableMob parent) {
        return null;
    }

    public void setInstrument(RibbitInstrument instrument) {
        this.getRibbitData().setInstrument(instrument);
        this.entityData.set(RIBBIT_DATA, this.getRibbitData(), true);
    }

    public int getBuffCooldown() {
        return this.buffCooldown;
    }

    public void setBuffCooldown(int cooldown) {
        this.buffCooldown = cooldown;
    }

    public int getWaterCropsCooldown() {
        return this.waterCropsCooldown;
    }

    public void setWaterCropsCooldown(int cooldown) {
        this.waterCropsCooldown = cooldown;
    }

    public RibbitData getRibbitData() {
        return this.sidedRibbitData;
    }

    public void setRibbitData(RibbitData data) {
        this.entityData.set(RIBBIT_DATA, data);
    }

    public BlockPos getHomePosition() {
        return this.homePosition;
    }

    public boolean getPlayingInstrument() {
        return this.isPlayingInstrument;
    }

    public void setPlayingInstrument(boolean playingInstrument) {
        this.entityData.set(PLAYING_INSTRUMENT, playingInstrument);
    }

    public boolean getUmbrellaFalling() {
        return this.isUmbrellaFalling;
    }

    public void setUmbrellaFalling(boolean umbrellaFalling) {
        this.entityData.set(UMBRELLA_FALLING, umbrellaFalling);
    }

    public boolean getWatering() {
        return this.isWatering;
    }

    public void setWatering(boolean isWatering) {
        this.entityData.set(WATERING, isWatering);
    }

    public boolean getFishing() {
        return this.isFishing;
    }

    public void setFishing(boolean isFishing) {
        this.entityData.set(FISHING, isFishing);
    }

    public boolean getBuffing() {
        return this.isBuffing;
    }

    public void setBuffing(boolean isBuffing) {
        this.entityData.set(BUFFING, isBuffing);
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

    @Nullable
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
        RibbitEntity newMaster = this.getRibbitsPlayingMusic().stream().filter(ribbit -> ribbit != this).findAny().orElse(null);

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
        this.getPlayersHearingMusic().clear();
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
        if (this.isMasterRibbit()) {
            findNewMasterRibbit();
        } else if (this.isPlayingInstrument && this.getMasterRibbit() != null){
            this.getMasterRibbit().getRibbitsPlayingMusic().remove(this);
            this.getMasterRibbit().removeBandMember(this.getRibbitData().getInstrument());
        }

        super.remove(reason);
    }

    public static AttributeSupplier.Builder createRibbitAttributes() {
        return createMobAttributes()
                .add(Attributes.MAX_HEALTH, 15.0D)
                .add(Attributes.MOVEMENT_SPEED, 0.125D)
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

    public boolean isPrideRibbit() {
        Random rand = new Random(this.getUUID().getLeastSignificantBits());

        return isPrideMonth() && this.getRibbitData().getProfession().equals(RibbitProfessionModule.NITWIT) && rand.nextFloat() < 0.33f;
    }

    private static boolean isPrideMonth() {
        if (RibbitsCommon.CONFIG.prideFlagAllYear) return true;

        LocalDate date = LocalDate.now();
        int month = date.get(ChronoField.MONTH_OF_YEAR);
        return month == 6;
    }

    public boolean isInRain() {
        BlockPos pos = this.blockPosition();
        return this.level().isRainingAt(pos) || this.level().isRainingAt(BlockPos.containing(pos.getX(), this.getBoundingBox().maxY, pos.getZ()));
    }

    private <E extends GeoAnimatable> PlayState predicate(AnimationState<E> state) {
        if (this.getUmbrellaFalling()) {
            state.getController().setAnimation(this.getRibbitData().getProfession().equals(RibbitProfessionModule.FISHERMAN) || this.isPrideRibbit() ? IDLE_HOLDING_2 : IDLE_HOLDING_1);
        } else if (getPlayingInstrument() && this.getRibbitData().getInstrument() != RibbitInstrumentModule.NONE) {
            state.getController().setAnimation(RawAnimation.begin().thenPlay(this.getRibbitData().getInstrument().getAnimationName()));
        } else if (getBuffing()) {
            state.getController().setAnimation(this.isInRain() ? SORCERER_BUFF_HOLDING : SORCERER_BUFF);
        } else if (getFishing()) {
            state.getController().setAnimation(this.isInRain() ? FISH_HOLDING : FISH);
        } else if (getWatering()) {
            state.getController().setAnimation(this.isInRain() ? WATER_CROPS_HOLDING: WATER_CROPS);
        } else if (state.isMoving() && !this.isInWater()) {
            state.getController().setAnimation(this.getWalkAnimation());
          } else {
            state.getController().setAnimation(this.getIdleAnimation());
        }
        return PlayState.CONTINUE;
    }

    private RawAnimation getWalkAnimation() {
        if (this.getRibbitData().getProfession().equals(RibbitProfessionModule.FISHERMAN)) {
            return this.isInRain() ? WALK_HOLDING_FISHERMAN : WALK_HOLDING_2;
        } else if (this.isPrideRibbit()) {
            return WALK_HOLDING_2;
        } else if (this.getRibbitData().getProfession().equals(RibbitProfessionModule.SORCERER) || this.getRibbitData().getProfession().equals(RibbitProfessionModule.GARDENER)) {
            return this.isInRain() ? WALK_HOLDING_HAT : WALK;
        } else {
            return this.isInRain() ? WALK_HOLDING_1 : WALK;
        }
    }

    private RawAnimation getIdleAnimation() {
        if (this.getRibbitData().getProfession().equals(RibbitProfessionModule.FISHERMAN)) {
            return this.isInRain() ? IDLE_HOLDING_FISHERMAN : IDLE_HOLDING_2;
        } else if (this.isPrideRibbit()) {
            return IDLE_HOLDING_2;
        } else if (this.getRibbitData().getProfession().equals(RibbitProfessionModule.SORCERER) || this.getRibbitData().getProfession().equals(RibbitProfessionModule.GARDENER)) {
            return this.isInRain() ? IDLE_HOLDING_HAT : IDLE;
        } else {
            return this.isInRain() ? IDLE_HOLDING_1 : IDLE;
        }
    }

    @Override
    public void registerControllers(AnimatableManager.ControllerRegistrar controllerRegistrar) {
        controllerRegistrar.add(new AnimationController<>(this, "controller", 5, this::predicate));
    }

    @Override
    public AnimatableInstanceCache getAnimatableInstanceCache() {
        return this.cache;
    }

    @Nullable
    @Override
    public Player getTradingPlayer() {
        return this.tradingPlayer;
    }

    @Override
    public MerchantOffers getOffers() {
        if (this.offers == null) {
            this.offers = new MerchantOffers();
            RibbitTradeModule.updateTrades(this);
        }

        return this.offers;
    }

    @Override
    public void overrideOffers(MerchantOffers merchantOffers) {
    }

    @Override
    public void notifyTrade(MerchantOffer merchantOffer) {
        merchantOffer.increaseUses();
        this.ambientSoundTime = -this.getAmbientSoundInterval();
        this.rewardTradeXp(merchantOffer);
    }

    protected void rewardTradeXp(MerchantOffer merchantOffer) {
        int i = 3 + this.random.nextInt(4);

        if (merchantOffer.shouldRewardExp()) {
            this.level().addFreshEntity(new ExperienceOrb(this.level(), this.getX(), this.getY() + 0.5, this.getZ(), i));
        }
    }

    @Override
    public void notifyTradeUpdated(ItemStack itemStack) {
        if (!this.level().isClientSide && this.ambientSoundTime > -this.getAmbientSoundInterval() + 20) {
            this.ambientSoundTime = -this.getAmbientSoundInterval();
        }
    }

    private void startTrading(Player player) {
        this.setTradingPlayer(player);
        this.openTradingScreen(player, this.getDisplayName(), 0);
    }

    @Override
    public void setTradingPlayer(@Nullable Player player) {
        boolean bl = this.getTradingPlayer() != null && player == null;
        this.tradingPlayer = player;

        if (bl) {
            this.stopTrading();
        }
    }

    protected void stopTrading() {
        this.setTradingPlayer(null);
        this.resetSpecialPrices();
    }

    private void resetSpecialPrices() {
        for (MerchantOffer merchantOffer : this.getOffers()) {
            merchantOffer.resetSpecialPriceDiff();
        }
    }

    @Override
    public boolean canRestock() {
        return true;
    }

    public void restock() {
        this.updateDemand();
        for (MerchantOffer merchantOffer : this.getOffers()) {
            merchantOffer.resetUses();
        }
        this.resendOffersToTradingPlayer();
        this.lastRestockGameTime = this.level().getGameTime();
        ++this.numberOfRestocksToday;
    }

    private void resendOffersToTradingPlayer() {
        MerchantOffers merchantOffers = this.getOffers();
        Player player = this.getTradingPlayer();
        if (player != null && !merchantOffers.isEmpty()) {
            player.sendMerchantOffers(player.containerMenu.containerId, merchantOffers, 0, this.getVillagerXp(), this.showProgressBar(), this.canRestock());
        }
    }

    private boolean needsToRestock() {
        for (MerchantOffer merchantOffer : this.getOffers()) {
            if (!merchantOffer.needsRestock()) continue;
            return true;
        }
        return false;
    }

    private boolean allowedToRestock() {
        return this.numberOfRestocksToday == 0 || this.numberOfRestocksToday < 2 && this.level().getGameTime() > this.lastRestockGameTime + 2400L;
    }

    public boolean shouldRestock() {
        long l = this.lastRestockGameTime + 12000L;
        long m = this.level().getGameTime();
        boolean bl = m > l;
        long n = this.level().getDayTime();
        if (this.lastRestockCheckDayTime > 0L) {
            long p = n / 24000L;
            long o = this.lastRestockCheckDayTime / 24000L;
            bl |= p > o;
        }
        this.lastRestockCheckDayTime = n;
        if (bl) {
            this.lastRestockGameTime = m;
            this.resetNumberOfRestocks();
        }
        return this.allowedToRestock() && this.needsToRestock();
    }

    private void resetNumberOfRestocks() {
        this.catchUpDemand();
        this.numberOfRestocksToday = 0;
    }

    private void catchUpDemand() {
        int i = 2 - this.numberOfRestocksToday;
        if (i > 0) {
            for (MerchantOffer merchantOffer : this.getOffers()) {
                merchantOffer.resetUses();
            }
        }
        for (int j = 0; j < i; ++j) {
            this.updateDemand();
        }
        this.resendOffersToTradingPlayer();
    }

    private void updateDemand() {
        for (MerchantOffer merchantOffer : this.getOffers()) {
            merchantOffer.updateDemand();
        }
    }

    public boolean isTrading() {
        return this.tradingPlayer != null;
    }

    @Override
    public int getVillagerXp() {
        return 0;
    }

    @Override
    public void overrideXp(int i) {
    }

    @Override
    public boolean showProgressBar() {
        return false;
    }

    @Override
    public SoundEvent getNotifyTradeSound() {
        return null;
    }

    @Override
    public boolean isClientSide() {
        return this.level().isClientSide();
    }
}
