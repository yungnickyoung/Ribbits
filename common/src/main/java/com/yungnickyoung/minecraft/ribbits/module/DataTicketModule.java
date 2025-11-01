package com.yungnickyoung.minecraft.ribbits.module;

import com.yungnickyoung.minecraft.ribbits.data.RibbitData;
import software.bernie.geckolib.constant.dataticket.DataTicket;

public class DataTicketModule {
    public static final DataTicket<RibbitData> DT_RIBBIT_DATA =
            DataTicket.create("ribbit_data", RibbitData.class);
    public static final DataTicket<Boolean> DT_PLAYING_INSTRUMENT =
            DataTicket.create("ribbit_playing_instrument", Boolean.class);
    public static final DataTicket<Boolean> DT_UMBRELLA_FALLING =
            DataTicket.create("ribbit_umbrella_falling", Boolean.class);
    public static final DataTicket<Boolean> DT_IN_RAIN =
            DataTicket.create("ribbit_in_rain", Boolean.class);
    public static final DataTicket<Boolean> DT_IS_PRIDE_RIBBIT =
            DataTicket.create("ribbit_is_pride", Boolean.class);
}
