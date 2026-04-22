package com.yungnickyoung.minecraft.ribbits.neoforge.config;

import net.neoforged.bus.api.IEventBus;
import net.neoforged.fml.ModContainer;

public record NeoForgeConfigContext(ModContainer container, IEventBus modEventBus) {
}

