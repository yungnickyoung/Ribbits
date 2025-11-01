package com.yungnickyoung.minecraft.ribbits.module;

import com.google.common.collect.Maps;
import com.google.common.collect.Sets;
import com.yungnickyoung.minecraft.ribbits.data.RibbitProfession;
import com.yungnickyoung.minecraft.ribbits.entity.RibbitEntity;
import com.yungnickyoung.minecraft.ribbits.entity.trade.AmethystForItems;
import com.yungnickyoung.minecraft.ribbits.entity.trade.EnchantedItemForAmethyst;
import com.yungnickyoung.minecraft.ribbits.entity.trade.ItemListing;
import com.yungnickyoung.minecraft.ribbits.entity.trade.ItemsForAmethysts;
import net.minecraft.Util;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.trading.MerchantOffer;

import java.util.HashSet;
import java.util.Map;

public class RibbitTradeModule {
    public static final Map<RibbitProfession, ItemListing[]> TRADES_BY_PROFESSION = Util.make(Maps.newHashMap(), (map) -> {
        map.put(RibbitProfessionModule.MERCHANT, new ItemListing[]{
                new ItemsForAmethysts(BlockModule.BROWN_TOADSTOOL.get().asItem(), 1, 1, 8, 16, 32),
                new ItemsForAmethysts(BlockModule.RED_TOADSTOOL.get().asItem(), 1, 1, 8, 16, 32),
                new ItemsForAmethysts(BlockModule.TOADSTOOL_STEM.get().asItem(), 1, 1, 8, 16, 32),
                new ItemsForAmethysts(BlockModule.MOSSY_OAK_PLANKS.get().asItem(), 1, 2, 16, 32, 32),
                new ItemsForAmethysts(BlockModule.SWAMP_LANTERN.get().asItem(), 2, 3, 4, 8, 32),
                new ItemsForAmethysts(ItemModule.MARACA.get(), 6, 8, 1, 1, 4)
        });

        map.put(RibbitProfessionModule.FISHERMAN, new ItemListing[]{
                new ItemsForAmethysts(Items.AXOLOTL_BUCKET, 4, 8, 1, 1, 16),
                new ItemsForAmethysts(Items.TROPICAL_FISH_BUCKET, 4, 8, 1, 1, 16),
                new ItemsForAmethysts(Items.COOKED_COD, 4, 8, 8, 24, 16),
                new ItemsForAmethysts(Items.COOKED_SALMON, 4, 8, 8, 24, 16),
                new EnchantedItemForAmethyst(Items.FISHING_ROD, 12, 16, 4),
                new AmethystForItems(Items.COD, 16, 32, 4, 8, 16),
                new AmethystForItems(Items.SALMON, 16, 32, 4, 8, 16)
        });
    });

    /**
     * Updates the trades for a RibbitEntity.
     *
     * @param ribbit The RibbitEntity to update the trades for.
     */
    public static void updateTrades(RibbitEntity ribbit) {
        ItemListing[] itemListings = TRADES_BY_PROFESSION.get(ribbit.getRibbitData().getProfession());
        if (itemListings == null || itemListings.length == 0) {
            return;
        }

        int numOffers = ribbit.getRibbitData().getProfession() == RibbitProfessionModule.MERCHANT ? 10 : 4;

        // Randomly select a subset of the available offers to populate the merchant's offers
        HashSet<Integer> chosenIndices = Sets.newHashSet();
        if (itemListings.length > numOffers) {
            while (chosenIndices.size() < numOffers) {
                chosenIndices.add(ribbit.getRandom().nextInt(itemListings.length));
            }
        } else {
            for (int i = 0; i < itemListings.length; ++i) {
                chosenIndices.add(i);
            }
        }

        // Populate the merchant offers
        for (Integer index : chosenIndices) {
            ItemListing itemListing = itemListings[index];
            MerchantOffer merchantOffer = itemListing.getOffer(ribbit, ribbit.getRandom());
            if (merchantOffer == null) continue;
            ribbit.getOffers().add(merchantOffer);
        }
    }
}
