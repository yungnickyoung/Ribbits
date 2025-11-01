package com.yungnickyoung.minecraft.ribbits.module;

import com.yungnickyoung.minecraft.ribbits.RibbitsCommon;
import com.yungnickyoung.minecraft.ribbits.item.MaracaItem;
import com.yungnickyoung.minecraft.ribbits.item.RibbitSpawnEggDispenseItemBehavior;
import com.yungnickyoung.minecraft.ribbits.item.RibbitSpawnEggItem;
import com.yungnickyoung.minecraft.ribbits.util.RegisterHelper;
import dev.architectury.registry.registries.DeferredRegister;
import dev.architectury.registry.registries.RegistrySupplier;
import net.minecraft.core.dispenser.DispenseItemBehavior;
import net.minecraft.core.registries.Registries;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.PlaceOnWaterBlockItem;
import net.minecraft.world.level.block.ComposterBlock;
import net.minecraft.world.level.block.DispenserBlock;

public class ItemModule {
    public static final DeferredRegister<Item> ITEMS = DeferredRegister.create(RibbitsCommon.MOD_ID, Registries.ITEM);

    public static final RegistrySupplier<BlockItem> BROWN_TOADSTOOL_ITEM = RegisterHelper.registerBlockItem(ITEMS, "brown_toadstool", BlockModule.BROWN_TOADSTOOL);
    public static final RegistrySupplier<BlockItem> RED_TOADSTOOL_ITEM = RegisterHelper.registerBlockItem(ITEMS, "red_toadstool", BlockModule.RED_TOADSTOOL);
    public static final RegistrySupplier<BlockItem> TOADSTOOL_STEM_ITEM = RegisterHelper.registerBlockItem(ITEMS, "toadstool_stem", BlockModule.TOADSTOOL_STEM);
    public static final RegistrySupplier<BlockItem> SWAMP_LANTERN_ITEM = RegisterHelper.registerBlockItem(ITEMS, "swamp_lantern", BlockModule.SWAMP_LANTERN);
    public static final RegistrySupplier<BlockItem> SWAMP_DAISY_ITEM = RegisterHelper.registerBlockItem(ITEMS, "swamp_daisy", BlockModule.SWAMP_DAISY);
    public static final RegistrySupplier<BlockItem> TOADSTOOL_ITEM = RegisterHelper.registerBlockItem(ITEMS, "toadstool", BlockModule.TOADSTOOL);
    public static final RegistrySupplier<BlockItem> UMBRELLA_LEAF_ITEM = RegisterHelper.registerBlockItem(ITEMS, "umbrella_leaf", BlockModule.UMBRELLA_LEAF);
    public static final RegistrySupplier<BlockItem> MOSSY_OAK_PLANKS_ITEM = RegisterHelper.registerBlockItem(ITEMS, "mossy_oak_planks", BlockModule.MOSSY_OAK_PLANKS::get);
    public static final RegistrySupplier<BlockItem> MOSSY_OAK_SLAB_ITEM = RegisterHelper.registerBlockItem(ITEMS, "mossy_oak_planks_slab", BlockModule.MOSSY_OAK_PLANKS::getSlab);
    public static final RegistrySupplier<BlockItem> MOSSY_OAK_STAIRS_ITEM = RegisterHelper.registerBlockItem(ITEMS, "mossy_oak_planks_stairs", BlockModule.MOSSY_OAK_PLANKS::getStairs);
    public static final RegistrySupplier<Item> MOSSY_OAK_FENCE_ITEM = ITEMS.register("mossy_oak_planks_fence", () -> new BlockItem(BlockModule.MOSSY_OAK_PLANKS.getFence(), new Item.Properties().setId(RegisterHelper.itemKey("mossy_oak_planks_fence"))));
    public static final RegistrySupplier<Item> MOSSY_OAK_FENCE_GATE_ITEM = ITEMS.register("mossy_oak_planks_fence_gate", () -> new BlockItem(BlockModule.MOSSY_OAK_PLANKS.getFenceGate(), new Item.Properties().setId(RegisterHelper.itemKey("mossy_oak_planks_fence_gate"))));
    public static final RegistrySupplier<Item> MOSSY_OAK_DOOR_ITEM = ITEMS.register("mossy_oak_door", () -> new BlockItem(BlockModule.MOSSY_OAK_DOOR.get(), new Item.Properties().setId(RegisterHelper.itemKey("mossy_oak_door"))));


    public static final RegistrySupplier<Item> GIANT_LILYPAD_ITEM = ITEMS.register("giant_lilypad", () -> new PlaceOnWaterBlockItem(BlockModule.GIANT_LILYPAD.get(), new Item.Properties().setId(RegisterHelper.itemKey("giant_lilypad"))));

    public static final RegistrySupplier<RibbitSpawnEggItem> RIBBIT_NITWIT_SPAWN_EGG = RegisterHelper.registerRibbitSpawnEggItem(ITEMS, "ribbit_nitwit_spawn_egg", RibbitProfessionModule.NITWIT);
    public static final RegistrySupplier<RibbitSpawnEggItem> RIBBIT_FISHERMAN_SPAWN_EGG = RegisterHelper.registerRibbitSpawnEggItem(ITEMS, "ribbit_fisherman_spawn_egg", RibbitProfessionModule.FISHERMAN);
    public static final RegistrySupplier<RibbitSpawnEggItem> RIBBIT_GARDENER_SPAWN_EGG = RegisterHelper.registerRibbitSpawnEggItem(ITEMS, "ribbit_gardener_spawn_egg", RibbitProfessionModule.GARDENER);
    public static final RegistrySupplier<RibbitSpawnEggItem> RIBBIT_MERCHANT_SPAWN_EGG = RegisterHelper.registerRibbitSpawnEggItem(ITEMS, "ribbit_merchant_spawn_egg", RibbitProfessionModule.MERCHANT);
    public static final RegistrySupplier<RibbitSpawnEggItem> RIBBIT_SORCERER_SPAWN_EGG = RegisterHelper.registerRibbitSpawnEggItem(ITEMS, "ribbit_sorcerer_spawn_egg", RibbitProfessionModule.SORCERER);

    public static final RegistrySupplier<MaracaItem> MARACA = RegisterHelper.registerItem(ITEMS, "maraca",
            MaracaItem::new, properties -> properties.stacksTo(1));

    public static void init() {
        ITEMS.register();

        // Defer compostable registrations until items exist
        SWAMP_DAISY_ITEM.listen(item -> ComposterBlock.COMPOSTABLES.put(item, 0.65F));
        GIANT_LILYPAD_ITEM.listen(item -> ComposterBlock.COMPOSTABLES.put(item, 0.65F));
        UMBRELLA_LEAF_ITEM.listen(item -> ComposterBlock.COMPOSTABLES.put(item, 0.65F));
        TOADSTOOL_ITEM.listen(item -> ComposterBlock.COMPOSTABLES.put(item, 0.65F));
        TOADSTOOL_STEM_ITEM.listen(item -> ComposterBlock.COMPOSTABLES.put(item, 0.85F));
        BROWN_TOADSTOOL_ITEM.listen(item -> ComposterBlock.COMPOSTABLES.put(item, 0.85F));
        RED_TOADSTOOL_ITEM.listen(item -> ComposterBlock.COMPOSTABLES.put(item, 0.85F));

        // Defer dispenser behaviors until spawn eggs exist
        DispenseItemBehavior ribbitSpawnEggDispenseItemBehavior = new RibbitSpawnEggDispenseItemBehavior();
        RIBBIT_NITWIT_SPAWN_EGG.listen(item -> DispenserBlock.registerBehavior(item, ribbitSpawnEggDispenseItemBehavior));
        RIBBIT_FISHERMAN_SPAWN_EGG.listen(item -> DispenserBlock.registerBehavior(item, ribbitSpawnEggDispenseItemBehavior));
        RIBBIT_GARDENER_SPAWN_EGG.listen(item -> DispenserBlock.registerBehavior(item, ribbitSpawnEggDispenseItemBehavior));
        RIBBIT_MERCHANT_SPAWN_EGG.listen(item -> DispenserBlock.registerBehavior(item, ribbitSpawnEggDispenseItemBehavior));
        RIBBIT_SORCERER_SPAWN_EGG.listen(item -> DispenserBlock.registerBehavior(item, ribbitSpawnEggDispenseItemBehavior));
    }
}
