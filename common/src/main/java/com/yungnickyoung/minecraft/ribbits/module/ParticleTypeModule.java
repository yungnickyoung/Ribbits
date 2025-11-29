package com.yungnickyoung.minecraft.ribbits.module;

import com.yungnickyoung.minecraft.ribbits.RibbitsCommon;
import com.yungnickyoung.minecraft.yungsapi.api.autoregister.AutoRegister;
import com.yungnickyoung.minecraft.yungsapi.api.autoregister.AutoRegisterParticleType;
import net.minecraft.core.particles.SimpleParticleType;

@AutoRegister(RibbitsCommon.MOD_ID)
public class ParticleTypeModule {

    @AutoRegister("spell")
    public static final AutoRegisterParticleType<SimpleParticleType> SPELL = AutoRegisterParticleType.simple();
}
