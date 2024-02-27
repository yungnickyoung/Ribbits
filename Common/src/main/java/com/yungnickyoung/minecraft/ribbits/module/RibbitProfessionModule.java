package com.yungnickyoung.minecraft.ribbits.module;

import com.yungnickyoung.minecraft.ribbits.RibbitsCommon;
import com.yungnickyoung.minecraft.ribbits.data.RibbitProfession;
import com.yungnickyoung.minecraft.yungsapi.api.autoregister.AutoRegister;
import net.minecraft.resources.ResourceLocation;

import javax.annotation.Nullable;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Random;

@AutoRegister(RibbitsCommon.MOD_ID)
public class RibbitProfessionModule {
    /** Map of all Ribbit profession ResourceLocations to their RibbitProfession objects. */
    private static final Map<ResourceLocation, RibbitProfession> PROFESSION_REGISTRY = new HashMap<>();

    /* Registration of built-in RibbitProfessions. */
    public static final RibbitProfession NITWIT = register("nitwit", "geo/nitwit_ribbit.geo.json");
    public static final RibbitProfession GARDENER = register("gardener", "geo/gardener_ribbit.geo.json");
    public static final RibbitProfession SORCERER = register("sorcerer", "geo/sorcerer_ribbit.geo.json");
    public static final RibbitProfession FISHERMAN = register("fisherman", "geo/fisherman_ribbit.geo.json");
    public static final RibbitProfession MERCHANT = register("merchant", "geo/merchant_ribbit.geo.json");

    /**
     * Registers a RibbitProfession with the given name and model path.
     */
    public static RibbitProfession register(String name, String modelPath) {
        ResourceLocation id = RibbitsCommon.id(name);
        RibbitProfession profession = new RibbitProfession(id, RibbitsCommon.id(modelPath));
        PROFESSION_REGISTRY.put(id, profession);
        return profession;
    }

    /**
     * Gets a RibbitProfession by its ResourceLocation.
     * @param id ResourceLocation of the RibbitProfession to get.
     * @return RibbitProfession with the given ResourceLocation, or null if not found.
     */
    public static @Nullable RibbitProfession getProfession(ResourceLocation id) {
        return PROFESSION_REGISTRY.get(id);
    }

    /**
     * Gets a random RibbitProfession.
     * @return Random RibbitProfession.
     */
    public static RibbitProfession getRandomProfession() {
        Random random = new Random();
        List<RibbitProfession> professionList = PROFESSION_REGISTRY.values().stream().toList();
        return professionList.get(random.nextInt(professionList.size()));
    }

    /**
     * The AutoRegister system will call this method after mod initialization is complete.
     * The method itself is a NO-OP, but calling it will trigger the static initialization above.
     */
    @AutoRegister("_ignored")
    public static void initRibbitsProfessions() {
        RibbitsCommon.LOGGER.info("Registering Ribbit professions...");
    }
}
