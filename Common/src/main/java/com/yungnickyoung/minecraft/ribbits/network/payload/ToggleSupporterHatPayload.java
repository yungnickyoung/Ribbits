package com.yungnickyoung.minecraft.ribbits.network.payload;

import com.yungnickyoung.minecraft.ribbits.RibbitsCommon;
import net.minecraft.core.UUIDUtil;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.network.codec.ByteBufCodecs;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;
import org.jetbrains.annotations.NotNull;

import java.util.UUID;

/**
 * Packet to toggle the supporter hat for the given player.
 * The client sends this payload to the server to toggle the supporter hat for the player, and the server then
 * forwards the payload to all clients to keep all clients in sync.
 *
 * @param playerUUID The UUID of the player to toggle the supporter hat for
 * @param enabled    Whether to enable or disable the supporter hat
 */
public record ToggleSupporterHatPayload(UUID playerUUID, boolean enabled) implements CustomPacketPayload {
    public static final CustomPacketPayload.Type<ToggleSupporterHatPayload> TYPE =
            new CustomPacketPayload.Type<>(RibbitsCommon.id("toggle_supporter_hat"));

    public static final StreamCodec<FriendlyByteBuf, ToggleSupporterHatPayload> STREAM_CODEC = StreamCodec.composite(
            UUIDUtil.STREAM_CODEC,
            ToggleSupporterHatPayload::playerUUID,
            ByteBufCodecs.BOOL,
            ToggleSupporterHatPayload::enabled,
            ToggleSupporterHatPayload::new);

    @Override
    public @NotNull Type<? extends CustomPacketPayload> type() {
        return TYPE;
    }
}
