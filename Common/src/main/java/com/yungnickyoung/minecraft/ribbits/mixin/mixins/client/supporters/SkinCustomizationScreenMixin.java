package com.yungnickyoung.minecraft.ribbits.mixin.mixins.client.supporters;

import com.yungnickyoung.minecraft.ribbits.client.supporters.RibbitOptionsJSON;
import net.minecraft.client.Options;
import net.minecraft.client.gui.components.AbstractWidget;
import net.minecraft.client.gui.components.CycleButton;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.client.gui.screens.options.OptionsSubScreen;
import net.minecraft.client.gui.screens.options.SkinCustomizationScreen;
import net.minecraft.network.chat.Component;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.LocalCapture;

import java.util.List;

@Mixin(value = SkinCustomizationScreen.class, priority = 1500)
public abstract class SkinCustomizationScreenMixin extends OptionsSubScreen {
    public SkinCustomizationScreenMixin(Screen $$0, Options $$1, Component $$2) {
        super($$0, $$1, $$2);
    }

    @Inject(
            method = "addOptions",
            at = @At(
                    target = "Ljava/util/List;add(Ljava/lang/Object;)Z",
                    value = "INVOKE",
                    shift = At.Shift.AFTER),
            locals = LocalCapture.CAPTURE_FAILSOFT)
    public void ribbits$addSupporterHatToggleOption(CallbackInfo ci, List<AbstractWidget> list) {
        list.add(
                CycleButton.onOffBuilder(RibbitOptionsJSON.get().isSupporterHatEnabled())
                        .create(
                                Component.translatable("ribbits.options.supporter_hat_button"),
                                (button, enabled) -> RibbitOptionsJSON.get().setSupporterHatEnabled(enabled)
                        )
        );
    }
}
