package com.yungnickyoung.minecraft.ribbits.network.payload;

import com.yungnickyoung.minecraft.ribbits.RibbitsCommon;
import net.minecraft.core.UUIDUtil;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;
import org.jetbrains.annotations.NotNull;

import java.util.UUID;

/**
 * Packet sent from the server to the client to stop playing Maraca sounds from a player.
 *
 * @param performerUUID The UUID of the player playing the Maraca.
 */
public record StopHearingMaracaPayload(UUID performerUUID) implements CustomPacketPayload {
    public static final Type<StopHearingMaracaPayload> TYPE =
            new Type<>(RibbitsCommon.id("stop_hearing_maraca"));

    public static final StreamCodec<FriendlyByteBuf, StopHearingMaracaPayload> STREAM_CODEC = StreamCodec.composite(
            UUIDUtil.STREAM_CODEC,
            StopHearingMaracaPayload::performerUUID,
            StopHearingMaracaPayload::new);

    @Override
    public @NotNull Type<? extends CustomPacketPayload> type() {
        return TYPE;
    }
}
