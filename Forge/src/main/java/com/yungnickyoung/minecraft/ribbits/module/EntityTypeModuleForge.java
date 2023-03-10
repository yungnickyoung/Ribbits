package com.yungnickyoung.minecraft.ribbits.module;

import com.yungnickyoung.minecraft.ribbits.RibbitsCommon;
import com.yungnickyoung.minecraft.ribbits.entity.RibbitEntity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.MobCategory;
import net.minecraftforge.event.entity.EntityAttributeCreationEvent;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;

public class EntityTypeModuleForge {
    public static final DeferredRegister<EntityType<?>> ENTITY_TYPES = DeferredRegister.create(ForgeRegistries.ENTITIES, RibbitsCommon.MOD_ID);

    public static final RegistryObject<EntityType<RibbitEntity>> RIBBIT = ENTITY_TYPES.register("ribbit",
                    () -> EntityType.Builder.of(RibbitEntity::new, MobCategory.CREATURE)
                    .sized(0.75f, 0.9f)
                    .build( "ribbit"));

    public static void init() {
        ENTITY_TYPES.register(FMLJavaModLoadingContext.get().getModEventBus());
        FMLJavaModLoadingContext.get().getModEventBus().addListener(EntityTypeModuleForge::addEntityAttributes);
    }

    public static void addEntityAttributes(EntityAttributeCreationEvent event) {
        event.put(RIBBIT.get(), RibbitEntity.createMobAttributes().build());
    }
}
