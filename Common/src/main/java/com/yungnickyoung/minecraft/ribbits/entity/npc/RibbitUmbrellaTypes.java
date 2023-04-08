package com.yungnickyoung.minecraft.ribbits.entity.npc;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Random;

public class RibbitUmbrellaTypes {
    private static final Map<Integer, RibbitUmbrellaType> umbrellaTypes = new HashMap<>();

    public static void addUmbrellaType(int id, RibbitUmbrellaType profession) {
        umbrellaTypes.put(id, profession);
    }

    public static RibbitUmbrellaType getUmbrellaType(int id) {
        return umbrellaTypes.get(id);
    }

    public static RibbitUmbrellaType getRandomUmbrellaType() {
        Random random = new Random();
        List<RibbitUmbrellaType> umbrellaTypeList = umbrellaTypes.values().stream().toList();
        return umbrellaTypeList.get(random.nextInt(umbrellaTypeList.size()));
    }
}
