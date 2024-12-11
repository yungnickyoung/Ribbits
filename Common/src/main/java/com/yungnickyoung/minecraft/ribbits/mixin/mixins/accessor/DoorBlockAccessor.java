package com.yungnickyoung.minecraft.ribbits.mixin.mixins.accessor;

import net.minecraft.world.level.block.DoorBlock;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.block.state.properties.BlockSetType;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Invoker;

@Mixin(DoorBlock.class)
public interface DoorBlockAccessor {
    @Invoker("<init>")
    static DoorBlock createDoorBlock(BlockSetType $$0, BlockBehaviour.Properties $$1) {
        throw new UnsupportedOperationException();
    }
}
