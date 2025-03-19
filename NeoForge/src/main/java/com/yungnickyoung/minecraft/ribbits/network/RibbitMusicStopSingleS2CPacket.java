package com.yungnickyoung.minecraft.ribbits.network;

import net.minecraft.network.FriendlyByteBuf;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.fml.DistExecutor;
import net.minecraftforge.network.NetworkEvent;

import java.util.UUID;
import java.util.function.Supplier;

public class RibbitMusicStopSingleS2CPacket {
    private final UUID ribbitId;

    public RibbitMusicStopSingleS2CPacket(UUID ribbitId) {
        this.ribbitId = ribbitId;
    }

    /**
     * Decoder
     */
    public RibbitMusicStopSingleS2CPacket(FriendlyByteBuf buf) {
        this.ribbitId = buf.readUUID();
    }

    /**
     * Encoder
     */
    public void toBytes(FriendlyByteBuf buf) {
        buf.writeUUID(this.ribbitId);
    }

    /**
     * Handler
     */
    public boolean handle(Supplier<NetworkEvent.Context> ctx) {
        ctx.get().enqueueWork(() ->
                // Make sure this is only executed on the physical client
                DistExecutor.unsafeRunWhenOn(Dist.CLIENT, () -> () -> ClientPacketHandlerForge.handleStopSingleRibbitInstrument(this, ctx))
        );
        ctx.get().setPacketHandled(true);
        return true;
    }

    public UUID getRibbitId() {
        return this.ribbitId;
    }
}
