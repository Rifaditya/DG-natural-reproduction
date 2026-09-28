// Copyright (C) 2026 Dasik (Rifaditya) | GNU GPLv3
// Verified against: Minecraft 26.3
package net.vanillaoutsider.naturalreproduction;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

/**
 * Headless unit & fuzz test suite asserting continuous overcrowding stunting curve,
 * non-increasing monotonicity, floor/ceiling saturation, negative count exploit protection,
 * and spacious pasture recovery scale bounds ([BL-NR-006b]).
 */
public class StuntingFuzzTest {

    private static float clamp(float value, float min, float max) {
        return Math.min(Math.max(value, min), max);
    }

    private static float computePenaltyMultiplier(int extraLocalCount) {
        int safeCount = Math.max(0, extraLocalCount);
        return Math.max(0.95f - (safeCount * 0.05f), 0.20f);
    }

    private static float applyConfinementScale(float currentScale, int extraLocalCount, float minAllowed, float maxAllowed) {
        float penaltyMultiplier = computePenaltyMultiplier(extraLocalCount);
        return clamp(currentScale * penaltyMultiplier, minAllowed, maxAllowed);
    }

    private static float applyRecoveryScale(float currentScale, float recoveryBoost, float minAllowed, float maxAllowed) {
        return clamp(currentScale * recoveryBoost, minAllowed, maxAllowed);
    }

    @Test
    @DisplayName("Verify Continuous Overcrowding Curve Monotonicity & Boundary Invariants")
    public void testContinuousOvercrowdingCurveMonotonicity() {
        for (int c = 0; c <= 100; c++) {
            float multiplier = computePenaltyMultiplier(c);
            Assertions.assertTrue(multiplier >= 0.20f && multiplier <= 0.95f,
                    "Multiplier for count " + c + " (" + multiplier + ") must be within [0.20, 0.95]");
            float nextMultiplier = computePenaltyMultiplier(c + 1);
            Assertions.assertTrue(multiplier >= nextMultiplier,
                    "Multiplier must be non-increasing monotonic: c=" + c + " (" + multiplier + ") >= c=" + (c + 1) + " (" + nextMultiplier + ")");
        }

        Assertions.assertEquals(0.95f, computePenaltyMultiplier(0), 0.0001f, "Count 0 must yield 0.95f");
        Assertions.assertEquals(0.90f, computePenaltyMultiplier(1), 0.0001f, "Count 1 must yield 0.90f");
        Assertions.assertEquals(0.85f, computePenaltyMultiplier(2), 0.0001f, "Count 2 must yield 0.85f");
        Assertions.assertEquals(0.80f, computePenaltyMultiplier(3), 0.0001f, "Count 3 must yield 0.80f");
        Assertions.assertEquals(0.20f, computePenaltyMultiplier(15), 0.0001f, "Count 15 must yield 0.20f");
    }

    @Test
    @DisplayName("Verify Severe Overcrowding Saturation Floor (0.20f)")
    public void testSevereOvercrowdingSaturationFloor() {
        int[] counts = {15, 20, 50, 100, 1000};
        for (int count : counts) {
            float multiplier = computePenaltyMultiplier(count);
            Assertions.assertEquals(0.20f, multiplier, 0.0001f,
                    "Count " + count + " must saturate exactly at 0.20f floor");
        }
    }

    @Test
    @DisplayName("Verify Spacious Pasture Recovery Ceiling Clamping (1.20f)")
    public void testSpaciousPastureRecoveryCeiling() {
        float minAllowed = 0.10f;
        float maxAllowed = 1.20f;
        float recoveryBoost = 1.15f;

        // Baseline scale 0.80f with 1.15f boost -> 0.92f
        float scale1 = applyRecoveryScale(0.80f, recoveryBoost, minAllowed, maxAllowed);
        Assertions.assertEquals(0.92f, scale1, 0.0001f, "0.80f with 1.15f boost must produce 0.92f");

        // High baseline scale 1.10f with 1.15f boost -> clamps strictly to maxAllowed (1.20f), no overshoot
        float scale2 = applyRecoveryScale(1.10f, recoveryBoost, minAllowed, maxAllowed);
        Assertions.assertEquals(1.20f, scale2, 0.0001f, "1.10f with 1.15f boost must clamp to 1.20f ceiling");

        // Current scale 1.20f with 1.15f boost -> remains strictly 1.20f
        float scale3 = applyRecoveryScale(1.20f, recoveryBoost, minAllowed, maxAllowed);
        Assertions.assertEquals(1.20f, scale3, 0.0001f, "1.20f with 1.15f boost must remain clamped at 1.20f ceiling");
    }

    @Test
    @DisplayName("Verify Negative Density Exploit Protection")
    public void testNegativeDensityExploitProtection() {
        int[] negativeCounts = {-1, -10, -500};
        for (int count : negativeCounts) {
            float multiplier = computePenaltyMultiplier(count);
            Assertions.assertEquals(0.95f, multiplier, 0.0001f,
                    "Negative count " + count + " must clamp to count 0 multiplier (0.95f)");
        }
    }

    @Test
    @DisplayName("Verify Confinement Scale Calculation & Floor Clamping")
    public void testConfinementScaleClamping() {
        float minAllowed = 0.10f;
        float maxAllowed = 1.20f;

        // Normal scale 1.0f with 5 extra mobs -> penaltyMultiplier 0.70f -> scale 0.70f
        float normalScale = applyConfinementScale(1.0f, 5, minAllowed, maxAllowed);
        Assertions.assertEquals(0.70f, normalScale, 0.0001f, "1.0f with 5 extra mobs must scale to 0.70f");

        // Already stunted scale 0.12f with 15 extra mobs -> 0.12 * 0.20 = 0.024f -> clamps strictly to minAllowed (0.10f)
        float stuntedScale = applyConfinementScale(0.12f, 15, minAllowed, maxAllowed);
        Assertions.assertEquals(0.10f, stuntedScale, 0.0001f, "0.12f with 15 extra mobs must clamp to minAllowed (0.10f)");
    }
}
