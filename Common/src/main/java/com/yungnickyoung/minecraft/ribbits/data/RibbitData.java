package com.yungnickyoung.minecraft.ribbits.data;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import com.yungnickyoung.minecraft.ribbits.entity.npc.RibbitProfession;
import com.yungnickyoung.minecraft.ribbits.entity.npc.RibbitUmbrellaType;
import com.yungnickyoung.minecraft.ribbits.module.RibbitProfessionModule;
import com.yungnickyoung.minecraft.ribbits.module.RibbitUmbrellaTypeModule;
import net.minecraft.network.FriendlyByteBuf;

/**
 * Defines data that determines profession, instrument, and umbrella used by all Ribbits.
 * Each Ribbit defines its own data randomly.
 */
public class RibbitData {
    public static final Codec<RibbitData> CODEC = RecordCodecBuilder.create(instance -> instance
            .group(
                    RibbitProfession.CODEC.fieldOf("profession").forGetter(data -> data.profession),
                    RibbitUmbrellaType.CODEC.fieldOf("umbrella_type").forGetter(data -> data.umbrellaType))
            .apply(instance, instance.stable(RibbitData::new)));

    private final RibbitProfession profession;
    private final RibbitUmbrellaType umbrellaType;

    public RibbitData(RibbitProfession profession, RibbitUmbrellaType umbrellaType) {
        this.profession = profession;
        this.umbrellaType = umbrellaType;
    }

    public RibbitData(FriendlyByteBuf buf) {
        this.profession = RibbitProfessionModule.getProfession(buf.readResourceLocation());
        this.umbrellaType = RibbitUmbrellaTypeModule.getUmbrellaType(buf.readResourceLocation());
    }

    public void write(FriendlyByteBuf buf) {
        buf.writeResourceLocation(this.getProfession().getId());
        buf.writeResourceLocation(this.getUmbrellaType().getId());
    }

    public RibbitProfession getProfession() {
        return this.profession;
    }

    public RibbitUmbrellaType getUmbrellaType() {
        return this.umbrellaType;
    }
}
