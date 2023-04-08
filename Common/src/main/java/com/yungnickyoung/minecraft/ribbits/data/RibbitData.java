package com.yungnickyoung.minecraft.ribbits.data;

import com.yungnickyoung.minecraft.ribbits.entity.npc.RibbitProfession;
import com.yungnickyoung.minecraft.ribbits.entity.npc.RibbitUmbrellaType;

/**
 * Defines data that determines profession, instrument, and umbrella used by all Ribbits.
 * Each Ribbit defines its own data randomly.
 */
public class RibbitData {
    private final RibbitProfession profession;
    private final RibbitUmbrellaType umbrellaType;

    public RibbitData(RibbitProfession profession, RibbitUmbrellaType umbrellaType) {
        this.profession = profession;
        this.umbrellaType = umbrellaType;
    }

    public RibbitProfession getProfession() {
        return this.profession;
    }

    public RibbitUmbrellaType getUmbrellaType() {
        return this.umbrellaType;
    }

    public RibbitData setProfession(RibbitProfession profession) {
        return new RibbitData(profession, this.umbrellaType);
    }

    public RibbitData setUmbrellaType(RibbitUmbrellaType umbrellaType) {
        return new RibbitData(this.profession, umbrellaType);
    }

}
