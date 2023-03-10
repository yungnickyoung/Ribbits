package com.yungnickyoung.minecraft.ribbits.client.render;

import com.yungnickyoung.minecraft.ribbits.client.model.RibbitModel;
import com.yungnickyoung.minecraft.ribbits.entity.RibbitEntity;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.resources.ResourceLocation;
import software.bernie.geckolib3.model.AnimatedGeoModel;
import software.bernie.geckolib3.renderers.geo.GeoEntityRenderer;

public class RibbitRenderer extends GeoEntityRenderer<RibbitEntity> {

    public RibbitRenderer(EntityRendererProvider.Context renderManager) {
        super(renderManager, new RibbitModel());
    }

    @Override
    public ResourceLocation getTextureLocation(RibbitEntity entity) {
        return super.getTextureLocationGeckoLib(entity);
    }
}
