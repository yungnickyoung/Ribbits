package com.yungnickyoung.minecraft.ribbits.fabric.client;

import com.yungnickyoung.minecraft.ribbits.client.RibbitsCommonClient;
import com.yungnickyoung.minecraft.ribbits.client.model.SupporterHatModel;
import com.yungnickyoung.minecraft.ribbits.client.particle.RibbitSpellParticle;
import com.yungnickyoung.minecraft.ribbits.client.render.RibbitRenderer;
import com.yungnickyoung.minecraft.ribbits.fabric.module.NetworkModuleFabric;
import com.yungnickyoung.minecraft.ribbits.module.BlockModule;
import com.yungnickyoung.minecraft.ribbits.module.EntityTypeModule;
import com.yungnickyoung.minecraft.ribbits.module.ParticleTypeModule;
import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.fabric.api.client.particle.v1.ParticleFactoryRegistry;
import net.fabricmc.fabric.api.client.rendering.v1.BlockRenderLayerMap;
import net.fabricmc.fabric.api.client.rendering.v1.EntityModelLayerRegistry;
import net.minecraft.client.renderer.chunk.ChunkSectionLayer;
import net.minecraft.client.renderer.entity.EntityRenderers;

public class RibbitsFabricClient implements ClientModInitializer {
    @Override
    public void onInitializeClient() {
        RibbitsCommonClient.init();
        NetworkModuleFabric.registerClient();
        BlockRenderLayerMap.putBlock(BlockModule.SWAMP_LANTERN.get(), ChunkSectionLayer.CUTOUT);
        BlockRenderLayerMap.putBlock(BlockModule.GIANT_LILYPAD.get(), ChunkSectionLayer.CUTOUT);
        BlockRenderLayerMap.putBlock(BlockModule.SWAMP_DAISY.get(), ChunkSectionLayer.CUTOUT);
        BlockRenderLayerMap.putBlock(BlockModule.TOADSTOOL.get(), ChunkSectionLayer.CUTOUT);
        BlockRenderLayerMap.putBlock(BlockModule.UMBRELLA_LEAF.get(), ChunkSectionLayer.CUTOUT);
        BlockRenderLayerMap.putBlock(BlockModule.MOSSY_OAK_DOOR.get(), ChunkSectionLayer.CUTOUT);
        EntityRenderers.register(EntityTypeModule.RIBBIT.get(), RibbitRenderer::new);
        EntityModelLayerRegistry.registerModelLayer(SupporterHatModel.LAYER_LOCATION, SupporterHatModel::getTexturedModelData);

        ParticleFactoryRegistry.getInstance().register(ParticleTypeModule.SPELL.get(), RibbitSpellParticle.Factory::new);
    }
}
