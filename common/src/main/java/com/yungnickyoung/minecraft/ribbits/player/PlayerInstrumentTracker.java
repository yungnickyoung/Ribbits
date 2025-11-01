package com.yungnickyoung.minecraft.ribbits.player;

import com.yungnickyoung.minecraft.ribbits.module.ItemModule;
import com.yungnickyoung.minecraft.ribbits.network.ServerNetworkHandler;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.player.Player;

import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;

/**
 * Tracks which players are listening to which players playing instruments.
 */
public class PlayerInstrumentTracker {
    private static final float AUDIENCE_RANGE = 32.0f; // distance in blocks
    private static final ConcurrentHashMap<Player, Set<Player>> performerToAudienceMap = new ConcurrentHashMap<>();

    public static void onServerTick() {
        for (Player performer : performerToAudienceMap.keySet()) {
            updateAudienceForPerformer(performer);
        }
    }

    private static void updateAudienceForPerformer(Player performer) {
        Set<Player> audienceMembers = new HashSet<>(performerToAudienceMap.get(performer));

        if (performer.isRemoved() || !performer.getItemInHand(performer.getUsedItemHand()).is(ItemModule.MARACA.get())) {
            removePerformer(performer);
            return;
        }

        List<Player> playersInRange = performer.level().getEntitiesOfClass(Player.class,
                performer.getBoundingBox().inflate(AUDIENCE_RANGE, AUDIENCE_RANGE, AUDIENCE_RANGE));

        // Add any new players in range
        playersInRange.forEach(player -> {
            if (!audienceMembers.contains(player)) {
                audienceMembers.add(player);
                ServerNetworkHandler.startHearingMaraca((ServerPlayer) performer, (ServerPlayer) player);
            }
        });

        // Remove any players no longer in the world or out of range
        audienceMembers.removeIf(player -> {
            if (player.isRemoved() || !playersInRange.contains(player)) {
                ServerNetworkHandler.stopHearingMaraca((ServerPlayer) performer, (ServerPlayer) player);
                return true;
            }
            return false;
        });

        performerToAudienceMap.put(performer, audienceMembers);
    }

    public static void addPerformer(Player performer) {
        performerToAudienceMap.put(performer, new HashSet<>());
    }

    public static void removePerformer(Player performer) {
        // Stop the sound for all players in the audience
        for (Player audienceMember : performerToAudienceMap.get(performer)) {
            ServerNetworkHandler.stopHearingMaraca((ServerPlayer) performer, (ServerPlayer) audienceMember);
        }

        performerToAudienceMap.remove(performer);
    }
}
