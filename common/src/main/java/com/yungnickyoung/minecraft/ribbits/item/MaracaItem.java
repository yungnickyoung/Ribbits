package com.yungnickyoung.minecraft.ribbits.item;

import com.yungnickyoung.minecraft.ribbits.player.PlayerInstrumentTracker;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.ItemUseAnimation;
import net.minecraft.world.level.Level;

public class MaracaItem extends Item {
    public MaracaItem(Properties properties) {
        super(properties);
    }

    @Override
    public InteractionResult use(Level level, Player player, InteractionHand hand) {
        player.startUsingItem(hand);
        if (!level.isClientSide()) {
            PlayerInstrumentTracker.addPerformer(player);
        }
        return InteractionResult.CONSUME;
    }

    @Override
    public boolean releaseUsing(ItemStack itemStack, Level level, LivingEntity entity, int timeCharged) {
        if (!level.isClientSide() && entity instanceof Player player) {
            PlayerInstrumentTracker.removePerformer(player);
        }
        return false;
    }

    @Override
    public int getUseDuration(ItemStack $$0, LivingEntity $$1) {
        return 72000;
    }

    @Override
    public ItemUseAnimation getUseAnimation(ItemStack $$0) {
        return ItemUseAnimation.BRUSH;
    }
}
