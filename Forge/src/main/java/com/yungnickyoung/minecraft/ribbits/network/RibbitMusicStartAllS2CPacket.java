package com.yungnickyoung.minecraft.ribbits.network;

import com.yungnickyoung.minecraft.ribbits.entity.RibbitEntity;
import com.yungnickyoung.minecraft.ribbits.util.BufferUtils;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.fml.DistExecutor;
import net.minecraftforge.network.NetworkEvent;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Supplier;

public class RibbitMusicStartAllS2CPacket {
    private final List<Integer> ribbitIds;
    private final int tickOffset;

    public RibbitMusicStartAllS2CPacket(RibbitEntity masterRibbit, int tickOffset) {
        List<Integer> ribbitIds = new ArrayList<>();
        ribbitIds.add(masterRibbit.getId());
        ribbitIds.addAll(masterRibbit.getRibbitsPlayingMusic().stream().map(RibbitEntity::getId).toList());
        this.ribbitIds = ribbitIds;
        this.tickOffset = tickOffset;
    }

    /**
     * Decoder
     */
    public RibbitMusicStartAllS2CPacket(FriendlyByteBuf buf) {
        this.ribbitIds = BufferUtils.readIntList(buf);
        this.tickOffset = buf.readInt();
    }

    /**
     * Encoder
     */
    public void toBytes(FriendlyByteBuf buf) {
        BufferUtils.writeIntList(this.ribbitIds, buf);
        buf.writeInt(tickOffset);
    }

    /**
     * Handler
     */
    public boolean handle(Supplier<NetworkEvent.Context> ctx) {
        ctx.get().enqueueWork(() ->
                // Make sure this is only executed on the physical client
                DistExecutor.unsafeRunWhenOn(Dist.CLIENT, () -> () -> ClientPacketHandlerForge.handleStartAllPacket(this, ctx))
        );
        ctx.get().setPacketHandled(true);
        return true;
    }

    public List<Integer> getRibbitIds() {
        return this.ribbitIds;
    }

    public int getTickOffset() {
        return this.tickOffset;
    }
}
