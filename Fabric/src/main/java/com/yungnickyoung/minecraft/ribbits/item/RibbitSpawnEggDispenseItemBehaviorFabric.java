package com.yungnickyoung.minecraft.ribbits.item;

import com.yungnickyoung.minecraft.ribbits.RibbitsCommon;
import com.yungnickyoung.minecraft.ribbits.data.RibbitProfession;
import net.minecraft.core.dispenser.BlockSource;
import net.minecraft.core.Direction;
import net.minecraft.core.dispenser.DispenseItemBehavior;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.MobSpawnType;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.block.DispenserBlock;
import net.minecraft.world.level.gameevent.GameEvent;
import org.jetbrains.annotations.NotNull;

public class RibbitSpawnEggDispenseItemBehaviorFabric implements DispenseItemBehavior {
    @Override
    public @NotNull ItemStack dispense(BlockSource blockSource, ItemStack itemStack) {
        Direction direction = blockSource.state().getValue(DispenserBlock.FACING);

        // CUSTOM BEHAVIOR - attach profession to item stack used when spawning the entity
        RibbitSpawnEggItemFabric ribbitSpawnEggItem = (RibbitSpawnEggItemFabric) itemStack.getItem();
        RibbitProfession profession = ribbitSpawnEggItem.getProfession();
        itemStack.getOrCreateTag().putString("Profession", profession.toString());

        EntityType<?> entityType = ribbitSpawnEggItem.getType(itemStack.getTag());

        try {
            entityType.spawn(blockSource.level(), itemStack, null,
                    blockSource.pos().relative(direction), MobSpawnType.DISPENSER, direction != Direction.UP, false);
        } catch (Exception e) {
            RibbitsCommon.LOGGER.error("Error while dispensing Ribbit spawn egg from dispenser at {}", blockSource.pos(), e);
            return ItemStack.EMPTY;
        }

        itemStack.shrink(1);
        blockSource.level().gameEvent(null, GameEvent.ENTITY_PLACE, blockSource.pos());
        return itemStack;
    }
}
