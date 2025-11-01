package com.yungnickyoung.minecraft.ribbits.platform.fabric;

import com.yungnickyoung.minecraft.ribbits.platform.IPlatformHelper;

public class PlatformHelperImpl {
    public static IPlatformHelper getPlatformService() {
        return new FabricPlatformHelper();
    }
}
