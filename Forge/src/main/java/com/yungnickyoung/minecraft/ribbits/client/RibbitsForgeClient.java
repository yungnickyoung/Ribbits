package com.yungnickyoung.minecraft.ribbits.client;

import com.yungnickyoung.minecraft.ribbits.module.BlockModule;
import net.minecraft.client.renderer.ItemBlockRenderTypes;
import net.minecraft.client.renderer.RenderType;
import net.minecraftforge.fml.event.lifecycle.FMLClientSetupEvent;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;

public class RibbitsForgeClient {
    public static void init() {
        FMLJavaModLoadingContext.get().getModEventBus().addListener(RibbitsForgeClient::clientSetup);
    }

    private static void clientSetup(final FMLClientSetupEvent event) {
        ItemBlockRenderTypes.setRenderLayer(BlockModule.SWAMP_LANTERN.get(), RenderType.cutout());
        ItemBlockRenderTypes.setRenderLayer(BlockModule.GIANT_LILYPAD.get(), RenderType.cutout());
        ItemBlockRenderTypes.setRenderLayer(BlockModule.SWAMP_DAISY.get(), RenderType.cutout());
        ItemBlockRenderTypes.setRenderLayer(BlockModule.TOADSTOOL.get(), RenderType.cutout());
        ItemBlockRenderTypes.setRenderLayer(BlockModule.UMBRELLA_LEAF.get(), RenderType.cutout());
    }
}
