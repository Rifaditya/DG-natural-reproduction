// Copyright (C) 2026 Dasik (Rifaditya) | GNU GPLv3
package net.vanillaoutsider.naturalreproduction.util;

import net.minecraft.core.BlockPos;
import net.minecraft.server.level.ServerLevel;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * Thread-safe logger and history tracker for autonomous animal reproduction events.
 */
public class BreedingTrackerLogger {
    private static final Logger LOGGER = LoggerFactory.getLogger("NaturalReproduction-Tracker");
    private static final int MAX_ENTRIES = 50;
    private static final List<BreedingLogEntry> LOG_HISTORY = Collections.synchronizedList(new ArrayList<>());

    public static void logBreeding(ServerLevel level, String species, BlockPos pos, String biomeId, float scale, String habitatStatus) {
        long gameTime = level.getGameTime();
        long day = gameTime / 24000L;
        long timeOfDay = gameTime % 24000L;

        BreedingLogEntry entry = new BreedingLogEntry(day, timeOfDay, species, pos, biomeId, scale, habitatStatus);
        
        synchronized (LOG_HISTORY) {
            LOG_HISTORY.add(entry);
            if (LOG_HISTORY.size() > MAX_ENTRIES) {
                LOG_HISTORY.remove(0);
            }
        }

        LOGGER.info("[Autonomous Breeding Log] {}", entry.formatSummary());
    }

    public static List<BreedingLogEntry> getRecentLogs(int limit) {
        synchronized (LOG_HISTORY) {
            int size = LOG_HISTORY.size();
            if (size == 0) {
                return Collections.emptyList();
            }
            int start = Math.max(0, size - limit);
            List<BreedingLogEntry> recent = new ArrayList<>(LOG_HISTORY.subList(start, size));
            Collections.reverse(recent);
            return recent;
        }
    }

    public static int getLogCount() {
        return LOG_HISTORY.size();
    }

    public static void clear() {
        LOG_HISTORY.clear();
        LOGGER.info("Natural Reproduction: Autonomous Breeding Tracker Logs cleared.");
    }
}
