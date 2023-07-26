package com.yungnickyoung.minecraft.ribbits.module;

import com.yungnickyoung.minecraft.ribbits.RibbitsCommon;
import com.yungnickyoung.minecraft.ribbits.entity.npc.RibbitProfession;
import com.yungnickyoung.minecraft.ribbits.entity.npc.RibbitProfessions;
import com.yungnickyoung.minecraft.ribbits.entity.npc.RibbitUmbrellaType;
import com.yungnickyoung.minecraft.ribbits.entity.npc.RibbitUmbrellaTypes;
import com.yungnickyoung.minecraft.yungsapi.api.autoregister.AutoRegister;

@AutoRegister(RibbitsCommon.MOD_ID)
public class RibbitsModule {
    /**
     * The AutoRegister system will call this method after mod initialization is complete.
     * For Fabric, this simply occurs at the end of init, after everything has been registered.
     * For Forge, this is during CommonSetup.
     * <p>
     * Note that the AutoRegister annotation's value is ignored, hence the dummy value.
     */
    @AutoRegister("_ignored")
    public static void postInit() {
        RibbitsCommon.LOGGER.info("Registering Ribbit professions and umbrella types...");

        RibbitProfessions.addProfession(RibbitProfession.SORCERER.getId(), RibbitProfession.SORCERER);
        RibbitProfessions.addProfession(RibbitProfession.GARDENER.getId(), RibbitProfession.GARDENER);
        RibbitProfessions.addProfession(RibbitProfession.BASSIST.getId(), RibbitProfession.BASSIST);
        RibbitProfessions.addProfession(RibbitProfession.BONGOIST.getId(), RibbitProfession.BONGOIST);
        RibbitProfessions.addProfession(RibbitProfession.FLAUTIST.getId(), RibbitProfession.FLAUTIST);
        RibbitProfessions.addProfession(RibbitProfession.GUITARIST.getId(), RibbitProfession.GUITARIST);

        RibbitUmbrellaTypes.addUmbrellaType(RibbitUmbrellaType.UMBRELLA_1.getId(), RibbitUmbrellaType.UMBRELLA_1);
        RibbitUmbrellaTypes.addUmbrellaType(RibbitUmbrellaType.UMBRELLA_2.getId(), RibbitUmbrellaType.UMBRELLA_2);
        RibbitUmbrellaTypes.addUmbrellaType(RibbitUmbrellaType.UMBRELLA_3.getId(), RibbitUmbrellaType.UMBRELLA_3);
    }
}
