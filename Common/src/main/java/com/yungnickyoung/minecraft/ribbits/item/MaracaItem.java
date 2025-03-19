package com.yungnickyoung.minecraft.ribbits.item;

import com.yungnickyoung.minecraft.ribbits.player.PlayerInstrumentTracker;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResultHolder;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.UseAnim;
import net.minecraft.world.level.Level;

public class MaracaItem extends Item {
    public MaracaItem(Properties properties) {
        super(properties);
    }

    @Override
    public InteractionResultHolder<ItemStack> use(Level level, Player player, InteractionHand hand) {
        ItemStack itemInHand = player.getItemInHand(hand);
        player.startUsingItem(hand);
        if (!level.isClientSide()) {
            PlayerInstrumentTracker.addPerformer(player);
        }
        return InteractionResultHolder.consume(itemInHand);
    }

    @Override
    public void releaseUsing(ItemStack itemStack, Level level, LivingEntity livingEntity, int $$3) {
        if (!level.isClientSide() && livingEntity instanceof Player player) {
            PlayerInstrumentTracker.removePerformer(player);
        }
    }

    @Override
    public int getUseDuration(ItemStack $$0, LivingEntity $$1) {
        return 72000;
    }

    @Override
    public UseAnim getUseAnimation(ItemStack $$0) {
        return UseAnim.BRUSH;
    }
}
