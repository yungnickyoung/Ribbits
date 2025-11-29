package com.yungnickyoung.minecraft.ribbits.neoforge;

import com.yungnickyoung.minecraft.ribbits.RibbitsCommon;
import com.yungnickyoung.minecraft.ribbits.neoforge.module.EntityDataSerializerModuleNeoForge;
import com.yungnickyoung.minecraft.ribbits.network.payload.RequestSupporterHatStatePayload;
import com.yungnickyoung.minecraft.ribbits.player.PlayerInstrumentTracker;
import com.yungnickyoung.minecraft.ribbits.supporters.SupportersListServer;
import net.minecraft.server.level.ServerPlayer;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.fml.ModContainer;
import net.neoforged.fml.common.Mod;
import net.neoforged.neoforge.common.NeoForge;
import net.neoforged.neoforge.event.entity.player.PlayerEvent;
import net.neoforged.neoforge.event.tick.ServerTickEvent;
import net.neoforged.neoforge.network.PacketDistributor;

import java.util.List;
import java.util.UUID;

@Mod(value = RibbitsCommon.MOD_ID)
public class RibbitsNeoForge {

    public RibbitsNeoForge(IEventBus eventBus, ModContainer container) {
        EntityDataSerializerModuleNeoForge.DATA_SERIALIZERS.register(eventBus);
        RibbitsCommon.init();
        NeoForge.EVENT_BUS.addListener(RibbitsNeoForge::onServerTickStart);
        NeoForge.EVENT_BUS.addListener((PlayerEvent.PlayerLoggedInEvent e) -> {
            List<UUID> playersWithSupporterHat = SupportersListServer.getPlayersWithSupporterHat().stream().toList();
            PacketDistributor.sendToPlayer((ServerPlayer) e.getEntity(), new RequestSupporterHatStatePayload(playersWithSupporterHat));
        });
    }

    private static void onServerTickStart(ServerTickEvent.Pre event) {
        PlayerInstrumentTracker.onServerTick();
    }
}