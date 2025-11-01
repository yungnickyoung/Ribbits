package com.yungnickyoung.minecraft.ribbits.entity.trade;

import net.minecraft.util.RandomSource;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.trading.ItemCost;
import net.minecraft.world.item.trading.MerchantOffer;
import net.minecraft.world.level.ItemLike;
import org.jetbrains.annotations.Nullable;

import java.util.Optional;

public class ItemsAndAmethystsToItems implements ItemListing {
    private final ItemStack itemCost;
    private final int itemCostCount;
    private final int amethystCostCount;
    private final ItemStack result;
    private final int resultCount;
    private final int maxUses;
    private final float priceMultiplier;

    public ItemsAndAmethystsToItems(ItemLike itemCost, int itemCostCount, Item result, int resultCount, int maxUses) {
        this(itemCost, itemCostCount, 1, result, resultCount, maxUses);
    }

    public ItemsAndAmethystsToItems(ItemLike itemCost, int itemCostCount, int cost, Item result, int resultCount, int maxUses) {
        this.itemCost = new ItemStack(itemCost);
        this.itemCostCount = itemCostCount;
        this.amethystCostCount = cost;
        this.result = new ItemStack(result);
        this.resultCount = resultCount;
        this.maxUses = maxUses;
        this.priceMultiplier = 0.05F;
    }

    @Nullable
    public MerchantOffer getOffer(Entity entity, RandomSource rand) {
        ItemCost itemCost = new ItemCost(this.itemCost.getItem(), this.itemCostCount);
        ItemCost amethystCost = new ItemCost(Items.AMETHYST_SHARD, this.amethystCostCount);
        ItemStack result = new ItemStack(this.result.getItem(), this.resultCount);
        return new MerchantOffer(itemCost, Optional.of(amethystCost), result, this.maxUses, 0, this.priceMultiplier);
    }
}
