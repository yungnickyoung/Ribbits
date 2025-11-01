package com.yungnickyoung.minecraft.ribbits.module;

import com.yungnickyoung.minecraft.ribbits.RibbitsCommon;
import com.yungnickyoung.minecraft.ribbits.data.RibbitUmbrellaType;
import net.minecraft.resources.ResourceLocation;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Random;

public class RibbitUmbrellaTypeModule {
    /**
     * Map of all RibbitUmbrellaType ResourceLocations to their RibbitUmbrellaType objects.
     */
    private static final Map<ResourceLocation, RibbitUmbrellaType> UMBRELLA_TYPE_REGISTRY = new HashMap<>();

    /* Registration of built-in RibbitUmbrellaTypes. */
    public static final RibbitUmbrellaType UMBRELLA_1 = register("umbrella_1", "umbrella_1");
    public static final RibbitUmbrellaType UMBRELLA_2 = register("umbrella_2", "umbrella_2");
    public static final RibbitUmbrellaType UMBRELLA_3 = register("umbrella_3", "umbrella_3");

    /**
     * Registers a RibbitUmbrellaType with the given name and model path.
     */
    public static RibbitUmbrellaType register(String name, String modelPathSuffix) {
        ResourceLocation id = RibbitsCommon.id(name);
        RibbitUmbrellaType umbrellaType = new RibbitUmbrellaType(id, modelPathSuffix);
        UMBRELLA_TYPE_REGISTRY.put(id, umbrellaType);
        return umbrellaType;
    }

    /**
     * Gets a RibbitUmbrellaType by its ResourceLocation.
     *
     * @param id ResourceLocation of the RibbitUmbrellaType to get.
     * @return RibbitUmbrellaType with the given ResourceLocation, or null if not found.
     */
    public static RibbitUmbrellaType getUmbrellaType(ResourceLocation id) {
        return UMBRELLA_TYPE_REGISTRY.get(id);
    }

    /**
     * Gets a random RibbitUmbrellaType.
     *
     * @return Random RibbitUmbrellaType.
     */
    public static RibbitUmbrellaType getRandomUmbrellaType() {
        Random random = new Random();
        List<RibbitUmbrellaType> umbrellaTypeList = UMBRELLA_TYPE_REGISTRY.values().stream().toList();
        return umbrellaTypeList.get(random.nextInt(umbrellaTypeList.size()));
    }

    /**
     * The AutoRegister system will call this method after mod initialization is complete.
     * The method itself is a NO-OP, but calling it will trigger the static initialization above.
     */
    public static void init() {
        RibbitsCommon.LOGGER.info("Registering Ribbit umbrella types...");
    }
}
