package com.yungnickyoung.minecraft.ribbits.util;

import com.yungnickyoung.minecraft.ribbits.RibbitsCommon;
import com.yungnickyoung.minecraft.ribbits.data.RibbitProfession;
import com.yungnickyoung.minecraft.ribbits.item.RibbitSpawnEggItem;
import dev.architectury.registry.registries.DeferredRegister;
import dev.architectury.registry.registries.RegistrySupplier;
import net.minecraft.core.registries.Registries;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockBehaviour;

import java.util.function.Function;
import java.util.function.Supplier;
import java.util.function.UnaryOperator;

public class RegisterHelper {
    public static ResourceLocation id(String name) {
        return ResourceLocation.fromNamespaceAndPath(RibbitsCommon.MOD_ID, name);
    }

    public static ResourceKey<Block> blockKey(String name) {
        return ResourceKey.create(Registries.BLOCK, id(name));
    }

    public static ResourceKey<Item> itemKey(String name) {
        return ResourceKey.create(Registries.ITEM, id(name));
    }

    public static <B extends Block> RegistrySupplier<B> registerBlock(DeferredRegister<Block> register, String name,
                                                                      Function<BlockBehaviour.Properties, B> blockFactory,
                                                                      UnaryOperator<BlockBehaviour.Properties> propertiesModifier) {
        return register.register(name, () -> {
            BlockBehaviour.Properties properties = propertiesModifier.apply(BlockBehaviour.Properties.of())
                    .setId(blockKey(name));
            return blockFactory.apply(properties);
        });
    }

    public static <B extends Block> RegistrySupplier<B> registerBlock(DeferredRegister<Block> register, String name,
                                                                      Function<BlockBehaviour.Properties, B> blockFactory) {
        return registerBlock(register, name, blockFactory, properties -> properties);
    }

    public static <I extends Item> RegistrySupplier<I> registerItem(DeferredRegister<Item> register, String name,
                                                                    Function<Item.Properties, I> itemFactory,
                                                                    UnaryOperator<Item.Properties> propertiesModifier) {
        return register.register(name, () -> {
            Item.Properties properties = propertiesModifier.apply(new Item.Properties())
                    .setId(itemKey(name));
            return itemFactory.apply(properties);
        });
    }

    public static <I extends Item> RegistrySupplier<I> registerItem(DeferredRegister<Item> register, String name,
                                                                    Function<Item.Properties, I> itemFactory) {
        return registerItem(register, name, itemFactory, properties -> properties);
    }


    public static RegistrySupplier<BlockItem> registerBlockItem(DeferredRegister<Item> register, String name,
                                                                Supplier<Block> blockSupplier,
                                                                UnaryOperator<Item.Properties> propertiesModifier) {
        return registerItem(register, name, properties -> new BlockItem(blockSupplier.get(), properties), propertiesModifier);
    }

    public static RegistrySupplier<BlockItem> registerBlockItem(DeferredRegister<Item> register, String name,
                                                                Supplier<Block> blockSupplier) {
        return registerBlockItem(register, name, blockSupplier, properties -> properties);
    }

    public static RegistrySupplier<RibbitSpawnEggItem> registerRibbitSpawnEggItem(DeferredRegister<Item> register, String name,
                                                                                  RibbitProfession profession,
                                                                                  UnaryOperator<Item.Properties> propertiesModifier) {
        return registerItem(register, name, properties -> new RibbitSpawnEggItem(profession, properties), propertiesModifier);

    }

    public static RegistrySupplier<RibbitSpawnEggItem> registerRibbitSpawnEggItem(DeferredRegister<Item> register, String name,
                                                                                  RibbitProfession profession) {
        return registerRibbitSpawnEggItem(register, name, profession, properties -> properties);
    }
}