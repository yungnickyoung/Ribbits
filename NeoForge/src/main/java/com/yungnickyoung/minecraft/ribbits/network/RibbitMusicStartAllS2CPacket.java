package com.yungnickyoung.minecraft.ribbits.network;

import com.yungnickyoung.minecraft.ribbits.entity.RibbitEntity;
import com.yungnickyoung.minecraft.ribbits.util.BufferUtils;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.resources.ResourceLocation;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.fml.DistExecutor;
import net.minecraftforge.network.NetworkEvent;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
import java.util.function.Supplier;

public class RibbitMusicStartAllS2CPacket {
    private final List<UUID> ribbitIds;
    private final List<ResourceLocation> instrumentIds;
    private final int tickOffset;

    public RibbitMusicStartAllS2CPacket(RibbitEntity masterRibbit, int tickOffset) {
        List<RibbitEntity> ribbitsPlayingMusic = masterRibbit.getRibbitsPlayingMusic().stream().toList();

        List<UUID> ribbitIds = new ArrayList<>();
        ribbitIds.add(masterRibbit.getUUID());
        ribbitIds.addAll(ribbitsPlayingMusic.stream().map(RibbitEntity::getUUID).toList());

        List<ResourceLocation> instrumentIds = new ArrayList<>();
        instrumentIds.add(masterRibbit.getRibbitData().getInstrument().getId());
        instrumentIds.addAll(ribbitsPlayingMusic.stream().map((ribbit) -> ribbit.getRibbitData().getInstrument().getId()).toList());

        this.ribbitIds = ribbitIds;
        this.instrumentIds = instrumentIds;
        this.tickOffset = tickOffset;
    }

    /**
     * Decoder
     */
    public RibbitMusicStartAllS2CPacket(FriendlyByteBuf buf) {
        this.ribbitIds = BufferUtils.readUUIDList(buf);
        this.instrumentIds = BufferUtils.readResourceLocationList(buf);
        this.tickOffset = buf.readInt();
    }

    /**
     * Encoder
     */
    public void toBytes(FriendlyByteBuf buf) {
        BufferUtils.writeUUIDList(this.ribbitIds, buf);
        BufferUtils.writeResourceLocationList(this.instrumentIds, buf);
        buf.writeInt(tickOffset);
    }

    /**
     * Handler
     */
    public boolean handle(Supplier<NetworkEvent.Context> ctx) {
        ctx.get().enqueueWork(() ->
                // Make sure this is only executed on the physical client
                DistExecutor.unsafeRunWhenOn(Dist.CLIENT, () -> () -> ClientPacketHandlerForge.handleStartAllRibbitInstruments(this, ctx))
        );
        ctx.get().setPacketHandled(true);
        return true;
    }

    public List<UUID> getRibbitIds() {
        return this.ribbitIds;
    }

    public List<ResourceLocation> getInstrumentIds() {
        return this.instrumentIds;
    }

    public int getTickOffset() {
        return this.tickOffset;
    }
}
