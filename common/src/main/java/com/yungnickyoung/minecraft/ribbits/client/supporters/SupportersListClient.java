package com.yungnickyoung.minecraft.ribbits.client.supporters;

import com.yungnickyoung.minecraft.ribbits.RibbitsCommon;
import com.yungnickyoung.minecraft.ribbits.platform.PlatformHelper;

import java.util.HashSet;
import java.util.Set;
import java.util.UUID;

/**
 * List of players with the supporter hat enabled on the player's current server.
 * The Set of UUIDs should always be kept in sync with the server's list of supporters.
 */
public class SupportersListClient {
    private static final Set<UUID> playersWithSupporterHat = new HashSet<>();

    public static void toggleSupporterHat(UUID playerUUID, boolean enabled) {
        if (enabled) {
            if (!PlatformHelper.isDevelopmentEnvironment() && !SupportersJSON.get().isSupporter(playerUUID)) {
                RibbitsCommon.LOGGER.warn("Player {} tried to enable supporter hat but is not a supporter.", playerUUID);
                return;
            }
            playersWithSupporterHat.add(playerUUID);
        } else {
            playersWithSupporterHat.remove(playerUUID);
        }
    }

    public static boolean isPlayerSupporterHatEnabled(UUID playerUUID) {
        return playersWithSupporterHat.contains(playerUUID);
    }

    public static void clear() {
        playersWithSupporterHat.clear();
    }
}
