package com.yungnickyoung.minecraft.ribbits.item;

import com.yungnickyoung.minecraft.ribbits.RibbitsCommon;
import com.yungnickyoung.minecraft.ribbits.data.RibbitData;
import com.yungnickyoung.minecraft.ribbits.entity.RibbitEntity;
import com.yungnickyoung.minecraft.ribbits.module.RibbitUmbrellaTypeModule;
import net.minecraft.core.Direction;
import net.minecraft.core.dispenser.BlockSource;
import net.minecraft.core.dispenser.DispenseItemBehavior;
import net.minecraft.world.entity.EntitySpawnReason;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.block.DispenserBlock;
import net.minecraft.world.level.gameevent.GameEvent;
import org.jetbrains.annotations.NotNull;

public class RibbitSpawnEggDispenseItemBehavior implements DispenseItemBehavior {

    @Override
    public @NotNull ItemStack dispense(BlockSource source, ItemStack stack) {
        Direction dir = source.state().getValue(DispenserBlock.FACING);
        RibbitSpawnEggItem item = (RibbitSpawnEggItem) stack.getItem();
        EntityType<?> type = item.getType(stack);

        RibbitEntity ribbit;
        try {
            ribbit = (RibbitEntity) type.spawn(source.level(), stack, null,
                    source.pos().relative(dir), EntitySpawnReason.DISPENSER,
                    dir != Direction.UP, false);
        } catch (Exception e) {
            RibbitsCommon.LOGGER.error("Error dispensing Ribbit spawn egg at {}", source.pos(), e);
            return ItemStack.EMPTY;
        }

        if (ribbit != null) {
            ribbit.setRibbitData(new RibbitData(
                    item.getProfession(),
                    RibbitUmbrellaTypeModule.getRandomUmbrellaType(),
                    ribbit.getRibbitData().getInstrument()));
        }

        stack.shrink(1);
        source.level().gameEvent(null, GameEvent.ENTITY_PLACE, source.pos());
        return stack;
    }
}