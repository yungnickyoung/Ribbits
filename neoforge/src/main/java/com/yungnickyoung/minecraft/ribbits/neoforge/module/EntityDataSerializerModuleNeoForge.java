package com.yungnickyoung.minecraft.ribbits.neoforge.module;

import com.yungnickyoung.minecraft.ribbits.RibbitsCommon;
import com.yungnickyoung.minecraft.ribbits.data.RibbitData;
import com.yungnickyoung.minecraft.ribbits.module.EntityDataSerializerModule;
import net.minecraft.network.syncher.EntityDataSerializer;
import net.neoforged.neoforge.registries.DeferredRegister;
import net.neoforged.neoforge.registries.NeoForgeRegistries;

import java.util.function.Supplier;

public class EntityDataSerializerModuleNeoForge {
    public static final DeferredRegister<EntityDataSerializer<?>> DATA_SERIALIZERS = DeferredRegister.create(NeoForgeRegistries.Keys.ENTITY_DATA_SERIALIZERS, RibbitsCommon.MOD_ID);

    public static final Supplier<EntityDataSerializer<RibbitData>> RIBBIT_DATA = DATA_SERIALIZERS.register(
            "ribbit_data",
            () -> EntityDataSerializerModule.RIBBIT_DATA_SERIALIZER
    );


}
