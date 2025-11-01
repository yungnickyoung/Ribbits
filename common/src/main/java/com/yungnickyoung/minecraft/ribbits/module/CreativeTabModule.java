package com.yungnickyoung.minecraft.ribbits.module;

import com.yungnickyoung.minecraft.ribbits.RibbitsCommon;
import dev.architectury.registry.CreativeTabRegistry;
import dev.architectury.registry.registries.DeferredRegister;
import dev.architectury.registry.registries.RegistrySupplier;
import net.minecraft.core.registries.Registries;
import net.minecraft.network.chat.Component;
import net.minecraft.world.item.CreativeModeTab;
import net.minecraft.world.item.ItemStack;

public class CreativeTabModule {
    public static final DeferredRegister<CreativeModeTab> TABS = DeferredRegister.create(RibbitsCommon.MOD_ID, Registries.CREATIVE_MODE_TAB);

    public static final RegistrySupplier<CreativeModeTab> GENERAL = TABS.register("general", () ->
            CreativeTabRegistry.create(builder ->
                    builder.title(Component.translatable("itemGroup.ribbits.general"))
                            .icon(() -> new ItemStack(ItemModule.RED_TOADSTOOL_ITEM.get()))
                            .displayItems((params, output) -> {
                                output.accept(ItemModule.RED_TOADSTOOL_ITEM.get());
                                output.accept(ItemModule.BROWN_TOADSTOOL_ITEM.get());
                                output.accept(ItemModule.TOADSTOOL_STEM_ITEM.get());
                                output.accept(ItemModule.SWAMP_LANTERN_ITEM.get());
                                output.accept(ItemModule.GIANT_LILYPAD_ITEM.get());
                                output.accept(ItemModule.SWAMP_DAISY_ITEM.get());
                                output.accept(ItemModule.TOADSTOOL_ITEM.get());
                                output.accept(ItemModule.UMBRELLA_LEAF_ITEM.get());
                                output.accept(ItemModule.MOSSY_OAK_PLANKS_ITEM.get());
                                output.accept(ItemModule.MOSSY_OAK_STAIRS_ITEM.get());
                                output.accept(ItemModule.MOSSY_OAK_SLAB_ITEM.get());
                                output.accept(ItemModule.MOSSY_OAK_FENCE_ITEM.get());
                                output.accept(ItemModule.MOSSY_OAK_FENCE_GATE_ITEM.get());
                                output.accept(ItemModule.MOSSY_OAK_DOOR_ITEM.get());

                                output.accept(ItemModule.MARACA.get());

                                output.accept(ItemModule.RIBBIT_NITWIT_SPAWN_EGG.get());
                                output.accept(ItemModule.RIBBIT_FISHERMAN_SPAWN_EGG.get());
                                output.accept(ItemModule.RIBBIT_GARDENER_SPAWN_EGG.get());
                                output.accept(ItemModule.RIBBIT_MERCHANT_SPAWN_EGG.get());
                                output.accept(ItemModule.RIBBIT_SORCERER_SPAWN_EGG.get());
                            })
            )
    );

    public static void init() {
        TABS.register();
    }
}