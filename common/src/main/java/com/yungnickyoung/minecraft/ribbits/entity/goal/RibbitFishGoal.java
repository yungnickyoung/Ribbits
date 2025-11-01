package com.yungnickyoung.minecraft.ribbits.entity.goal;

import com.yungnickyoung.minecraft.ribbits.entity.RibbitEntity;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.tags.FluidTags;
import net.minecraft.util.Mth;
import net.minecraft.world.entity.ai.goal.Goal;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.phys.Vec3;

import java.util.EnumSet;
import java.util.Optional;

public class RibbitFishGoal extends Goal {
    private static final float REQUIRED_DISTANCE_TO_DRY_POS = 0.2f;

    private final RibbitEntity ribbit;
    private final double range;
    private final int minRequiredFishTicks;
    private final int maxRequiredFishTicks;
    private final float speedModifier;

    private int requiredFishTicks;
    private int ticksFishing;
    private BlockPos waterPos;
    private Vec3 dryPos;

    public RibbitFishGoal(RibbitEntity ribbit, double range, float speedModifier, int minRequiredFishTicks, int maxRequiredFishTicks) {
        this.ribbit = ribbit;
        this.range = range;
        this.speedModifier = speedModifier;
        this.minRequiredFishTicks = minRequiredFishTicks;
        this.maxRequiredFishTicks = maxRequiredFishTicks;

        this.setFlags(EnumSet.of(Flag.MOVE, Flag.LOOK));
    }

    @Override
    public void start() {
        this.requiredFishTicks = this.ribbit.getRandom().nextInt(this.minRequiredFishTicks, this.maxRequiredFishTicks);
        this.ticksFishing = 0;

        float waterModifier = this.ribbit.isInWater() ? RibbitEntity.WATER_SPEED_MULTIPLIER : 1.0f;
        this.ribbit.getNavigation().moveTo(this.dryPos.x(), this.dryPos.y(), this.dryPos.z(), this.speedModifier * waterModifier);
    }

    @Override
    public void stop() {
        this.waterPos = null;
        this.dryPos = null;
        this.ticksFishing = 0;

        this.ribbit.setFishing(false);
    }

    @Override
    public boolean isInterruptable() {
        return this.ticksFishing >= this.requiredFishTicks;
    }

    @Override
    public boolean canUse() {

        if (this.ribbit.level().isDarkOutside()) {
            return false;
        }

        Optional<BlockPos> waterPos = BlockPos.findClosestMatch(this.ribbit.getOnPos(), (int) range, 5, blockpos -> this.ribbit.level().getFluidState(blockpos).is(FluidTags.WATER) && this.ribbit.level().getBlockState(blockpos.above()).is(Blocks.AIR));

        this.dryPos = null;

        if (waterPos.isPresent()) {
            this.waterPos = waterPos.get();

            for (Direction dir : Direction.Plane.HORIZONTAL.shuffledCopy(this.ribbit.getRandom())) {
                BlockPos testedDryPos = this.waterPos.relative(dir);

                if (this.ribbit.level().getBlockState(testedDryPos.above()).is(Blocks.AIR) && Block.isFaceFull(this.ribbit.level().getBlockState(testedDryPos).getCollisionShape(this.ribbit.level(), testedDryPos), Direction.UP)) {
                    // Center of the surface of the adjacent dry block
                    Vec3 dryPosCenter = new Vec3(testedDryPos.getX() + 0.5, testedDryPos.getY() + 1.0, testedDryPos.getZ() + 0.5);

                    // Get a point on the edge of the dry block closest to the water, to ensure the ribbit's fishing line is in the water
                    this.dryPos = dryPosCenter.add(-dir.getStepX() * 0.25, 0, -dir.getStepZ() * 0.25);
                    break;
                }
            }
        } else {
            return false;
        }

        if (this.dryPos == null) {
            return false;
        }

        return this.ribbit.level().isBrightOutside();
    }

    @Override
    public boolean requiresUpdateEveryTick() {
        return true;
    }

    @Override
    public boolean canContinueToUse() {
        Iterable<BlockPos> nearbyPositions = BlockPos.betweenClosed(Mth.floor(this.ribbit.getX() - 1.5), Mth.floor(this.ribbit.getY() - 1.5), Mth.floor(this.ribbit.getZ() - 1.5), Mth.floor(this.ribbit.getX() + 1.5), this.ribbit.getBlockY(), Mth.floor(this.ribbit.getZ() + 1.5));

        boolean waterNearby = false;
        for (BlockPos nearbyPos : nearbyPositions) {
            if (this.ribbit.level().getFluidState(nearbyPos).is(FluidTags.WATER)) {
                waterNearby = true;
                break;
            }
        }

        return this.ribbit.distanceToSqr(this.dryPos) >= REQUIRED_DISTANCE_TO_DRY_POS * REQUIRED_DISTANCE_TO_DRY_POS || waterNearby;
    }

    @Override
    public void tick() {
        float waterModifier = this.ribbit.isInWater() ? RibbitEntity.WATER_SPEED_MULTIPLIER : 1.0f;
        this.ribbit.getNavigation().setSpeedModifier(this.speedModifier * waterModifier);

        if (this.ribbit.distanceToSqr(this.dryPos) <= REQUIRED_DISTANCE_TO_DRY_POS * REQUIRED_DISTANCE_TO_DRY_POS) {
            this.ticksFishing++;
            this.ribbit.setFishing(true);
            this.ribbit.getLookControl().setLookAt(this.waterPos.getX() + 0.5f, this.ribbit.getEyeY(), this.waterPos.getZ() + 0.5f);
        } else if (this.ribbit.distanceToSqr(this.dryPos) < 1.0f) {
            this.ribbit.setFishing(false);
            this.ribbit.getMoveControl().setWantedPosition(this.dryPos.x(), this.dryPos.y(), this.dryPos.z(), this.speedModifier * waterModifier);
        } else {
            this.ribbit.setFishing(false);
            this.ribbit.getNavigation().moveTo(this.dryPos.x(), this.dryPos.y(), this.dryPos.z(), this.speedModifier * waterModifier);
        }
    }
}
