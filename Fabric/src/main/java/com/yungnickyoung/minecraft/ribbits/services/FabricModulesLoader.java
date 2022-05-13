package com.yungnickyoung.minecraft.ribbits.services;

import com.yungnickyoung.minecraft.ribbits.module.*;

public class FabricModulesLoader implements IModulesLoader {
    @Override
    public void loadModules() {
        ConfigModuleFabric.init();
        TagModuleFabric.init();
        StructureProcessorModuleFabric.init();
        StructureFeatureModuleFabric.init();
    }
}
