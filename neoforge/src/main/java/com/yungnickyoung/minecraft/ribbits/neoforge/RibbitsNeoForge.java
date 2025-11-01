package com.yungnickyoung.minecraft.ribbits.neoforge;

import com.yungnickyoung.minecraft.ribbits.RibbitsCommon;
import com.yungnickyoung.minecraft.ribbits.neoforge.module.EntityDataSerializerModuleNeoForge;
import com.yungnickyoung.minecraft.ribbits.player.PlayerInstrumentTracker;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.fml.ModContainer;
import net.neoforged.fml.common.Mod;
import net.neoforged.neoforge.common.NeoForge;
import net.neoforged.neoforge.event.tick.ServerTickEvent;

@Mod(value = RibbitsCommon.MOD_ID)
public class RibbitsNeoForge {

    public RibbitsNeoForge(IEventBus eventBus, ModContainer container) {
        EntityDataSerializerModuleNeoForge.DATA_SERIALIZERS.register(eventBus);
        RibbitsCommon.init();
        NeoForge.EVENT_BUS.addListener(RibbitsNeoForge::onServerTickStart);
    }

    private static void onServerTickStart(ServerTickEvent.Pre event) {
        PlayerInstrumentTracker.onServerTick();
    }
}