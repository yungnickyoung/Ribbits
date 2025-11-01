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
 * The client sends this payload to the server to toggle the supporter hat for the player.
 *
 * @param playerUUID The UUID of the player to toggle the supporter hat for
 * @param enabled    Whether to enable or disable the supporter hat
 */
public record ToggleSupporterHatPayloadC2S(UUID playerUUID, boolean enabled) implements CustomPacketPayload {
    public static final Type<ToggleSupporterHatPayloadC2S> TYPE =
            new Type<>(RibbitsCommon.id("toggle_supporter_hat_c2s"));

    public static final StreamCodec<FriendlyByteBuf, ToggleSupporterHatPayloadC2S> STREAM_CODEC = StreamCodec.composite(
            UUIDUtil.STREAM_CODEC,
            ToggleSupporterHatPayloadC2S::playerUUID,
            ByteBufCodecs.BOOL,
            ToggleSupporterHatPayloadC2S::enabled,
            ToggleSupporterHatPayloadC2S::new);

    @Override
    public @NotNull Type<? extends CustomPacketPayload> type() {
        return TYPE;
    }
}
