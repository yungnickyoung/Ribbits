package com.yungnickyoung.minecraft.ribbits.network;

import net.minecraft.network.FriendlyByteBuf;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.fml.DistExecutor;
import net.minecraftforge.network.NetworkEvent;

import java.util.function.Supplier;

public class RibbitMusicS2CPacket {
    private final int ribbitId;
    private final int tickOffset;

    public RibbitMusicS2CPacket(int ribbitId, int tickOffset) {
        this.ribbitId = ribbitId;
        this.tickOffset = tickOffset;
    }

    /**
     * Decoder
     */
    public RibbitMusicS2CPacket(FriendlyByteBuf buf) {
        this.ribbitId = buf.readInt();
        this.tickOffset = buf.readInt();
    }

    /**
     * Encoder
     */
    public void toBytes(FriendlyByteBuf buf) {
        buf.writeInt(this.ribbitId);
        buf.writeInt(this.tickOffset);
    }

    /**
     * Handler
     */
    public boolean handle(Supplier<NetworkEvent.Context> ctx) {
        ctx.get().enqueueWork(() ->
                // Make sure this is only executed on the physical client
                DistExecutor.unsafeRunWhenOn(Dist.CLIENT, () -> () -> ClientPacketHandler.handleRibbitMusic(this, ctx))
        );
        ctx.get().setPacketHandled(true);
        return true;
    }

    public int getRibbitId() {
        return this.ribbitId;
    }

    public int getTickOffset() {
        return this.tickOffset;
    }
}
