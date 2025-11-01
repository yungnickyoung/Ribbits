package com.yungnickyoung.minecraft.ribbits.item;

import com.yungnickyoung.minecraft.ribbits.data.RibbitData;
import com.yungnickyoung.minecraft.ribbits.data.RibbitProfession;
import com.yungnickyoung.minecraft.ribbits.entity.RibbitEntity;
import com.yungnickyoung.minecraft.ribbits.module.EntityTypeModule;
import com.yungnickyoung.minecraft.ribbits.module.RibbitUmbrellaTypeModule;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.stats.Stats;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.EntitySpawnReason;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
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
import org.jetbrains.annotations.Nullable;

import java.util.Objects;

public class RibbitSpawnEggItem extends SpawnEggItem {
    private final RibbitProfession profession;

    public RibbitSpawnEggItem(RibbitProfession profession, Item.Properties properties) {
        super(properties);
        this.profession = profession;
    }

    @Override
    public @Nullable EntityType<?> getType(ItemStack itemStack) {
        return EntityTypeModule.RIBBIT.get();
    }

    @Override
    public InteractionResult useOn(UseOnContext ctx) {
        Level level = ctx.getLevel();
        if (!(level instanceof ServerLevel)) return InteractionResult.SUCCESS;

        ItemStack stack = ctx.getItemInHand();
        BlockPos clicked = ctx.getClickedPos();
        Direction face = ctx.getClickedFace();
        BlockState state = level.getBlockState(clicked);
        EntityType<?> type = this.getType(stack);

        if (state.is(Blocks.SPAWNER)) {
            BlockEntity be = level.getBlockEntity(clicked);
            if (be instanceof SpawnerBlockEntity spawner) {
                spawner.setEntityId(type, level.getRandom());
                be.setChanged();
                level.sendBlockUpdated(clicked, state, state, 3);
                level.gameEvent(ctx.getPlayer(), GameEvent.BLOCK_CHANGE, clicked);
                stack.shrink(1);
                return InteractionResult.CONSUME;
            }
        }

        BlockPos spawnPos = state.getCollisionShape(level, clicked).isEmpty() ? clicked : clicked.relative(face);
        RibbitEntity ribbit = (RibbitEntity) type.spawn((ServerLevel) level, stack, ctx.getPlayer(),
                spawnPos, EntitySpawnReason.SPAWN_ITEM_USE, true,
                !Objects.equals(clicked, spawnPos) && face == Direction.UP);

        if (ribbit != null) {
            ribbit.setRibbitData(new RibbitData(
                    this.profession,
                    RibbitUmbrellaTypeModule.getRandomUmbrellaType(),
                    ribbit.getRibbitData().getInstrument()));
            stack.shrink(1);
            level.gameEvent(ctx.getPlayer(), GameEvent.ENTITY_PLACE, clicked);
        }

        return InteractionResult.CONSUME;
    }

    @Override
    public InteractionResult use(Level level, Player player, InteractionHand hand) {
        ItemStack stack = player.getItemInHand(hand);
        BlockHitResult hit = SpawnEggItem.getPlayerPOVHitResult(level, player, ClipContext.Fluid.SOURCE_ONLY);

        if (hit.getType() != HitResult.Type.BLOCK) return InteractionResult.PASS;
        if (!(level instanceof ServerLevel)) return InteractionResult.SUCCESS;

        BlockPos pos = hit.getBlockPos();
        if (!(level.getBlockState(pos).getBlock() instanceof LiquidBlock)) return InteractionResult.PASS;
        if (!level.mayInteract(player, pos) || !player.mayUseItemAt(pos, hit.getDirection(), stack))
            return InteractionResult.FAIL;

        EntityType<?> type = this.getType(stack);
        RibbitEntity ribbit = (RibbitEntity) type.spawn((ServerLevel) level, stack, player,
                pos, EntitySpawnReason.SPAWN_ITEM_USE, false, false);
        if (ribbit == null) return InteractionResult.PASS;

        ribbit.setRibbitData(new RibbitData(
                this.profession,
                RibbitUmbrellaTypeModule.getRandomUmbrellaType(),
                ribbit.getRibbitData().getInstrument()));

        if (!player.getAbilities().instabuild) stack.shrink(1);
        player.awardStat(Stats.ITEM_USED.get(this));
        level.gameEvent(player, GameEvent.ENTITY_PLACE, ribbit.position());
        return InteractionResult.CONSUME;
    }

    public RibbitProfession getProfession() {
        return profession;
    }
}