package com.yungnickyoung.minecraft.ribbits.config;

import net.neoforged.neoforge.common.ModConfigSpec;

public class RibbitsConfigNeoForge {
    public static final ModConfigSpec.Builder BUILDER = new ModConfigSpec.Builder();
    public static final ModConfigSpec SPEC;

    public static final ConfigGeneralNeoForge general;

    static {
        BUILDER.push("Ribbits");

        general = new ConfigGeneralNeoForge(BUILDER);

        BUILDER.pop();
        SPEC = BUILDER.build();
    }
}