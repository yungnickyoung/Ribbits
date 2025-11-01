package com.yungnickyoung.minecraft.yungsapi;

import com.yungnickyoung.minecraft.ribbits.RibbitsCommon;
import com.yungnickyoung.minecraft.yungsapi.module.StructurePlacementTypeModule;
import com.yungnickyoung.minecraft.yungsapi.module.StructurePoolElementTypeModule;
import com.yungnickyoung.minecraft.yungsapi.module.StructureTypeModule;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

/**
 * API for YUNG's Minecraft mods.
 * Most classes in this project are either useful data abstractions
 * or static helper classes.
 */
public class YungsApiCommon {
    public static final Logger LOGGER = LogManager.getLogger(RibbitsCommon.MOD_ID);

    public static void init() {
        StructurePlacementTypeModule.register();
        StructurePoolElementTypeModule.register();
        StructureTypeModule.register();
    }
}
