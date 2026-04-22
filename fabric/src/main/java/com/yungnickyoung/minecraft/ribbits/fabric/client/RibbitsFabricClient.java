package com.yungnickyoung.minecraft.ribbits.fabric.client;

import com.yungnickyoung.minecraft.ribbits.client.RibbitsCommonClient;
import com.yungnickyoung.minecraft.ribbits.client.model.SupporterHatModel;
import com.yungnickyoung.minecraft.ribbits.client.particle.RibbitSpellParticle;
import com.yungnickyoung.minecraft.ribbits.client.render.RibbitRenderer;
import com.yungnickyoung.minecraft.ribbits.fabric.module.NetworkModuleFabric;
import com.yungnickyoung.minecraft.ribbits.module.EntityTypeModule;
import com.yungnickyoung.minecraft.ribbits.module.ParticleTypeModule;
import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.fabric.api.client.particle.v1.ParticleProviderRegistry;
import net.fabricmc.fabric.api.client.rendering.v1.ModelLayerRegistry;
import net.minecraft.client.renderer.entity.EntityRenderers;

public class RibbitsFabricClient implements ClientModInitializer {
    @Override
    public void onInitializeClient() {
        RibbitsCommonClient.init();
        NetworkModuleFabric.registerClient();
        EntityRenderers.register(EntityTypeModule.RIBBIT.get(), RibbitRenderer::new);
        ModelLayerRegistry.registerModelLayer(SupporterHatModel.LAYER_LOCATION, SupporterHatModel::getTexturedModelData);

        ParticleProviderRegistry.getInstance().register(ParticleTypeModule.SPELL.get(), RibbitSpellParticle.Factory::new);
    }
}
