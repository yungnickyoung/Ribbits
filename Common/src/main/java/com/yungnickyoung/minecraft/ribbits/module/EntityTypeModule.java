package com.yungnickyoung.minecraft.ribbits.module;

import com.yungnickyoung.minecraft.ribbits.RibbitsCommon;
import com.yungnickyoung.minecraft.ribbits.entity.RibbitEntity;
import com.yungnickyoung.minecraft.yungsapi.api.autoregister.AutoRegister;
import com.yungnickyoung.minecraft.yungsapi.api.autoregister.AutoRegisterEntityType;
import net.minecraft.world.entity.MobCategory;

@AutoRegister(RibbitsCommon.MOD_ID)
public class EntityTypeModule {
    @AutoRegister("ribbit")
    public static final AutoRegisterEntityType<RibbitEntity> RIBBIT = AutoRegisterEntityType.of(() ->
            AutoRegisterEntityType.Builder
                    .of(RibbitEntity::new, MobCategory.CREATURE)
                    .sized(0.75f, 0.9f)
                    .build())
            .attributes(RibbitEntity::createRibbitAttributes);
}
