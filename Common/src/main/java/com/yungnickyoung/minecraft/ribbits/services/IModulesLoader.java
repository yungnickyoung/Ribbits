package com.yungnickyoung.minecraft.ribbits.services;

import com.yungnickyoung.minecraft.ribbits.module.StructureFeatureModule;
import com.yungnickyoung.minecraft.ribbits.module.StructureProcessorModule;

public interface IModulesLoader {
    default void loadModules() {
        StructureFeatureModule.init();
        StructureProcessorModule.init();
    }
}
