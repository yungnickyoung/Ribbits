package com.yungnickyoung.minecraft.ribbits;

import com.yungnickyoung.minecraft.ribbits.RibbitsCommon;
import com.yungnickyoung.minecraft.ribbits.entity.RibbitEntity;
import com.yungnickyoung.minecraft.ribbits.module.EntityTypeModuleFabric;
import net.fabricmc.api.ModInitializer;
import net.fabricmc.fabric.api.object.builder.v1.entity.FabricDefaultAttributeRegistry;
import software.bernie.geckolib3.GeckoLib;

public class RibbitsFabric implements ModInitializer {
    @Override
    public void onInitialize() {
        GeckoLib.initialize();
        RibbitsCommon.init();
        FabricDefaultAttributeRegistry.register(EntityTypeModuleFabric.RIBBIT, RibbitEntity.createRibbitAttributes());
    }
}
