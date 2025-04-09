package com.yungnickyoung.minecraft.ribbits.entity.trade;

import net.minecraft.util.RandomSource;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.enchantment.EnchantmentHelper;
import net.minecraft.world.item.trading.ItemCost;
import net.minecraft.world.item.trading.MerchantOffer;

import java.util.Optional;

public class EnchantedItemForAmethyst implements ItemListing {
    private final ItemStack result;
    private final int costCountMin;
    private final int costCountMax;
    private final int maxUses;
    private final float priceMultiplier;

    public EnchantedItemForAmethyst(Item resultItem, int costCountMin, int costCountMax, int maxUses) {
        this(resultItem, costCountMin, costCountMax, maxUses, 0.05F);
    }

    public EnchantedItemForAmethyst(Item resultItem, int costCountMin, int costCountMax, int maxUses, float priceMultiplier) {
        this.result = new ItemStack(resultItem);
        this.costCountMin = costCountMin;
        this.costCountMax = costCountMax;
        this.maxUses = maxUses;
        this.priceMultiplier = priceMultiplier;
    }

    public MerchantOffer getOffer(Entity entity, RandomSource rand) {
        int randomEnchantCost = 5 + rand.nextInt(15);
        ItemCost cost = new ItemCost(Items.AMETHYST_SHARD, rand.nextIntBetweenInclusive(this.costCountMin, this.costCountMax));
        ItemStack result = EnchantmentHelper.enchantItem(rand, new ItemStack(this.result.getItem()), randomEnchantCost, entity.registryAccess(), Optional.empty());
        return new MerchantOffer(cost, result, this.maxUses, 0, this.priceMultiplier);
    }
}
