package com.yungnickyoung.minecraft.ribbits;

import com.yungnickyoung.minecraft.ribbits.client.RibbitsForgeClient;
import com.yungnickyoung.minecraft.ribbits.module.EntityTypeModuleForge;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.fml.DistExecutor;
import net.minecraftforge.fml.common.Mod;

@Mod(RibbitsCommon.MOD_ID)
public class RibbitsForge {
    public RibbitsForge() {
        RibbitsCommon.init();
        DistExecutor.safeRunWhenOn(Dist.CLIENT, () -> RibbitsForgeClient::init);
        EntityTypeModuleForge.init();
    }
}