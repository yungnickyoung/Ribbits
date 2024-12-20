package com.yungnickyoung.minecraft.ribbits.mixin.mixins.client.supporters;

import com.yungnickyoung.minecraft.ribbits.client.supporters.RibbitOptionsJSON;
import net.minecraft.client.Options;
import net.minecraft.client.gui.components.CycleButton;
import net.minecraft.client.gui.screens.OptionsSubScreen;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.client.gui.screens.SkinCustomizationScreen;
import net.minecraft.network.chat.Component;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.ModifyVariable;

@Mixin(value = SkinCustomizationScreen.class, priority = 1500)
public abstract class SkinCustomizationScreenMixin extends OptionsSubScreen {
    public SkinCustomizationScreenMixin(Screen lastScreen, Options options, Component titleComponent) {
        super(lastScreen, options, titleComponent);
    }

    @ModifyVariable(method = "init", ordinal = 0, at = @At(value = "INVOKE", target = "Lnet/minecraft/client/gui/screens/SkinCustomizationScreen;addRenderableWidget(Lnet/minecraft/client/gui/components/events/GuiEventListener;)Lnet/minecraft/client/gui/components/events/GuiEventListener;", ordinal = 1, shift = At.Shift.AFTER))
    public int ribbits$addSupporterHatToggleOption(int i) {
        ++i;
        this.addRenderableWidget(
                CycleButton.onOffBuilder(RibbitOptionsJSON.get().isSupporterHatEnabled())
                        .create(
                                this.width / 2 - 155 + i % 2 * 160,
                                this.height / 6 + 24 * (i >> 1),
                                150,
                                20,
                                Component.translatable("ribbits.options.supporter_hat_button"),
                                (button, enabled) -> RibbitOptionsJSON.get().setSupporterHatEnabled(enabled)
                        )
        );
        return i;
    }
}
