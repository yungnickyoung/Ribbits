package com.yungnickyoung.minecraft.ribbits.network;

import com.yungnickyoung.minecraft.ribbits.entity.RibbitEntity;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.fml.DistExecutor;
import net.minecraftforge.network.NetworkEvent;

import java.util.function.Supplier;

public class RibbitMusicS2CPacket {
    private int ribbitId;

    public RibbitMusicS2CPacket(int ribbitId) {
        this.ribbitId = ribbitId;
    }

    /**
     * Decoder
     */
    public RibbitMusicS2CPacket(FriendlyByteBuf buf) {
        this.ribbitId = buf.readInt();
    }

    /**
     * Encoder
     */
    public void toBytes(FriendlyByteBuf buf) {
        buf.writeInt(this.ribbitId);
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
}
