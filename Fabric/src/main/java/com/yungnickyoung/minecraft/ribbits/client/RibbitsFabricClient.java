package com.yungnickyoung.minecraft.ribbits.client;

import com.yungnickyoung.minecraft.ribbits.client.model.SupporterHatModel;
import com.yungnickyoung.minecraft.ribbits.client.particle.RibbitSpellParticle;
import com.yungnickyoung.minecraft.ribbits.client.render.RibbitRenderer;
import com.yungnickyoung.minecraft.ribbits.module.BlockModule;
import com.yungnickyoung.minecraft.ribbits.module.EntityTypeModule;
import com.yungnickyoung.minecraft.ribbits.module.NetworkModuleFabric;
import com.yungnickyoung.minecraft.ribbits.module.ParticleTypeModule;
import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.fabric.api.blockrenderlayer.v1.BlockRenderLayerMap;
import net.fabricmc.fabric.api.client.particle.v1.ParticleFactoryRegistry;
import net.fabricmc.fabric.api.client.rendering.v1.EntityModelLayerRegistry;
import net.fabricmc.fabric.api.client.rendering.v1.EntityRendererRegistry;
import net.minecraft.client.renderer.RenderType;

public class RibbitsFabricClient implements ClientModInitializer {
    @Override
    public void onInitializeClient() {
        RibbitsCommonClient.init();

        NetworkModuleFabric.registerS2CHandlers();
        BlockRenderLayerMap.INSTANCE.putBlock(BlockModule.SWAMP_LANTERN.get(), RenderType.cutout());
        BlockRenderLayerMap.INSTANCE.putBlock(BlockModule.GIANT_LILYPAD.get(), RenderType.cutout());
        BlockRenderLayerMap.INSTANCE.putBlock(BlockModule.SWAMP_DAISY.get(), RenderType.cutout());
        BlockRenderLayerMap.INSTANCE.putBlock(BlockModule.TOADSTOOL.get(), RenderType.cutout());
        BlockRenderLayerMap.INSTANCE.putBlock(BlockModule.UMBRELLA_LEAF.get(), RenderType.cutout());
        BlockRenderLayerMap.INSTANCE.putBlock(BlockModule.MOSSY_OAK_DOOR.get(), RenderType.cutout());
        EntityRendererRegistry.register(EntityTypeModule.RIBBIT.get(), RibbitRenderer::new);
        EntityModelLayerRegistry.registerModelLayer(SupporterHatModel.LAYER_LOCATION, SupporterHatModel::getTexturedModelData);

        // Particle rendering
        ParticleFactoryRegistry.getInstance().register(ParticleTypeModule.SPELL.get(), RibbitSpellParticle.Factory::new);
    }
}
