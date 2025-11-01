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
 * The server forwards this payload to all clients to keep all clients in sync.
 *
 * @param playerUUID The UUID of the player to toggle the supporter hat for
 * @param enabled    Whether to enable or disable the supporter hat
 */
public record ToggleSupporterHatPayloadS2C(UUID playerUUID, boolean enabled) implements CustomPacketPayload {
    public static final Type<ToggleSupporterHatPayloadS2C> TYPE =
            new Type<>(RibbitsCommon.id("toggle_supporter_hat_s2c"));

    public static final StreamCodec<FriendlyByteBuf, ToggleSupporterHatPayloadS2C> STREAM_CODEC = StreamCodec.composite(
            UUIDUtil.STREAM_CODEC,
            ToggleSupporterHatPayloadS2C::playerUUID,
            ByteBufCodecs.BOOL,
            ToggleSupporterHatPayloadS2C::enabled,
            ToggleSupporterHatPayloadS2C::new);

    @Override
    public @NotNull Type<? extends CustomPacketPayload> type() {
        return TYPE;
    }
}
