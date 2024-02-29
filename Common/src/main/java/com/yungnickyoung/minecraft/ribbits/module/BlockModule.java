package com.yungnickyoung.minecraft.ribbits.module;

import com.yungnickyoung.minecraft.ribbits.RibbitsCommon;
import com.yungnickyoung.minecraft.ribbits.block.SwampDaisyBlock;
import com.yungnickyoung.minecraft.ribbits.block.SwampLanternBlock;
import com.yungnickyoung.minecraft.ribbits.block.ToadstoolBlock;
import com.yungnickyoung.minecraft.ribbits.block.UmbrellaLeafBlock;
import com.yungnickyoung.minecraft.ribbits.mixin.mixins.accessor.DoorBlockAccessor;
import com.yungnickyoung.minecraft.ribbits.services.Services;
import com.yungnickyoung.minecraft.yungsapi.api.autoregister.AutoRegister;
import com.yungnickyoung.minecraft.yungsapi.api.autoregister.AutoRegisterBlock;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.HugeMushroomBlock;
import net.minecraft.world.level.block.SoundType;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.block.state.properties.BlockSetType;
import net.minecraft.world.level.block.state.properties.NoteBlockInstrument;
import net.minecraft.world.level.block.state.properties.WoodType;
import net.minecraft.world.level.material.MapColor;
import net.minecraft.world.level.material.PushReaction;

@AutoRegister(RibbitsCommon.MOD_ID)
public class BlockModule {
    @AutoRegister("brown_toadstool")
    public static final AutoRegisterBlock BROWN_TOADSTOOL = AutoRegisterBlock.of(() -> new HugeMushroomBlock(
                    BlockBehaviour.Properties
                            .of()
                            .mapColor(MapColor.DIRT)
                            .strength(0.2f)
                            .instrument(NoteBlockInstrument.BASS)
                            .sound(SoundType.WOOD)))
            .withItem(Item.Properties::new);

    @AutoRegister("red_toadstool")
    public static final AutoRegisterBlock RED_TOADSTOOL = AutoRegisterBlock.of(() -> new HugeMushroomBlock(
                    BlockBehaviour.Properties
                            .of()
                            .mapColor(MapColor.COLOR_RED)
                            .strength(0.2f)
                            .instrument(NoteBlockInstrument.BASS)
                            .sound(SoundType.WOOD)))
            .withItem(Item.Properties::new);

    @AutoRegister("toadstool_stem")
    public static final AutoRegisterBlock TOADSTOOL_STEM = AutoRegisterBlock.of(() -> new HugeMushroomBlock(
                    BlockBehaviour.Properties
                            .of()
                            .mapColor(MapColor.WOOL)
                            .strength(0.2f)
                            .instrument(NoteBlockInstrument.BASS)
                            .sound(SoundType.WOOD)))
            .withItem(Item.Properties::new);

    @AutoRegister("swamp_lantern")
    public static final AutoRegisterBlock SWAMP_LANTERN = AutoRegisterBlock.of(() -> new SwampLanternBlock(
                    BlockBehaviour.Properties
                            .of()
                            .mapColor(MapColor.METAL)
                            .requiresCorrectToolForDrops()
                            .strength(2.0f)
                            .sound(SoundType.LANTERN)
                            .lightLevel(_ignored -> 15)
                            .noOcclusion()
                            .pushReaction(PushReaction.DESTROY)))
            .withItem(Item.Properties::new);

    @AutoRegister("giant_lilypad")
    public static final AutoRegisterBlock GIANT_LILYPAD = AutoRegisterBlock.of(Services.PLATFORM.getGiantLilyPadBlock());

    @AutoRegister("swamp_daisy")
    public static final AutoRegisterBlock SWAMP_DAISY = AutoRegisterBlock.of(() -> new SwampDaisyBlock(
                    BlockBehaviour.Properties
                            .of()
                            .mapColor(MapColor.PLANT)
                            .instabreak()
                            .noCollission()
                            .noOcclusion()
                            .sound(SoundType.BIG_DRIPLEAF)
                            .ignitedByLava()))
            .withItem(Item.Properties::new);

    @AutoRegister("toadstool")
    public static final AutoRegisterBlock TOADSTOOL = AutoRegisterBlock.of(() -> new ToadstoolBlock(
                    BlockBehaviour.Properties
                            .of()
                            .mapColor(MapColor.PLANT)
                            .instabreak()
                            .noCollission()
                            .sound(SoundType.SMALL_DRIPLEAF)
                            .ignitedByLava()))
            .withItem(Item.Properties::new);

    @AutoRegister("mossy_oak_planks")
    public static final AutoRegisterBlock MOSSY_OAK_PLANKS = AutoRegisterBlock.of(() -> new Block(
                    BlockBehaviour.Properties
                            .of()
                            .mapColor(Blocks.OAK_PLANKS.defaultMapColor())
                            .instrument(NoteBlockInstrument.BASS)
                            .strength(2.0f, 3.0f)
                            .sound(SoundType.WOOD)
                            .ignitedByLava()))
            .withItem(Item.Properties::new)
            .withSlab()
            .withStairs()
            .withFence()
            .withFenceGate(WoodType.OAK);

    @AutoRegister("mossy_oak_door")
    public static final AutoRegisterBlock MOSSY_OAK_DOOR = AutoRegisterBlock.of(() ->
                    DoorBlockAccessor.createDoorBlock(BlockBehaviour.Properties
                                    .of()
                                    .mapColor(Blocks.OAK_PLANKS.defaultMapColor())
                                    .instrument(NoteBlockInstrument.BASS)
                                    .strength(3.0f)
                                    .noOcclusion()
                                    .ignitedByLava()
                                    .pushReaction(PushReaction.DESTROY)
                                    .sound(SoundType.WOOD),
                            BlockSetType.OAK))
            .withItem(Item.Properties::new);

    @AutoRegister("umbrella_leaf")
    public static final AutoRegisterBlock UMBRELLA_LEAF = AutoRegisterBlock.of(() -> new UmbrellaLeafBlock(
                    BlockBehaviour.Properties
                            .of()
                            .mapColor(MapColor.PLANT)
                            .instabreak()
                            .noCollission()
                            .ignitedByLava()
                            .sound(SoundType.SMALL_DRIPLEAF)))
            .withItem(Item.Properties::new);
}
