package com.yungnickyoung.minecraft.ribbits.network;

import net.minecraft.network.FriendlyByteBuf;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.fml.DistExecutor;
import net.minecraftforge.event.network.CustomPayloadEvent;

import java.util.UUID;

public class PlayerMusicStartS2CPacket {
    private final UUID performerId;

    public PlayerMusicStartS2CPacket(UUID performerId) {
        this.performerId = performerId;
    }

    /**
     * Decoder
     */
    public PlayerMusicStartS2CPacket(FriendlyByteBuf buf) {
        this.performerId = buf.readUUID();
    }

    /**
     * Encoder
     */
    public void toBytes(FriendlyByteBuf buf) {
        buf.writeUUID(this.performerId);
    }

    /**
     * Handler
     */
    public boolean handle(CustomPayloadEvent.Context ctx) {
        ctx.enqueueWork(() ->
                // Make sure this is only executed on the physical client
                DistExecutor.unsafeRunWhenOn(Dist.CLIENT, () -> () -> ClientPacketHandlerForge.handleStartPlayerInstrument(this, ctx))
        );
        ctx.setPacketHandled(true);
        return true;
    }

    public UUID getPerformerId() {
        return this.performerId;
    }
}
