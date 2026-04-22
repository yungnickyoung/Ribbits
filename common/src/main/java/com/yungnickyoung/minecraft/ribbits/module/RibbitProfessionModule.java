package com.yungnickyoung.minecraft.ribbits.module;

import com.yungnickyoung.minecraft.ribbits.RibbitsCommon;
import com.yungnickyoung.minecraft.ribbits.data.RibbitProfession;
import com.yungnickyoung.minecraft.yungsapi.api.autoregister.AutoRegister;
import net.minecraft.resources.Identifier;
import org.jetbrains.annotations.Nullable;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Random;

@AutoRegister(RibbitsCommon.MOD_ID)
public class RibbitProfessionModule {
    /**
     * Map of all Ribbit profession Identifiers to their RibbitProfession objects.
     */
    private static final Map<Identifier, RibbitProfession> PROFESSION_REGISTRY = new HashMap<>();

    /* Registration of built-in RibbitProfessions. */
    public static final RibbitProfession NITWIT = register("nitwit", "nitwit_ribbit");
    public static final RibbitProfession GARDENER = register("gardener", "gardener_ribbit");
    public static final RibbitProfession SORCERER = register("sorcerer", "sorcerer_ribbit");
    public static final RibbitProfession FISHERMAN = register("fisherman", "fisherman_ribbit");
    public static final RibbitProfession MERCHANT = register("merchant", "merchant_ribbit");

    /**
     * Registers a RibbitProfession with the given name and model path.
     */
    public static RibbitProfession register(String name, String modelPath) {
        Identifier id = RibbitsCommon.id(name);
        RibbitProfession profession = new RibbitProfession(id, RibbitsCommon.id(modelPath));
        PROFESSION_REGISTRY.put(id, profession);
        return profession;
    }

    /**
     * Gets a RibbitProfession by its Identifier.
     *
     * @param id Identifier of the RibbitProfession to get.
     * @return RibbitProfession with the given Identifier, or null if not found.
     */
    public static @Nullable RibbitProfession getProfession(Identifier id) {
        return PROFESSION_REGISTRY.get(id);
    }

    /**
     * Gets a random RibbitProfession.
     *
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
