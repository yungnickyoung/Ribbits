package com.yungnickyoung.minecraft.ribbits.network.payload;

import com.yungnickyoung.minecraft.ribbits.RibbitsCommon;
import net.minecraft.core.UUIDUtil;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;
import org.jetbrains.annotations.NotNull;

import java.util.UUID;

/**
 * Packet sent from the server to the client to start playing Maraca sounds from a player.
 *
 * @param performerUUID The UUID of the player playing the Maraca.
 */
public record StartHearingMaracaPayload(UUID performerUUID) implements CustomPacketPayload {
    public static final Type<StartHearingMaracaPayload> TYPE =
            new Type<>(RibbitsCommon.id("start_hearing_maraca"));

    public static final StreamCodec<FriendlyByteBuf, StartHearingMaracaPayload> STREAM_CODEC = StreamCodec.composite(
            UUIDUtil.STREAM_CODEC,
            StartHearingMaracaPayload::performerUUID,
            StartHearingMaracaPayload::new);

    @Override
    public @NotNull Type<? extends CustomPacketPayload> type() {
        return TYPE;
    }
}
