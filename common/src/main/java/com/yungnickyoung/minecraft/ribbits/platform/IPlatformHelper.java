package com.yungnickyoung.minecraft.ribbits.platform;

import net.minecraft.server.MinecraftServer;
import net.minecraft.world.level.block.Block;

public interface IPlatformHelper {
    void setBlockAsFlammable(Block block, int igniteChance, int burnChance);

    MinecraftServer getCurrentServer();

    String getPlatformName();
}
