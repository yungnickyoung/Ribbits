package com.yungnickyoung.minecraft.ribbits.config;

import net.neoforged.neoforge.common.ModConfigSpec;

public class ConfigGeneralNeoForge {
    public final ModConfigSpec.ConfigValue<Boolean> prideFlagAllYear;

    public ConfigGeneralNeoForge(final ModConfigSpec.Builder BUILDER) {
        BUILDER
                .comment(
                        """
                                ##########################################################################################################
                                # General settings.
                                ##########################################################################################################""")
                .push("General");

        prideFlagAllYear = BUILDER
                .comment(
                        """
                        If enabled, Pride Ribbits will exist all year instead of only in June.
                        Default: false""".indent(1))
                .define("Show Pride Flags All Year", false);

        BUILDER.pop();
    }
}

