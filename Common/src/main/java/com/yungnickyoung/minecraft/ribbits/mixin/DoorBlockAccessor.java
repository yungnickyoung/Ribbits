package com.yungnickyoung.minecraft.ribbits.mixin;

import net.minecraft.world.level.block.DoorBlock;
import net.minecraft.world.level.block.state.BlockBehaviour;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Invoker;

@Mixin(DoorBlock.class)
public interface DoorBlockAccessor {
    @Invoker("<init>")
    static DoorBlock createDoorBlock(BlockBehaviour.Properties $$0) {
        throw new UnsupportedOperationException();
    }
}
