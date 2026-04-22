package com.yungnickyoung.minecraft.ribbits.client.render;

import com.mojang.serialization.MapCodec;
import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.client.renderer.item.properties.conditional.ConditionalItemModelProperty;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.ItemDisplayContext;
import net.minecraft.world.item.ItemStack;
import org.jetbrains.annotations.Nullable;

/**
 * Custom conditional item model property for Maraca that checks if the item is being used
 * in first-person context.
 */
public record MaracaUsingProperty() implements ConditionalItemModelProperty {
    public static final MapCodec<MaracaUsingProperty> MAP_CODEC = MapCodec.unit(new MaracaUsingProperty());

    @Override
    public boolean get(ItemStack itemStack, @Nullable ClientLevel clientLevel, @Nullable LivingEntity livingEntity, int seed, ItemDisplayContext ctx) {
        return ctx != ItemDisplayContext.GUI
                && ctx != ItemDisplayContext.GROUND
                && ctx != ItemDisplayContext.FIXED;
    }

    @Override
    public MapCodec<MaracaUsingProperty> type() {
        return MAP_CODEC;
    }
}

