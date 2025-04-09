package com.yungnickyoung.minecraft.ribbits.entity.trade;

import net.minecraft.util.RandomSource;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.trading.ItemCost;
import net.minecraft.world.item.trading.MerchantOffer;
import net.minecraft.world.level.ItemLike;

public class AmethystForItems implements ItemListing {
    private final Item item;
    private final int costCountMin;
    private final int costCountMax;
    private final int maxUses;
    private final float priceMultiplier;
    private final int resultCountMin;
    private final int resultCountMax;

    public AmethystForItems(ItemLike item, int costCountMin, int costCountMax, int resultCountMin, int resultCountMax, int maxUses) {
        this.item = item.asItem();
        this.costCountMin = costCountMin;
        this.costCountMax = costCountMax;
        this.resultCountMin = resultCountMin;
        this.resultCountMax = resultCountMax;
        this.maxUses = maxUses;
        this.priceMultiplier = 0.05F;
    }

    public MerchantOffer getOffer(Entity entity, RandomSource rand) {
        ItemCost cost = new ItemCost(this.item, rand.nextIntBetweenInclusive(this.costCountMin, this.costCountMax));
        ItemStack result = new ItemStack(Items.AMETHYST_SHARD, rand.nextIntBetweenInclusive(this.resultCountMin, this.resultCountMax));
        return new MerchantOffer(cost, result, this.maxUses, 0, this.priceMultiplier);
    }
}
