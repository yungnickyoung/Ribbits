package com.yungnickyoung.minecraft.ribbits.item;

import com.yungnickyoung.minecraft.ribbits.data.RibbitProfession;
import com.yungnickyoung.minecraft.ribbits.entity.RibbitEntity;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.core.dispenser.DispenseItemBehavior;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.stats.Stats;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.InteractionResultHolder;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.MobSpawnType;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.SpawnEggItem;
import net.minecraft.world.item.context.UseOnContext;
import net.minecraft.world.level.ClipContext;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.LiquidBlock;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.SpawnerBlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.gameevent.GameEvent;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.HitResult;
import net.minecraftforge.common.ForgeSpawnEggItem;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.Objects;

public class RibbitSpawnEggItemForge extends ForgeSpawnEggItem {
    private static final DispenseItemBehavior RIBBIT_DISPENSER_BEHAVIOR = new RibbitSpawnEggDispenseItemBehaviorForge();
    private final RibbitProfession profession;

    public RibbitSpawnEggItemForge(EntityType<RibbitEntity> entityType, RibbitProfession profession, int backgroundColor, int highlightColor, Properties properties) {
        super(() -> entityType, backgroundColor, highlightColor, properties);
        this.profession = profession;
    }

    @Override
    public @NotNull InteractionResult useOn(UseOnContext useOnContext) {
        BlockEntity blockEntity;
        Level level = useOnContext.getLevel();
        if (!(level instanceof ServerLevel)) {
            return InteractionResult.SUCCESS;
        }
        ItemStack itemStack = useOnContext.getItemInHand();
        BlockPos blockPos = useOnContext.getClickedPos();
        Direction direction = useOnContext.getClickedFace();
        BlockState blockState = level.getBlockState(blockPos);
        if (blockState.is(Blocks.SPAWNER) && (blockEntity = level.getBlockEntity(blockPos)) instanceof SpawnerBlockEntity) {
            SpawnerBlockEntity spawnerBlockEntity = (SpawnerBlockEntity) blockEntity;
            EntityType<?> entityType = this.getType(itemStack.getTag());
            spawnerBlockEntity.setEntityId(entityType, level.getRandom());
            blockEntity.setChanged();
            level.sendBlockUpdated(blockPos, blockState, blockState, 3);
            level.gameEvent(useOnContext.getPlayer(), GameEvent.BLOCK_CHANGE, blockPos);
            itemStack.shrink(1);
            return InteractionResult.CONSUME;
        }
        BlockPos blockPos2 = blockState.getCollisionShape(level, blockPos).isEmpty() ? blockPos : blockPos.relative(direction);
        EntityType<?> entityType2 = this.getType(itemStack.getTag());

        CompoundTag itemTag = itemStack.getOrCreateTag();

        itemTag.putString("Profession", this.profession.toString());

        RibbitEntity ribbit = (RibbitEntity) entityType2.spawn((ServerLevel) level, itemStack, useOnContext.getPlayer(), blockPos2, MobSpawnType.SPAWN_EGG, true, !Objects.equals(blockPos, blockPos2) && direction == Direction.UP);
        if (ribbit != null) {

            itemStack.shrink(1);
            level.gameEvent(useOnContext.getPlayer(), GameEvent.ENTITY_PLACE, blockPos);
        }
        return InteractionResult.CONSUME;
    }

    @Override
    public @NotNull InteractionResultHolder<ItemStack> use(@NotNull Level level, Player player, @NotNull InteractionHand interactionHand) {
        ItemStack itemStack = player.getItemInHand(interactionHand);
        BlockHitResult blockHitResult = SpawnEggItem.getPlayerPOVHitResult(level, player, ClipContext.Fluid.SOURCE_ONLY);
        if (blockHitResult.getType() != HitResult.Type.BLOCK) {
            return InteractionResultHolder.pass(itemStack);
        }
        if (!(level instanceof ServerLevel)) {
            return InteractionResultHolder.success(itemStack);
        }
        BlockPos blockPos = blockHitResult.getBlockPos();
        if (!(level.getBlockState(blockPos).getBlock() instanceof LiquidBlock)) {
            return InteractionResultHolder.pass(itemStack);
        }
        if (!level.mayInteract(player, blockPos) || !player.mayUseItemAt(blockPos, blockHitResult.getDirection(), itemStack)) {
            return InteractionResultHolder.fail(itemStack);
        }
        EntityType<?> entityType = this.getType(itemStack.getTag());

        CompoundTag itemTag = itemStack.getOrCreateTag();

        itemTag.putString("Profession", this.profession.toString());

        RibbitEntity ribbit = (RibbitEntity) entityType.spawn((ServerLevel) level, itemStack, player, blockPos, MobSpawnType.SPAWN_EGG, false, false);
        if (ribbit == null) {
            return InteractionResultHolder.pass(itemStack);
        }

        if (!player.getAbilities().instabuild) {
            itemStack.shrink(1);
        }
        player.awardStat(Stats.ITEM_USED.get(this));
        level.gameEvent(player, GameEvent.ENTITY_PLACE, ribbit.position());
        return InteractionResultHolder.consume(itemStack);
    }

    @Override
    protected @Nullable DispenseItemBehavior createDispenseBehavior() {
        return RIBBIT_DISPENSER_BEHAVIOR;
    }

    public RibbitProfession getProfession() {
        return profession;
    }
}
