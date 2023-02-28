package com.yungnickyoung.minecraft.ribbits.client.render;

import com.mojang.blaze3d.vertex.PoseStack;
import com.yungnickyoung.minecraft.ribbits.client.model.FrostMinerModel;
import com.yungnickyoung.minecraft.ribbits.entity.FrostMinerEntity;
import net.minecraft.client.renderer.block.model.ItemTransforms;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.block.state.BlockState;
import software.bernie.geckolib3.core.processor.IBone;
import software.bernie.geckolib3.geo.render.built.GeoBone;
import software.bernie.geckolib3.renderers.geo.ExtendedGeoEntityRenderer;

public class FrostMinerRenderer extends ExtendedGeoEntityRenderer<FrostMinerEntity> {

    public FrostMinerRenderer(EntityRendererProvider.Context renderManager) {
        super(renderManager, new FrostMinerModel());
    }

    @Override
    public ResourceLocation getTextureLocation(FrostMinerEntity entity) {
        return super.getTextureLocationGeckoLib(entity);
    }

    @Override
    protected boolean isArmorBone(GeoBone bone) {
        return false;
    }

    @Override
    protected ResourceLocation getTextureForBone(String boneName, FrostMinerEntity currentEntity) {
        return null;
    }

    @Override
    protected ItemStack getHeldItemForBone(String boneName, FrostMinerEntity currentEntity) {
        return null;
    }

    @Override
    protected ItemTransforms.TransformType getCameraTransformForItemAtBone(ItemStack boneItem, String boneName) {
        return null;
    }

    @Override
    protected BlockState getHeldBlockForBone(String boneName, FrostMinerEntity currentEntity) {
        return null;
    }

    @Override
    protected void preRenderItem(PoseStack PoseStack, ItemStack item, String boneName, FrostMinerEntity currentEntity, IBone bone) {

    }

    @Override
    protected void preRenderBlock(PoseStack PoseStack, BlockState block, String boneName, FrostMinerEntity currentEntity) {

    }

    @Override
    protected void postRenderItem(PoseStack PoseStack, ItemStack item, String boneName, FrostMinerEntity currentEntity, IBone bone) {

    }

    @Override
    protected void postRenderBlock(PoseStack PoseStack, BlockState block, String boneName, FrostMinerEntity currentEntity) {

    }
}
