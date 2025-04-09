package com.yungnickyoung.minecraft.ribbits.services;

import com.yungnickyoung.minecraft.ribbits.network.payload.ToggleSupporterHatPayload;
import net.minecraft.client.Minecraft;
import net.neoforged.neoforge.network.PacketDistributor;

import java.util.UUID;

public class NeoForgeSupporterHelper implements ISupporterHelper {
    @Override
    public void notifyServerOfSupporterHatState(boolean enabled) {
        // Ensure the player UUID is valid
        UUID playerUUID = Minecraft.getInstance().getUser().getProfileId();
        if (playerUUID == null) return;

        // Only send payload if the player is connected to a server
        if (Minecraft.getInstance().getConnection() == null) return;

        ToggleSupporterHatPayload payload = new ToggleSupporterHatPayload(playerUUID, enabled);
        PacketDistributor.sendToServer(payload);
    }
}
