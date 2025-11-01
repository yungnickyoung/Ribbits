package com.yungnickyoung.minecraft.ribbits.network.payload;

import com.yungnickyoung.minecraft.ribbits.RibbitsCommon;
import net.minecraft.core.UUIDUtil;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;
import org.jetbrains.annotations.NotNull;

import java.util.UUID;

/**
 * Packet sent from the server to the client to stop playing music for a single Ribbit.
 *
 * @param ribbitUUID The UUID of the Ribbit to stop playing music for
 */
public record RibbitStopMusicSinglePayload(UUID ribbitUUID) implements CustomPacketPayload {
    public static final Type<RibbitStopMusicSinglePayload> TYPE =
            new Type<>(RibbitsCommon.id("ribbit_stop_music_single"));

    public static final StreamCodec<FriendlyByteBuf, RibbitStopMusicSinglePayload> STREAM_CODEC = StreamCodec.composite(
            UUIDUtil.STREAM_CODEC,
            RibbitStopMusicSinglePayload::ribbitUUID,
            RibbitStopMusicSinglePayload::new);

    @Override
    public @NotNull Type<? extends CustomPacketPayload> type() {
        return TYPE;
    }
}
