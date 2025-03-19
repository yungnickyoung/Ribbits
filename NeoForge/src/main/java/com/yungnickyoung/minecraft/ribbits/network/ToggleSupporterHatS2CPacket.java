package com.yungnickyoung.minecraft.ribbits.network;

import net.minecraft.network.FriendlyByteBuf;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.fml.DistExecutor;
import net.minecraftforge.network.NetworkEvent;

import java.util.UUID;
import java.util.function.Supplier;

public class ToggleSupporterHatS2CPacket {
    private final UUID playerUuid;
    private final boolean enabled;

    public ToggleSupporterHatS2CPacket(UUID playerUuid, boolean enabled) {
        this.playerUuid = playerUuid;
        this.enabled = enabled;
    }

    /**
     * Decoder
     */
    public ToggleSupporterHatS2CPacket(FriendlyByteBuf buf) {
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
    public boolean handle(Supplier<NetworkEvent.Context> ctx) {
        ctx.get().enqueueWork(() ->
                // Make sure this is only executed on the physical client
                DistExecutor.unsafeRunWhenOn(Dist.CLIENT, () -> () -> ClientPacketHandlerForge.handleToggleSupporterHat(this, ctx))
        );
        ctx.get().setPacketHandled(true);
        return true;
    }

    public UUID getPlayerUuid() {
        return this.playerUuid;
    }

    public boolean getEnabled() {
        return this.enabled;
    }
}
