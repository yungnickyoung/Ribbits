package com.yungnickyoung.minecraft.ribbits.client;

import com.yungnickyoung.minecraft.ribbits.RibbitsCommon;
import com.yungnickyoung.minecraft.ribbits.client.model.SupporterHatModel;
import com.yungnickyoung.minecraft.ribbits.client.particle.RibbitSpellParticle;
import com.yungnickyoung.minecraft.ribbits.client.render.RibbitRenderer;
import com.yungnickyoung.minecraft.ribbits.module.BlockModule;
import com.yungnickyoung.minecraft.ribbits.module.EntityTypeModule;
import com.yungnickyoung.minecraft.ribbits.module.ParticleTypeModule;
import net.minecraft.client.renderer.ItemBlockRenderTypes;
import net.minecraft.client.renderer.RenderType;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.fml.ModContainer;
import net.neoforged.fml.common.Mod;
import net.neoforged.fml.event.lifecycle.FMLClientSetupEvent;
import net.neoforged.neoforge.client.event.EntityRenderersEvent;
import net.neoforged.neoforge.client.event.RegisterParticleProvidersEvent;

@Mod(value = RibbitsCommon.MOD_ID, dist = Dist.CLIENT)
public class RibbitsNeoForgeClient {
    public RibbitsNeoForgeClient(IEventBus eventBus, ModContainer container) {
        RibbitsCommonClient.init();
        eventBus.addListener(RibbitsNeoForgeClient::clientSetup);
        eventBus.addListener(RibbitsNeoForgeClient::registerRenderers);
        eventBus.addListener(RibbitsNeoForgeClient::registerLayers);
        eventBus.addListener(RibbitsNeoForgeClient::registerParticleFactories);
    }

    private static void clientSetup(final FMLClientSetupEvent event) {
        ItemBlockRenderTypes.setRenderLayer(BlockModule.SWAMP_LANTERN.get(), RenderType.cutout());
        ItemBlockRenderTypes.setRenderLayer(BlockModule.GIANT_LILYPAD.get(), RenderType.cutout());
        ItemBlockRenderTypes.setRenderLayer(BlockModule.SWAMP_DAISY.get(), RenderType.cutout());
        ItemBlockRenderTypes.setRenderLayer(BlockModule.TOADSTOOL.get(), RenderType.cutout());
        ItemBlockRenderTypes.setRenderLayer(BlockModule.UMBRELLA_LEAF.get(), RenderType.cutout());
        ItemBlockRenderTypes.setRenderLayer(BlockModule.MOSSY_OAK_DOOR.get(), RenderType.cutout());
     }

    private static void registerRenderers(EntityRenderersEvent.RegisterRenderers event) {
        event.registerEntityRenderer(EntityTypeModule.RIBBIT.get(), RibbitRenderer::new);
    }

    private static void registerLayers(EntityRenderersEvent.RegisterLayerDefinitions event) {
        event.registerLayerDefinition(SupporterHatModel.LAYER_LOCATION, SupporterHatModel::getTexturedModelData);
    }

    private static void registerParticleFactories(RegisterParticleProvidersEvent event) {
        event.registerSpriteSet(ParticleTypeModule.SPELL.get(), RibbitSpellParticle.Factory::new);
    }
}
