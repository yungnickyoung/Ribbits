package com.yungnickyoung.minecraft.ribbits;

import net.fabricmc.api.ModInitializer;
import software.bernie.geckolib3.GeckoLib;

public class RibbitsFabric implements ModInitializer {
    @Override
    public void onInitialize() {
        GeckoLib.initialize();
        RibbitsCommon.init();
    }
}
