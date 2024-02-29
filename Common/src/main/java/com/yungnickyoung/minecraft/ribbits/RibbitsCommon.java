package com.yungnickyoung.minecraft.ribbits;

import com.yungnickyoung.minecraft.ribbits.module.BlockModule;
import com.yungnickyoung.minecraft.ribbits.module.ConfigModule;
import com.yungnickyoung.minecraft.ribbits.module.ItemModule;
import com.yungnickyoung.minecraft.ribbits.services.Services;
import com.yungnickyoung.minecraft.yungsapi.api.YungAutoRegister;
import com.yungnickyoung.minecraft.yungsapi.api.autoregister.AutoRegister;
import com.yungnickyoung.minecraft.yungsapi.api.autoregister.AutoRegisterCreativeTab;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.ItemStack;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

@AutoRegister(RibbitsCommon.MOD_ID)
public class RibbitsCommon {
    public static final String MOD_ID = "ribbits";
    public static final Logger LOGGER = LogManager.getLogger(MOD_ID);

    public static final ConfigModule CONFIG = new ConfigModule();

    @AutoRegister("general")
    public static AutoRegisterCreativeTab TAB = AutoRegisterCreativeTab.builder()
            .title(Component.translatable("itemGroup.ribbits.general"))
            .iconItem(() -> new ItemStack(BlockModule.RED_TOADSTOOL.get()))
            .entries((params, output) -> {
                output.accept(BlockModule.RED_TOADSTOOL.get());
                output.accept(BlockModule.BROWN_TOADSTOOL.get());
                output.accept(BlockModule.TOADSTOOL_STEM.get());
                output.accept(BlockModule.SWAMP_LANTERN.get());
                output.accept(ItemModule.GIANT_LILYPAD.get());
                output.accept(BlockModule.SWAMP_DAISY.get());
                output.accept(BlockModule.TOADSTOOL.get());
                output.accept(BlockModule.UMBRELLA_LEAF.get());
                output.accept(BlockModule.MOSSY_OAK_PLANKS.get());
                output.accept(BlockModule.MOSSY_OAK_PLANKS.getStairs());
                output.accept(BlockModule.MOSSY_OAK_PLANKS.getSlab());
                output.accept(BlockModule.MOSSY_OAK_PLANKS.getFence());
                output.accept(BlockModule.MOSSY_OAK_PLANKS.getFenceGate());
                output.accept(BlockModule.MOSSY_OAK_DOOR.get());

                output.accept(ItemModule.RIBBIT_NITWIT_SPAWN_EGG.get());
                output.accept(ItemModule.RIBBIT_FISHERMAN_SPAWN_EGG.get());
                output.accept(ItemModule.RIBBIT_GARDENER_SPAWN_EGG.get());
                output.accept(ItemModule.RIBBIT_MERCHANT_SPAWN_EGG.get());
                output.accept(ItemModule.RIBBIT_SORCERER_SPAWN_EGG.get());
            })
            .build();

    public static void init() {
        YungAutoRegister.scanPackageForAnnotations("com.yungnickyoung.minecraft.ribbits");
        Services.MODULES.loadCommonModules();
    }

    public static ResourceLocation id(String path) {
        return new ResourceLocation(MOD_ID, path);
    }
}
