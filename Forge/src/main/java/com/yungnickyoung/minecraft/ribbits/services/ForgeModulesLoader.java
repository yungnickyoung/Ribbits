package com.yungnickyoung.minecraft.ribbits.services;

import com.yungnickyoung.minecraft.ribbits.module.*;

public class ForgeModulesLoader implements IModulesLoader {
    @Override
    public void loadCommonModules() {
        IModulesLoader.super.loadCommonModules(); // Load common modules
        ConfigModuleForge.init();
    }
}
