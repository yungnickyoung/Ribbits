package com.yungnickyoung.minecraft.ribbits.platform.fabric;

import com.yungnickyoung.minecraft.ribbits.fabric.RibbitsFabric;
import com.yungnickyoung.minecraft.ribbits.platform.IPlatformHelper;
import net.minecraft.server.MinecraftServer;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.FireBlock;

public class FabricPlatformHelper implements IPlatformHelper {
    @Override
    public void setBlockAsFlammable(Block block, int igniteChance, int burnChance) {
        FireBlock fireBlock = (FireBlock) Blocks.FIRE;
        fireBlock.setFlammable(block, igniteChance, burnChance);
    }

    @Override
    public MinecraftServer getCurrentServer() {
        return RibbitsFabric.getCurrentServer();
    }

    @Override
    public String getPlatformName() {
        return "fabric";
    }
}
