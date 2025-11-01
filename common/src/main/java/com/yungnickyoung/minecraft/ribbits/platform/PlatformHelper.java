package com.yungnickyoung.minecraft.ribbits.platform;

import dev.architectury.injectables.annotations.ExpectPlatform;
import net.minecraft.server.MinecraftServer;
import net.minecraft.world.level.block.Block;

public class PlatformHelper {
    @ExpectPlatform
    public static IPlatformHelper getPlatformService() {
        throw new AssertionError("This method should be replaced by Architectury");
    }

    public static MinecraftServer getCurrentServer() {
        return getPlatformService().getCurrentServer();
    }

    public static void setBlockAsFlammable(Block block, int igniteChance, int burnChance) {
        getPlatformService().setBlockAsFlammable(block, igniteChance, burnChance);
    }

    public static String getPlatformName() {
        return getPlatformService().getPlatformName();
    }
}