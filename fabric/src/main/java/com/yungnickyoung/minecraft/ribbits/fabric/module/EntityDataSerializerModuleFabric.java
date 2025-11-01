package com.yungnickyoung.minecraft.ribbits.fabric.module;

import com.yungnickyoung.minecraft.ribbits.RibbitsCommon;
import com.yungnickyoung.minecraft.ribbits.module.EntityDataSerializerModule;
import net.fabricmc.fabric.api.object.builder.v1.entity.FabricTrackedDataRegistry;
import net.minecraft.resources.ResourceLocation;

public class EntityDataSerializerModuleFabric {
    public static void init() {
        FabricTrackedDataRegistry.register(ResourceLocation.fromNamespaceAndPath(RibbitsCommon.MOD_ID, "ribbit_data"), EntityDataSerializerModule.RIBBIT_DATA_SERIALIZER);
    }
}
