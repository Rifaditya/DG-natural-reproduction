// Copyright (C) 2026 Dasik (Rifaditya) | GNU GPLv3
package net.vanillaoutsider.naturalreproduction.util;

import net.minecraft.core.BlockPos;

/**
 * Immutable record representing a single autonomous animal reproduction event.
 */
public record BreedingLogEntry(
    long day,
    long timeOfDay,
    String species,
    BlockPos pos,
    String biomeId,
    float scale,
    String habitatStatus
) {
    public String formatSummary() {
        return String.format("Day %d | %s at [%d, %d, %d] (%s) -> Offspring Scale: %.2fx [%s]",
            day, species, pos.getX(), pos.getY(), pos.getZ(), biomeId, scale, habitatStatus);
    }
}
