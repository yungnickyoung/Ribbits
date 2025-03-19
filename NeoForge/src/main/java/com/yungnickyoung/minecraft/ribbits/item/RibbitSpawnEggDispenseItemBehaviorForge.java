package com.yungnickyoung.minecraft.ribbits.item;

import com.yungnickyoung.minecraft.ribbits.RibbitsCommon;
import com.yungnickyoung.minecraft.ribbits.data.RibbitProfession;
import net.minecraft.core.BlockSource;
import net.minecraft.core.Direction;
import net.minecraft.core.dispenser.DispenseItemBehavior;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.MobSpawnType;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.block.DispenserBlock;
import net.minecraft.world.level.gameevent.GameEvent;
import org.jetbrains.annotations.NotNull;

public class RibbitSpawnEggDispenseItemBehaviorForge implements DispenseItemBehavior {
    @Override
    public @NotNull ItemStack dispense(BlockSource blockSource, ItemStack itemStack) {
        Direction direction = blockSource.getBlockState().getValue(DispenserBlock.FACING);

        // CUSTOM BEHAVIOR - attach profession to item stack used when spawning the entity
        RibbitSpawnEggItemForge ribbitSpawnEggItem = (RibbitSpawnEggItemForge) itemStack.getItem();
        RibbitProfession profession = ribbitSpawnEggItem.getProfession();
        itemStack.getOrCreateTag().putString("Profession", profession.toString());

        EntityType<?> entityType = ribbitSpawnEggItem.getType(itemStack.getTag());

        try {
            entityType.spawn(blockSource.getLevel(), itemStack, null,
                    blockSource.getPos().relative(direction), MobSpawnType.DISPENSER, direction != Direction.UP, false);
        } catch (Exception e) {
            RibbitsCommon.LOGGER.error("Error while dispensing Ribbit spawn egg from dispenser at {}", blockSource.getPos(), e);
            return ItemStack.EMPTY;
        }

        itemStack.shrink(1);
        blockSource.getLevel().gameEvent(null, GameEvent.ENTITY_PLACE, blockSource.getPos());
        return itemStack;
    }
}
