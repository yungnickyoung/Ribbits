package com.yungnickyoung.minecraft.ribbits.services;

import com.yungnickyoung.minecraft.ribbits.module.*;

public class FabricModulesLoader implements IModulesLoader {
    @Override
    public void loadCommonModules() {
        IModulesLoader.super.loadCommonModules(); // Load common modules
        ConfigModuleFabric.init();
    }
}
