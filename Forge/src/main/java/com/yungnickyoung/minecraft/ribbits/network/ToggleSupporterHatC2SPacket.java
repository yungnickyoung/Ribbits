package com.yungnickyoung.minecraft.ribbits.network;

import net.minecraft.network.FriendlyByteBuf;
import net.minecraftforge.event.network.CustomPayloadEvent;

import java.util.UUID;

public class ToggleSupporterHatC2SPacket {
    private final UUID playerUuid;
    private final boolean enabled;

    public ToggleSupporterHatC2SPacket(UUID playerUuid, boolean enabled) {
        this.playerUuid = playerUuid;
        this.enabled = enabled;
    }

    /**
     * Decoder
     */
    public ToggleSupporterHatC2SPacket(FriendlyByteBuf buf) {
        this.playerUuid = buf.readUUID();
        this.enabled = buf.readBoolean();
    }

    /**
     * Encoder
     */
    public void toBytes(FriendlyByteBuf buf) {
        buf.writeUUID(this.playerUuid);
        buf.writeBoolean(this.enabled);
    }

    /**
     * Handler
     */
    public boolean handle(CustomPayloadEvent.Context ctx) {
        ctx.enqueueWork(() -> ServerPacketHandlerForge.handleToggleSupporterHat(this, ctx));
        ctx.setPacketHandled(true);
        return true;
    }

    public UUID getPlayerUuid() {
        return this.playerUuid;
    }

    public boolean getEnabled() {
        return this.enabled;
    }
}
