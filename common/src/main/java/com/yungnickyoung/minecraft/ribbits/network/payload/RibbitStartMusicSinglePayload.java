package com.yungnickyoung.minecraft.ribbits.network.payload;

import com.yungnickyoung.minecraft.ribbits.RibbitsCommon;
import net.minecraft.core.UUIDUtil;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.network.codec.ByteBufCodecs;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;
import net.minecraft.resources.Identifier;
import org.jetbrains.annotations.NotNull;

import java.util.UUID;

/**
 * Packet sent from the server to the client to start playing music for a single Ribbit.
 *
 * @param ribbitUUID   The UUID of the Ribbit to start playing music for
 * @param instrumentId The Identifier of the instrument to play
 * @param tickOffset   The tick offset to start playing the music at
 */
public record RibbitStartMusicSinglePayload(UUID ribbitUUID,
                                            Identifier instrumentId,
                                            int tickOffset) implements CustomPacketPayload {

    public static final Type<RibbitStartMusicSinglePayload> TYPE =
            new Type<>(RibbitsCommon.id("ribbit_start_music_single"));

    public static final StreamCodec<FriendlyByteBuf, RibbitStartMusicSinglePayload> STREAM_CODEC = StreamCodec.composite(
            UUIDUtil.STREAM_CODEC,
            RibbitStartMusicSinglePayload::ribbitUUID,
            Identifier.STREAM_CODEC,
            RibbitStartMusicSinglePayload::instrumentId,
            ByteBufCodecs.INT,
            RibbitStartMusicSinglePayload::tickOffset,
            RibbitStartMusicSinglePayload::new);

    @Override
    public @NotNull Type<? extends CustomPacketPayload> type() {
        return TYPE;
    }
}
