package com.yungnickyoung.minecraft.ribbits.network;

import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.resources.ResourceLocation;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.fml.DistExecutor;
import net.minecraftforge.event.network.CustomPayloadEvent;

import java.util.UUID;

public class RibbitMusicStartSingleS2CPacket {
    private final UUID ribbitId;
    private final ResourceLocation instrumentId;
    private final int tickOffset;

    public RibbitMusicStartSingleS2CPacket(UUID ribbitId, ResourceLocation instrumentId, int tickOffset) {
        this.ribbitId = ribbitId;
        this.instrumentId = instrumentId;
        this.tickOffset = tickOffset;
    }

    /**
     * Decoder
     */
    public RibbitMusicStartSingleS2CPacket(FriendlyByteBuf buf) {
        this.ribbitId = buf.readUUID();
        this.instrumentId = buf.readResourceLocation();
        this.tickOffset = buf.readInt();
    }

    /**
     * Encoder
     */
    public void toBytes(FriendlyByteBuf buf) {
        buf.writeUUID(this.ribbitId);
        buf.writeResourceLocation(this.instrumentId);
        buf.writeInt(this.tickOffset);
    }

    /**
     * Handler
     */
    public boolean handle(CustomPayloadEvent.Context ctx) {
        ctx.enqueueWork(() ->
                // Make sure this is only executed on the physical client
                DistExecutor.unsafeRunWhenOn(Dist.CLIENT, () -> () -> ClientPacketHandlerForge.handleStartSingleRibbitInstrument(this, ctx))
        );
        ctx.setPacketHandled(true);
        return true;
    }

    public UUID getRibbitId() {
        return this.ribbitId;
    }

    public ResourceLocation getInstrumentId() {
        return this.instrumentId;
    }

    public int getTickOffset() {
        return this.tickOffset;
    }
}
