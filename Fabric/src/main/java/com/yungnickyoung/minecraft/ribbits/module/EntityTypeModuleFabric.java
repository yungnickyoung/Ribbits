package com.yungnickyoung.minecraft.ribbits.module;

import com.yungnickyoung.minecraft.ribbits.RibbitsCommon;
import com.yungnickyoung.minecraft.ribbits.entity.RibbitEntity;
import net.fabricmc.fabric.api.object.builder.v1.entity.FabricEntityTypeBuilder;
import net.minecraft.core.Registry;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.EntityDimensions;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.MobCategory;

public class EntityTypeModuleFabric {
    public static final EntityType<RibbitEntity> RIBBIT = Registry.register(
            Registry.ENTITY_TYPE,
            new ResourceLocation(RibbitsCommon.MOD_ID, "ribbit"),
            FabricEntityTypeBuilder.create(MobCategory.CREATURE, RibbitEntity::new)
                    .dimensions(EntityDimensions.fixed(0.75f, 0.9f))
                    .build());
}
