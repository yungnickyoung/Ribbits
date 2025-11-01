package com.yungnickyoung.minecraft.ribbits.supporters;

import java.util.Collection;
import java.util.HashSet;
import java.util.Set;
import java.util.UUID;

/**
 * List of players with the supporter hat enabled on the server.
 * Whenever this list is updated, the server should send a payload to all clients to update their list as well.
 */
public class SupportersListServer {
    private static final Set<UUID> playersWithSupporterHat = new HashSet<>();

    public static void toggleSupporterHat(UUID playerUUID, boolean enabled) {
        if (enabled) {
            playersWithSupporterHat.add(playerUUID);
        } else {
            playersWithSupporterHat.remove(playerUUID);
        }
    }

    public static Collection<UUID> getPlayersWithSupporterHat() {
        return playersWithSupporterHat;
    }
}
