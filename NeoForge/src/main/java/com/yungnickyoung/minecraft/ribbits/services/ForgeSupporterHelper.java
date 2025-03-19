package com.yungnickyoung.minecraft.ribbits.services;

import com.yungnickyoung.minecraft.ribbits.module.NetworkModuleNeoForge;
import com.yungnickyoung.minecraft.ribbits.network.ToggleSupporterHatC2SPacket;
import net.minecraft.client.Minecraft;

import java.util.UUID;

public class ForgeSupporterHelper implements ISupporterHelper {
    @Override
    public void notifyServerOfSupporterHatState(boolean enabled) {
        // Ensure the player UUID is valid
        UUID playerUUID = Minecraft.getInstance().getUser().getProfileId();
        if (playerUUID == null) return;

        // Only send packet if the player is connected to a server
        if (Minecraft.getInstance().getConnection() == null) return;

        NetworkModuleNeoForge.sendToServer(new ToggleSupporterHatC2SPacket(playerUUID, enabled));
    }
}
