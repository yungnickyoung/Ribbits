package com.yungnickyoung.minecraft.ribbits.client.render;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.math.Axis;
import net.minecraft.client.Minecraft;
import net.minecraft.util.Mth;
import net.minecraft.world.entity.HumanoidArm;
import net.minecraft.world.item.ItemStack;

public class MaracaInHandRenderer {

    public static void applyMaracaTransform(PoseStack poseStack, float partialTick, HumanoidArm arm, ItemStack handItem, float equipProgress) {
        // Constants
        final int timerSize = 8;
        final float rotAmp = 7.5F;
        final float translationAmp = 0.15F;

        // Calculate the timer
        float tickModulo = (float) (Minecraft.getInstance().player.getUseItemRemainingTicks() % timerSize); // 0 to timerSize - 1
        float tickTimer = tickModulo - partialTick + 1.0F; // Smooth timer from 0 to timerSize
        float timerNormalized = tickTimer / (float) timerSize; // Smooth timer from 0 to 1
        float timerAngle = (1.0F - timerNormalized) * 2.0F * (float) Math.PI; // Multiply by 2pi so the modulo timer loops

        // Calculate the rotation and movement
        float xRot = rotAmp * Mth.cos(timerAngle);
        float movement = translationAmp * Mth.cos(timerAngle);

        // Apply the transformation
        if (arm == HumanoidArm.RIGHT) {
            poseStack.translate(-.25, .1, .25F);
        } else {
            poseStack.translate(.25, .1, .25F);
        }
        poseStack.translate(0.0, 0.0, movement);
        poseStack.mulPose(Axis.XP.rotationDegrees(xRot));
    }
}
