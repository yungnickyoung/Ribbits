package com.yungnickyoung.minecraft.ribbits.client;

import com.yungnickyoung.minecraft.ribbits.RibbitsCommon;
import com.yungnickyoung.minecraft.ribbits.client.model.SupporterHatModel;
import com.yungnickyoung.minecraft.ribbits.client.particle.RibbitSpellParticle;
import com.yungnickyoung.minecraft.ribbits.client.render.RibbitRenderer;
import com.yungnickyoung.minecraft.ribbits.module.BlockModule;
import com.yungnickyoung.minecraft.ribbits.module.EntityTypeModule;
import com.yungnickyoung.minecraft.ribbits.module.ParticleTypeModule;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.ItemBlockRenderTypes;
import net.minecraft.client.renderer.RenderType;
import net.minecraftforge.client.event.EntityRenderersEvent;
import net.minecraftforge.client.event.RegisterParticleProvidersEvent;
import net.minecraftforge.fml.event.lifecycle.FMLClientSetupEvent;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.fml.ModContainer;
import net.neoforged.fml.common.Mod;

@Mod(value = RibbitsCommon.MOD_ID, dist = Dist.CLIENT)
public class RibbitsNeoForgeClient {
    public static void init(IEventBus eventBus, ModContainer container) {
        RibbitsCommonClient.init();
        FMLJavaModLoadingContext.get().getModEventBus().addListener(RibbitsNeoForgeClient::clientSetup);
        FMLJavaModLoadingContext.get().getModEventBus().addListener(RibbitsNeoForgeClient::registerRenderers);
        FMLJavaModLoadingContext.get().getModEventBus().addListener(RibbitsNeoForgeClient::registerLayers);
        FMLJavaModLoadingContext.get().getModEventBus().addListener(RibbitsNeoForgeClient::registerParticleFactories);
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
        Minecraft.getInstance().particleEngine.register(ParticleTypeModule.SPELL.get(), RibbitSpellParticle.Factory::new);
    }
}
