package com.yungnickyoung.minecraft.ribbits.module;

import com.yungnickyoung.minecraft.ribbits.RibbitsCommon;
import com.yungnickyoung.minecraft.ribbits.data.RibbitData;
import com.yungnickyoung.minecraft.yungsapi.api.autoregister.AutoRegister;
import net.minecraft.network.syncher.EntityDataSerializer;

@AutoRegister(RibbitsCommon.MOD_ID)
public class EntityDataSerializerModule {
    /**
     * Data serializer for Ribbits.
     * This mimics the serializer used for vanilla VillagerData, but tweaked for our purposes.
     */
    @AutoRegister("ribbit_data")
    public static final EntityDataSerializer<RibbitData> RIBBIT_DATA_SERIALIZER = EntityDataSerializer.forValueType(RibbitData.STREAM_CODEC);
}
