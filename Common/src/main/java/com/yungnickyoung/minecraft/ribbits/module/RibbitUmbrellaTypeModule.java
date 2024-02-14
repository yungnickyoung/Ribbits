package com.yungnickyoung.minecraft.ribbits.module;

import com.yungnickyoung.minecraft.ribbits.RibbitsCommon;
import com.yungnickyoung.minecraft.ribbits.data.RibbitUmbrellaType;
import com.yungnickyoung.minecraft.yungsapi.api.autoregister.AutoRegister;
import net.minecraft.resources.ResourceLocation;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Random;

@AutoRegister(RibbitsCommon.MOD_ID)
public class RibbitUmbrellaTypeModule {
    /** Map of all RibbitUmbrellaType ResourceLocations to their RibbitUmbrellaType objects. */
    private static final Map<ResourceLocation, RibbitUmbrellaType> UMBRELLA_TYPE_REGISTRY = new HashMap<>();

    /* Registration of built-in RibbitUmbrellaTypes. */
    public static final RibbitUmbrellaType UMBRELLA_1 = register("umbrella_1", "geo/umbrella_ribbit_1.geo.json");
    public static final RibbitUmbrellaType UMBRELLA_2 = register("umbrella_2", "geo/umbrella_ribbit_2.geo.json");
    public static final RibbitUmbrellaType UMBRELLA_3 = register("umbrella_3", "geo/umbrella_ribbit_3.geo.json");

    /**
     * Registers a RibbitUmbrellaType with the given name and model path.
     */
    public static RibbitUmbrellaType register(String name, String modelPath) {
        ResourceLocation id = RibbitsCommon.id(name);
        RibbitUmbrellaType umbrellaType = new RibbitUmbrellaType(id, RibbitsCommon.id(modelPath));
        UMBRELLA_TYPE_REGISTRY.put(id, umbrellaType);
        return umbrellaType;
    }

    /**
     * Gets a RibbitUmbrellaType by its ResourceLocation.
     * @param id ResourceLocation of the RibbitUmbrellaType to get.
     * @return RibbitUmbrellaType with the given ResourceLocation, or null if not found.
     */
    public static RibbitUmbrellaType getUmbrellaType(ResourceLocation id) {
        return UMBRELLA_TYPE_REGISTRY.get(id);
    }

    /**
     * Gets a random RibbitUmbrellaType.
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
    @AutoRegister("_ignored")
    public static void initRibbitsUmbrellaTypes() {
        RibbitsCommon.LOGGER.info("Registering Ribbit umbrella types...");
    }
}
