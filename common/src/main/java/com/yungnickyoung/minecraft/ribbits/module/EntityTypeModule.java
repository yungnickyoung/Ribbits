package com.yungnickyoung.minecraft.ribbits.module;

import com.yungnickyoung.minecraft.ribbits.RibbitsCommon;
import com.yungnickyoung.minecraft.ribbits.entity.RibbitEntity;
import dev.architectury.registry.level.entity.EntityAttributeRegistry;
import dev.architectury.registry.registries.DeferredRegister;
import dev.architectury.registry.registries.RegistrySupplier;
import net.minecraft.core.registries.Registries;
import net.minecraft.resources.ResourceKey;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.MobCategory;

public class EntityTypeModule {
    public static final DeferredRegister<EntityType<?>> ENTITY_TYPES = DeferredRegister.create(RibbitsCommon.MOD_ID, Registries.ENTITY_TYPE);

    public static final RegistrySupplier<EntityType<RibbitEntity>> RIBBIT = ENTITY_TYPES.register("ribbit", () ->
            EntityType.Builder
                    .of(RibbitEntity::new, MobCategory.CREATURE)
                    .sized(0.5f, 0.75f)
                    .build(ResourceKey.create(
                            Registries.ENTITY_TYPE,
                            RibbitsCommon.id("ribbit")
                    )));

    public static void init() {
        ENTITY_TYPES.register();
        EntityAttributeRegistry.register(RIBBIT, RibbitEntity::createRibbitAttributes);
    }
}
