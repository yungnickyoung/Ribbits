package com.yungnickyoung.minecraft.ribbits;

import net.fabricmc.api.ModInitializer;

public class RibbitsFabric implements ModInitializer {
    @Override
    public void onInitialize() {
        RibbitsCommon.init();
    }
}
