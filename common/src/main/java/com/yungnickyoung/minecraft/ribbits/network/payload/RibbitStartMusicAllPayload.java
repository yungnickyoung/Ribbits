package com.yungnickyoung.minecraft.ribbits.network.payload;

import com.yungnickyoung.minecraft.ribbits.RibbitsCommon;
import net.minecraft.core.UUIDUtil;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.network.codec.ByteBufCodecs;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;
import net.minecraft.resources.ResourceLocation;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

/**
 * Packet sent from the server to the client to start playing music for multiple Ribbits.
 *
 * @param ribbitUUIDs   The UUIDs of the Ribbits to start playing music for
 * @param instrumentIds The ResourceLocations of the instruments to play
 * @param tickOffset    The tick offset to start playing the music at
 */
public record RibbitStartMusicAllPayload(List<UUID> ribbitUUIDs,
                                         List<ResourceLocation> instrumentIds,
                                         int tickOffset) implements CustomPacketPayload {

    public static final Type<RibbitStartMusicAllPayload> TYPE =
            new Type<>(RibbitsCommon.id("ribbit_start_music_all"));

    public static final StreamCodec<FriendlyByteBuf, RibbitStartMusicAllPayload> STREAM_CODEC = StreamCodec.composite(
            ByteBufCodecs.collection(
                    ArrayList::new,
                    UUIDUtil.STREAM_CODEC
            ),
            RibbitStartMusicAllPayload::ribbitUUIDs,
            ByteBufCodecs.collection(
                    ArrayList::new,
                    ResourceLocation.STREAM_CODEC
            ),
            RibbitStartMusicAllPayload::instrumentIds,
            ByteBufCodecs.INT,
            RibbitStartMusicAllPayload::tickOffset,
            RibbitStartMusicAllPayload::new);

    @Override
    public @NotNull Type<? extends CustomPacketPayload> type() {
        return TYPE;
    }
}
