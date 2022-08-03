package com.yungnickyoung.minecraft.ribbits.client;

import com.yungnickyoung.minecraft.ribbits.module.BlockModule;
import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.fabric.api.blockrenderlayer.v1.BlockRenderLayerMap;
import net.minecraft.client.renderer.RenderType;

public class RibbitsFabricClient implements ClientModInitializer {
    @Override
    public void onInitializeClient() {
        BlockRenderLayerMap.INSTANCE.putBlock(BlockModule.SWAMP_LANTERN.get(), RenderType.cutout());
        BlockRenderLayerMap.INSTANCE.putBlock(BlockModule.GIANT_LILYPAD.get(), RenderType.cutout());
        BlockRenderLayerMap.INSTANCE.putBlock(BlockModule.SWAMP_DAISY.get(), RenderType.cutout());
        BlockRenderLayerMap.INSTANCE.putBlock(BlockModule.TOADSTOOL.get(), RenderType.cutout());
        BlockRenderLayerMap.INSTANCE.putBlock(BlockModule.UMBRELLA_LEAF.get(), RenderType.cutout());
    }
}
