package com.yungnickyoung.minecraft.ribbits.network;

import com.yungnickyoung.minecraft.ribbits.util.BufferUtils;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.fml.DistExecutor;
import net.minecraftforge.event.network.CustomPayloadEvent;

import java.util.List;
import java.util.UUID;

public class RequestSupporterHatStateS2CPacket {
    private final List<UUID> enabledSupporterHatPlayers;

    public RequestSupporterHatStateS2CPacket(List<UUID> enabledSupporterHatPlayers) {
        this.enabledSupporterHatPlayers = enabledSupporterHatPlayers;
    }

    /**
     * Decoder
     */
    public RequestSupporterHatStateS2CPacket(FriendlyByteBuf buf) {
        this.enabledSupporterHatPlayers = BufferUtils.readUUIDList(buf);
    }

    /**
     * Encoder
     */
    public void toBytes(FriendlyByteBuf buf) {
        BufferUtils.writeUUIDList(this.enabledSupporterHatPlayers, buf);
    }

    /**
     * Handler
     */
    public boolean handle(CustomPayloadEvent.Context ctx) {
        ctx.enqueueWork(() ->
                // Make sure this is only executed on the physical client
                DistExecutor.unsafeRunWhenOn(Dist.CLIENT, () -> () -> ClientPacketHandlerForge.handleSupporterHatStateRequest(this, ctx))
        );
        ctx.setPacketHandled(true);
        return true;
    }

    public List<UUID> getEnabledSupporterHatPlayers() {
        return enabledSupporterHatPlayers;
    }
}
