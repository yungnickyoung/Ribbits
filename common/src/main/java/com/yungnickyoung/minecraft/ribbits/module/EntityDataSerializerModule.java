package com.yungnickyoung.minecraft.ribbits.module;

import com.yungnickyoung.minecraft.ribbits.data.RibbitData;
import net.minecraft.network.syncher.EntityDataSerializer;

public final class EntityDataSerializerModule {
    public static final EntityDataSerializer<RibbitData> RIBBIT_DATA_SERIALIZER =
            EntityDataSerializer.forValueType(RibbitData.STREAM_CODEC);

    private EntityDataSerializerModule() {
    }
}