package com.yungnickyoung.minecraft.ribbits.network.payload;

import com.yungnickyoung.minecraft.ribbits.RibbitsCommon;
import net.minecraft.core.UUIDUtil;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.network.codec.ByteBufCodecs;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

/**
 * Packet sent from the server to the client to request the given client's supporter hat state.
 * Includes a list of all players with the supporter hat enabled on the server, to keep the client in sync.
 *
 * @param enabledSupporterHatPlayers A list of UUIDs of players on the server with the supporter hat enabled
 */
public record RequestSupporterHatStatePayload(List<UUID> enabledSupporterHatPlayers) implements CustomPacketPayload {
    public static final CustomPacketPayload.Type<RequestSupporterHatStatePayload> TYPE =
            new CustomPacketPayload.Type<>(RibbitsCommon.id("request_supporter_hat_state"));

    public static final StreamCodec<FriendlyByteBuf, RequestSupporterHatStatePayload> STREAM_CODEC = StreamCodec.composite(
            ByteBufCodecs.collection(
                    ArrayList::new,
                    UUIDUtil.STREAM_CODEC
            ),
            RequestSupporterHatStatePayload::enabledSupporterHatPlayers,
            RequestSupporterHatStatePayload::new);

    @Override
    public @NotNull Type<? extends CustomPacketPayload> type() {
        return TYPE;
    }
}
