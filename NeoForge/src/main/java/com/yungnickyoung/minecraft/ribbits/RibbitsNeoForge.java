package com.yungnickyoung.minecraft.ribbits;

import com.yungnickyoung.minecraft.ribbits.module.ConfigModuleNeoForge;
import com.yungnickyoung.minecraft.ribbits.module.NetworkModuleNeoForge;
import com.yungnickyoung.minecraft.ribbits.player.PlayerInstrumentTracker;
import com.yungnickyoung.minecraft.ribbits.supporters.SupporterEventsNeoForge;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.fml.ModContainer;
import net.neoforged.fml.common.Mod;
import net.neoforged.neoforge.common.NeoForge;
import net.neoforged.neoforge.event.tick.ServerTickEvent;

@Mod(value = RibbitsCommon.MOD_ID)
public class RibbitsNeoForge {
    public static IEventBus EVENT_BUS;

    public RibbitsNeoForge(IEventBus eventBus, ModContainer container) {
        EVENT_BUS = eventBus;

        RibbitsCommon.init();

        ConfigModuleNeoForge.init(eventBus, container);
        NetworkModuleNeoForge.init(eventBus);

        NeoForge.EVENT_BUS.addListener(RibbitsNeoForge::onServerTickStart);
        NeoForge.EVENT_BUS.addListener(SupporterEventsNeoForge::onPlayerJoin);
    }

    private static void onServerTickStart(ServerTickEvent.Pre event) {
        PlayerInstrumentTracker.onServerTick();
    }
}