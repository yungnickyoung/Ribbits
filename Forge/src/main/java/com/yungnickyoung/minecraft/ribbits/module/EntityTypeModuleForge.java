package com.yungnickyoung.minecraft.ribbits.module;

import com.yungnickyoung.minecraft.ribbits.RibbitsCommon;
import com.yungnickyoung.minecraft.ribbits.entity.FrostMinerEntity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.MobCategory;
import net.minecraftforge.event.entity.EntityAttributeCreationEvent;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;

public class EntityTypeModuleForge {
    public static final DeferredRegister<EntityType<?>> ENTITY_TYPES = DeferredRegister.create(ForgeRegistries.ENTITIES, RibbitsCommon.MOD_ID);

    public static final RegistryObject<EntityType<FrostMinerEntity>> FROST_MINER = ENTITY_TYPES.register("frost_miner",
                    () -> EntityType.Builder.of(FrostMinerEntity::new, MobCategory.MONSTER)
                    .sized(2.5f, 3.0f)
                    .build( "frost_miner"));

    public static void init() {
        ENTITY_TYPES.register(FMLJavaModLoadingContext.get().getModEventBus());
        FMLJavaModLoadingContext.get().getModEventBus().addListener(EntityTypeModuleForge::addEntityAttributes);
    }

    public static void addEntityAttributes(EntityAttributeCreationEvent event) {
        event.put(FROST_MINER.get(), FrostMinerEntity.createAttributes().build());
    }
}
