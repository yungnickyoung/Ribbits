package com.yungnickyoung.minecraft.ribbits;

import com.yungnickyoung.minecraft.ribbits.module.*;
import com.yungnickyoung.minecraft.ribbits.network.payload.RequestSupporterHatStatePayload;
import com.yungnickyoung.minecraft.ribbits.supporters.SupportersListServer;
import com.yungnickyoung.minecraft.yungsapi.YungsApiCommon;
import dev.architectury.event.events.common.PlayerEvent;
import dev.architectury.networking.NetworkManager;
import dev.architectury.registry.CreativeTabRegistry;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.CreativeModeTabs;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.List;
import java.util.UUID;

public class RibbitsCommon {
    public static final String MOD_ID = "ribbits";
    public static final Logger LOGGER = LogManager.getLogger(MOD_ID);

    // TODO - change this whenever updating to a new Minecraft version
    public static final String MC_VERSION_STRING = "1_21_10";


    public static void init() {
        YungsApiCommon.init();
        ConfigModule.init();
        EntityTypeModule.init();
        BlockModule.init();
        ItemModule.init();
        CreativeTabModule.init();
        FeatureModule.init();
        ParticleTypeModule.init();
        SoundModule.init();
        StructureProcessorTypeModule.init();
        NetworkModule.init();
        RibbitProfessionModule.init();
        RibbitInstrumentModule.init();
        RibbitUmbrellaTypeModule.init();
        PlayerEvent.PLAYER_JOIN.register(serverPlayer -> {
            List<UUID> playersWithSupporterHat = SupportersListServer.getPlayersWithSupporterHat().stream().toList();
            NetworkManager.sendToPlayer(serverPlayer, new RequestSupporterHatStatePayload(playersWithSupporterHat));
        });
    }

    public static ResourceLocation id(String path) {
        return ResourceLocation.fromNamespaceAndPath(MOD_ID, path);
    }
}
