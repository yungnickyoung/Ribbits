package com.yungnickyoung.minecraft.ribbits.module;

import com.yungnickyoung.minecraft.ribbits.RibbitsCommon;
import com.yungnickyoung.minecraft.ribbits.data.RibbitData;
import com.yungnickyoung.minecraft.yungsapi.api.autoregister.AutoRegister;
import net.minecraft.network.syncher.EntityDataSerializer;
import net.minecraft.network.syncher.EntityDataSerializers;

@AutoRegister(RibbitsCommon.MOD_ID)
public class EntityDataSerializerModule {
    /**
     * Data serializer for Ribbits.
     * This mimics the serializer used for vanilla VillagerData, but tweaked for our purposes.
     */
    public static final EntityDataSerializer<RibbitData> RIBBIT_DATA_SERIALIZER = EntityDataSerializer.forValueType(RibbitData.STREAM_CODEC);

    /**
     * AutoRegister doesn't natively support registering data serializers, so we have to do it manually.
     * <p>
     * The AutoRegister system will call this method after mod initialization is complete.
     * For Fabric, this simply occurs at the end of init, after everything has been registered.
     * For Forge, this is during CommonSetup.</p>
     * <p>
     * Note that the AutoRegister annotation's value is ignored, hence the dummy value.
     */
    @AutoRegister("_ignored")
    public static void register() {
        RibbitsCommon.LOGGER.info("Registering Ribbit data serializers...");
        EntityDataSerializers.registerSerializer(RIBBIT_DATA_SERIALIZER);
    }
}
