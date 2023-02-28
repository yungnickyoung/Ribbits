package com.yungnickyoung.minecraft.ribbits.module;

import com.yungnickyoung.minecraft.ribbits.RibbitsCommon;
import com.yungnickyoung.minecraft.ribbits.entity.FrostMinerEntity;
import net.fabricmc.fabric.api.object.builder.v1.entity.FabricEntityTypeBuilder;
import net.minecraft.core.Registry;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.EntityDimensions;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.MobCategory;

public class EntityTypeModuleFabric {
    public static final EntityType<FrostMinerEntity> FROST_MINER = Registry.register(
            Registry.ENTITY_TYPE,
            new ResourceLocation(RibbitsCommon.MOD_ID, "frost_miner"),
            FabricEntityTypeBuilder.create(MobCategory.MONSTER, FrostMinerEntity::new)
                    .dimensions(EntityDimensions.fixed(2.5f, 3.0f))
                    .build());
}
