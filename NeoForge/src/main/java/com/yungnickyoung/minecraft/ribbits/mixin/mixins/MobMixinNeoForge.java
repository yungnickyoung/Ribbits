package com.yungnickyoung.minecraft.ribbits.mixin.mixins;

import com.yungnickyoung.minecraft.ribbits.data.RibbitProfession;
import com.yungnickyoung.minecraft.ribbits.entity.RibbitEntity;
import com.yungnickyoung.minecraft.ribbits.module.EntityTypeModule;
import com.yungnickyoung.minecraft.ribbits.module.ItemModule;
import com.yungnickyoung.minecraft.ribbits.module.RibbitProfessionModule;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.Mob;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(Mob.class)
public abstract class MobMixinNeoForge extends Entity {
    public MobMixinNeoForge(EntityType<?> entityType, Level level) {
        super(entityType, level);
    }

    /**
     * Allows for the correct spawn egg to be picked when a Ribbit is middle-clicked.
     */
    @Inject(method = "getPickResult", at = @At("HEAD"), cancellable = true)
    private void ribbits$pickCorrectSpawnEgg(CallbackInfoReturnable<ItemStack> cir) {
        if (this.getType() == EntityTypeModule.RIBBIT.get()) {
            RibbitProfession profession = asRibbit(this).getRibbitData().getProfession();
            if (profession == RibbitProfessionModule.FISHERMAN) {
                cir.setReturnValue(new ItemStack(ItemModule.RIBBIT_FISHERMAN_SPAWN_EGG.get()));
            } else if (profession == RibbitProfessionModule.GARDENER) {
                cir.setReturnValue(new ItemStack(ItemModule.RIBBIT_GARDENER_SPAWN_EGG.get()));
            } else if (profession == RibbitProfessionModule.MERCHANT) {
                cir.setReturnValue(new ItemStack(ItemModule.RIBBIT_MERCHANT_SPAWN_EGG.get()));
            } else if (profession == RibbitProfessionModule.SORCERER) {
                cir.setReturnValue(new ItemStack(ItemModule.RIBBIT_SORCERER_SPAWN_EGG.get()));
            } else {
                cir.setReturnValue(new ItemStack(ItemModule.RIBBIT_NITWIT_SPAWN_EGG.get()));
            }
        }
    }

    @Unique
    private RibbitEntity asRibbit(Object object) {
        return (RibbitEntity) object;
    }
}
