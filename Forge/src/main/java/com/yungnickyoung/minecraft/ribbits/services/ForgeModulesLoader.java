package com.yungnickyoung.minecraft.ribbits.services;

import com.yungnickyoung.minecraft.ribbits.module.*;

public class ForgeModulesLoader implements IModulesLoader {
    @Override
    public void loadModules() {
        ConfigModuleForge.init();
        TagModuleForge.init();
        StructureProcessorModuleForge.init();
        StructureFeatureModuleForge.init();
    }
}
