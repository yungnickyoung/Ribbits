package com.yungnickyoung.minecraft.ribbits.module;

import com.yungnickyoung.minecraft.ribbits.RibbitsCommon;
import com.yungnickyoung.minecraft.ribbits.block.*;
import com.yungnickyoung.minecraft.ribbits.mixin.mixins.accessor.DoorBlockAccessor;
import com.yungnickyoung.minecraft.ribbits.platform.PlatformHelper;
import com.yungnickyoung.minecraft.ribbits.util.RegisterHelper;
import dev.architectury.registry.registries.DeferredRegister;
import dev.architectury.registry.registries.RegistrySupplier;
import net.minecraft.core.registries.Registries;
import net.minecraft.world.level.block.*;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.block.state.properties.BlockSetType;
import net.minecraft.world.level.block.state.properties.NoteBlockInstrument;
import net.minecraft.world.level.block.state.properties.WoodType;
import net.minecraft.world.level.material.MapColor;
import net.minecraft.world.level.material.PushReaction;

public class BlockModule {
    public static final DeferredRegister<Block> BLOCKS = DeferredRegister.create(RibbitsCommon.MOD_ID, Registries.BLOCK);

    public static final RegistrySupplier<Block> BROWN_TOADSTOOL = RegisterHelper.registerBlock(BLOCKS, "brown_toadstool",
            HugeMushroomBlock::new, properties -> properties
                    .mapColor(MapColor.DIRT)
                    .strength(0.2f)
                    .instrument(NoteBlockInstrument.BASS)
                    .ignitedByLava()
                    .sound(SoundType.WOOD));

    public static final RegistrySupplier<Block> RED_TOADSTOOL = RegisterHelper.registerBlock(BLOCKS, "red_toadstool",
            HugeMushroomBlock::new, properties -> properties
                    .mapColor(MapColor.COLOR_RED)
                    .strength(0.2f)
                    .instrument(NoteBlockInstrument.BASS)
                    .ignitedByLava()
                    .sound(SoundType.WOOD));

    public static final RegistrySupplier<Block> TOADSTOOL_STEM = RegisterHelper.registerBlock(BLOCKS, "toadstool_stem",
            HugeMushroomBlock::new, properties -> properties
                    .mapColor(MapColor.WOOL)
                    .strength(0.2f)
                    .instrument(NoteBlockInstrument.BASS)
                    .ignitedByLava()
                    .sound(SoundType.WOOD));

    public static final RegistrySupplier<Block> SWAMP_LANTERN = RegisterHelper.registerBlock(BLOCKS, "swamp_lantern",
            SwampLanternBlock::new, properties -> properties
                    .mapColor(MapColor.METAL)
                    .requiresCorrectToolForDrops()
                    .strength(2.0f)
                    .sound(SoundType.LANTERN)
                    .lightLevel(_ignored -> 15)
                    .noOcclusion()
                    .pushReaction(PushReaction.DESTROY));

    public static final RegistrySupplier<Block> GIANT_LILYPAD = RegisterHelper.registerBlock(BLOCKS, "giant_lilypad",
            GiantLilyPadBlock::new, properties -> properties
                    .mapColor(MapColor.PLANT)
                    .instabreak()
                    .sound(SoundType.LILY_PAD)
                    .noOcclusion()
                    .pushReaction(PushReaction.DESTROY));

    public static final RegistrySupplier<Block> SWAMP_DAISY = RegisterHelper.registerBlock(BLOCKS, "swamp_daisy",
            SwampDaisyBlock::new, properties -> properties
                    .mapColor(MapColor.PLANT)
                    .instabreak()
                    .noCollision()
                    .noOcclusion()
                    .sound(SoundType.BIG_DRIPLEAF)
                    .ignitedByLava());

    public static final RegistrySupplier<Block> TOADSTOOL = RegisterHelper.registerBlock(BLOCKS, "toadstool",
            ToadstoolBlock::new, properties -> properties
                    .mapColor(MapColor.PLANT)
                    .instabreak()
                    .noCollision()
                    .sound(SoundType.SMALL_DRIPLEAF)
                    .ignitedByLava());

    public static final RegistrySupplier<Block> UMBRELLA_LEAF = RegisterHelper.registerBlock(BLOCKS, "umbrella_leaf",
            UmbrellaLeafBlock::new, properties -> properties
                    .mapColor(MapColor.PLANT)
                    .instabreak()
                    .noCollision()
                    .ignitedByLava()
                    .sound(SoundType.SMALL_DRIPLEAF));

    public static final RegistrySupplier<Block> MOSSY_OAK_DOOR = BLOCKS.register("mossy_oak_door",
            () -> DoorBlockAccessor.createDoorBlock(
                    BlockSetType.OAK,
                    BlockBehaviour.Properties
                            .of()
                            .mapColor(Blocks.OAK_PLANKS.defaultMapColor())
                            .instrument(NoteBlockInstrument.BASS)
                            .strength(3.0f)
                            .noOcclusion()
                            .ignitedByLava()
                            .pushReaction(PushReaction.DESTROY)
                            .sound(SoundType.WOOD)
                            .setId(RegisterHelper.blockKey("mossy_oak_door"))));

    public static final MossyOakPlanksGroup MOSSY_OAK_PLANKS = new MossyOakPlanksGroup();

    public static void init() {
        MOSSY_OAK_PLANKS.register();
        BLOCKS.register();
        // Defer flammability setup to when blocks exist
        MOSSY_OAK_PLANKS.setupFlammability();

        MOSSY_OAK_DOOR.listen(block -> PlatformHelper.setBlockAsFlammable(block, 5, 20));
        UMBRELLA_LEAF.listen(block -> PlatformHelper.setBlockAsFlammable(block, 60, 100));
        SWAMP_DAISY.listen(block -> PlatformHelper.setBlockAsFlammable(block, 60, 100));
    }

    public static final class MossyOakPlanksGroup {
        private RegistrySupplier<Block> block;
        private RegistrySupplier<Block> slab;
        private RegistrySupplier<Block> stairs;
        private RegistrySupplier<Block> fence;
        private RegistrySupplier<Block> fenceGate;

        public void register() {
            block = RegisterHelper.registerBlock(BLOCKS, "mossy_oak_planks",
                    Block::new, properties -> properties
                            .mapColor(Blocks.OAK_PLANKS.defaultMapColor())
                            .instrument(NoteBlockInstrument.BASS)
                            .strength(2.0f, 3.0f)
                            .sound(SoundType.WOOD)
                            .ignitedByLava());

            slab = RegisterHelper.registerBlock(BLOCKS, "mossy_oak_planks_slab",
                    SlabBlock::new, properties -> properties
                            .mapColor(Blocks.OAK_PLANKS.defaultMapColor())
                            .instrument(NoteBlockInstrument.BASS)
                            .strength(2.0f, 3.0f)
                            .sound(SoundType.WOOD)
                            .ignitedByLava());

            stairs = BLOCKS.register("mossy_oak_planks_stairs",
                    () -> new StairBlock(block.get().defaultBlockState(), BlockBehaviour.Properties
                            .of()
                            .mapColor(Blocks.OAK_PLANKS.defaultMapColor())
                            .instrument(NoteBlockInstrument.BASS)
                            .strength(2.0f, 3.0f)
                            .sound(SoundType.WOOD)
                            .ignitedByLava()
                            .setId(RegisterHelper.blockKey("mossy_oak_planks_stairs"))));

            fence = RegisterHelper.registerBlock(BLOCKS, "mossy_oak_planks_fence",
                    FenceBlock::new, properties -> properties
                            .mapColor(Blocks.OAK_PLANKS.defaultMapColor())
                            .instrument(NoteBlockInstrument.BASS)
                            .strength(2.0f, 3.0f)
                            .sound(SoundType.WOOD)
                            .ignitedByLava());

            fenceGate = BLOCKS.register("mossy_oak_planks_fence_gate",
                    () -> new FenceGateBlock(
                            WoodType.OAK,
                            BlockBehaviour.Properties
                                    .of()
                                    .mapColor(Blocks.OAK_PLANKS.defaultMapColor())
                                    .instrument(NoteBlockInstrument.BASS)
                                    .strength(2.0f, 3.0f)
                                    .sound(SoundType.WOOD)
                                    .ignitedByLava()
                                    .setId(RegisterHelper.blockKey("mossy_oak_planks_fence_gate")))
            );
        }

        // Attach flammability listeners to avoid early .get()
        public void setupFlammability() {
            block.listen(b -> PlatformHelper.setBlockAsFlammable(b, 5, 20));
            slab.listen(b -> PlatformHelper.setBlockAsFlammable(b, 5, 20));
            stairs.listen(b -> PlatformHelper.setBlockAsFlammable(b, 5, 20));
            fence.listen(b -> PlatformHelper.setBlockAsFlammable(b, 5, 20));
            fenceGate.listen(b -> PlatformHelper.setBlockAsFlammable(b, 5, 20));
        }

        public Block get() {
            return block.get();
        }

        public Block getSlab() {
            return slab.get();
        }

        public Block getStairs() {
            return stairs.get();
        }

        public Block getFence() {
            return fence.get();
        }

        public Block getFenceGate() {
            return fenceGate.get();
        }
    }
}