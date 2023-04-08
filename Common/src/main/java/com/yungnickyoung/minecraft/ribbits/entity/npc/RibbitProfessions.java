package com.yungnickyoung.minecraft.ribbits.entity.npc;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Random;

public class RibbitProfessions {
    private static final Map<Integer, RibbitProfession> professions = new HashMap<>();

    public static void addProfession(int id, RibbitProfession profession) {
        professions.put(id, profession);
    }

    public static RibbitProfession getProfession(int id) {
        return professions.get(id);
    }

    public static RibbitProfession getRandomProfession() {
        Random random = new Random();
        List<RibbitProfession> professionList = professions.values().stream().toList();
        return professionList.get(random.nextInt(professionList.size()));
    }
}
