// Copyright (C) 2026 Dasik (Rifaditya) | GNU GPLv3
// Verified against: Minecraft 26.1
package net.vanillaoutsider.naturalreproduction;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import java.util.Random;

/**
 * Headless unit & fuzz test suite asserting continuous overcrowding stunting curve,
 * non-increasing monotonicity, floor/ceiling saturation, negative count exploit protection,
 * spacious pasture recovery scale bounds, and 10,000-iteration chaos property fuzzing ([BL-NR-006b]).
 */
public class StuntingFuzzTest {

    private static float clamp(float value, float min, float max) {
        return Math.min(Math.max(value, min), max);
    }

    public static float sanitizeScale(float rawScale, float minAllowed, float maxAllowed, float fallback) {
        if (Float.isNaN(rawScale) || Float.isInfinite(rawScale)) {
            return fallback;
        }
        float safeMin = (Float.isNaN(minAllowed) || Float.isInfinite(minAllowed) || minAllowed <= 0.0f) ? 0.10f : minAllowed;
        float safeMax = (Float.isNaN(maxAllowed) || Float.isInfinite(maxAllowed) || maxAllowed <= 0.0f) ? 1.20f : maxAllowed;
        if (safeMin > safeMax) {
            float temp = safeMin;
            safeMin = safeMax;
            safeMax = temp;
        }
        return clamp(rawScale, safeMin, safeMax);
    }

    private static float computePenaltyMultiplier(int extraLocalCount) {
        int safeCount = Math.max(0, extraLocalCount);
        return Math.max(0.95f - (safeCount * 0.05f), 0.20f);
    }

    private static float applyConfinementScale(float currentScale, int extraLocalCount, float minAllowed, float maxAllowed) {
        float penaltyMultiplier = computePenaltyMultiplier(extraLocalCount);
        return sanitizeScale(currentScale * penaltyMultiplier, minAllowed, maxAllowed, 1.0f);
    }

    private static float applyRecoveryScale(float currentScale, float recoveryBoost, float minAllowed, float maxAllowed) {
        return sanitizeScale(currentScale * recoveryBoost, minAllowed, maxAllowed, 1.0f);
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

    @Test
    @DisplayName("Verify 10,000-Iteration Chaos Property Fuzzing Resilience")
    public void testChaosPropertyFuzzingTenThousandIterations() {
        Random rng = new Random(0xDA51CL);
        float minAllowed = 0.10f;
        float maxAllowed = 1.20f;
        float fallback = 1.0f;
        float recoveryBoost = 1.15f;

        for (int i = 0; i < 10000; i++) {
            float rawScale;
            int density;
            int scenarioRoll = rng.nextInt(10); // 0..9

            if (scenarioRoll == 0) {
                // 10% chance: Float.NaN
                rawScale = Float.NaN;
                density = rng.nextInt(50);
            } else if (scenarioRoll == 1) {
                // 10% chance: Float.POSITIVE_INFINITY or Float.NEGATIVE_INFINITY
                rawScale = rng.nextBoolean() ? Float.POSITIVE_INFINITY : Float.NEGATIVE_INFINITY;
                density = rng.nextInt(50);
            } else if (scenarioRoll == 2) {
                // 10% chance: astronomical values (1e38f, -1e38f, Float.MAX_VALUE, -Float.MAX_VALUE)
                float[] astronomicalValues = {1e38f, -1e38f, Float.MAX_VALUE, -Float.MAX_VALUE};
                rawScale = astronomicalValues[rng.nextInt(astronomicalValues.length)];
                density = rng.nextInt(50);
            } else if (scenarioRoll == 3) {
                // 10% chance: subnormal/zero values (0.0f, -0.0f, Float.MIN_VALUE)
                float[] subnormals = {0.0f, -0.0f, Float.MIN_VALUE};
                rawScale = subnormals[rng.nextInt(subnormals.length)];
                density = rng.nextInt(50);
            } else if (scenarioRoll == 4) {
                // 10% chance: negative counts (-1000 ... -1)
                rawScale = 0.5f + rng.nextFloat();
                density = -1 - rng.nextInt(1000);
            } else {
                // 50% chance: uniform random floats between -100.0f and 100.0f with random densities between -50 and 200.
                rawScale = (rng.nextFloat() * 200.0f) - 100.0f;
                density = rng.nextInt(251) - 50;
            }

            float penaltyMultiplier = computePenaltyMultiplier(density);
            Assertions.assertFalse(Float.isNaN(penaltyMultiplier), "Penalty multiplier must not be NaN");
            Assertions.assertFalse(Float.isInfinite(penaltyMultiplier), "Penalty multiplier must not be Infinite");
            Assertions.assertTrue(penaltyMultiplier >= 0.20f && penaltyMultiplier <= 0.95f,
                    "Penalty multiplier must remain within [0.20, 0.95]: " + penaltyMultiplier);

            float confinementResult = applyConfinementScale(rawScale, density, minAllowed, maxAllowed);
            Assertions.assertFalse(Float.isNaN(confinementResult), "Resulting scale must never be NaN");
            Assertions.assertFalse(Float.isInfinite(confinementResult), "Resulting scale must never be Infinite");
            Assertions.assertTrue(confinementResult >= 0.05f && confinementResult <= 2.0f,
                    "Resulting scale must remain within technical saturated bounds: " + confinementResult);

            float recoveryResult = applyRecoveryScale(rawScale, recoveryBoost, minAllowed, maxAllowed);
            Assertions.assertFalse(Float.isNaN(recoveryResult), "Resulting scale must never be NaN");
            Assertions.assertFalse(Float.isInfinite(recoveryResult), "Resulting scale must never be Infinite");
            Assertions.assertTrue(recoveryResult >= 0.05f && recoveryResult <= 2.0f,
                    "Resulting scale must remain within technical saturated bounds: " + recoveryResult);

            float sanitizedResult = sanitizeScale(rawScale, minAllowed, maxAllowed, fallback);
            Assertions.assertFalse(Float.isNaN(sanitizedResult), "Resulting scale must never be NaN");
            Assertions.assertFalse(Float.isInfinite(sanitizedResult), "Resulting scale must never be Infinite");
            Assertions.assertTrue(sanitizedResult >= 0.05f && sanitizedResult <= 2.0f,
                    "Resulting scale must remain within technical saturated bounds: " + sanitizedResult);
        }
    }

    @Test
    @DisplayName("Verify Direct NaN Injection Clean Fallback Recovery")
    public void testDirectNaNInjection() {
        float fallback = 1.0f;
        float min = 0.10f;
        float max = 1.20f;

        // Directly inject Float.NaN as current scale
        float sanitizedNan = sanitizeScale(Float.NaN, min, max, fallback);
        Assertions.assertEquals(fallback, sanitizedNan, 0.0001f, "NaN scale must recover cleanly to fallback");

        // Directly inject Float.NaN as bounds
        float sanitizedBoundsNan = sanitizeScale(0.80f, Float.NaN, Float.NaN, fallback);
        Assertions.assertTrue(sanitizedBoundsNan >= 0.10f && sanitizedBoundsNan <= 1.20f,
                "NaN bounds must safely default to safe bounds [0.10, 1.20]");

        // Confinement with NaN scale
        float confinementNan = applyConfinementScale(Float.NaN, 5, min, max);
        Assertions.assertEquals(fallback, confinementNan, 0.0001f, "Confinement with NaN scale must recover to fallback");

        // Recovery with NaN scale
        float recoveryNan = applyRecoveryScale(Float.NaN, 1.15f, min, max);
        Assertions.assertEquals(fallback, recoveryNan, 0.0001f, "Recovery with NaN scale must recover to fallback");
    }

    @Test
    @DisplayName("Verify Infinite Scale Injection Clean Fallback Recovery")
    public void testInfiniteScaleInjection() {
        float fallback = 1.0f;
        float min = 0.10f;
        float max = 1.20f;

        // Directly inject Float.POSITIVE_INFINITY
        float posInf = sanitizeScale(Float.POSITIVE_INFINITY, min, max, fallback);
        Assertions.assertEquals(fallback, posInf, 0.0001f, "+Infinity scale must recover cleanly to fallback");

        // Directly inject Float.NEGATIVE_INFINITY
        float negInf = sanitizeScale(Float.NEGATIVE_INFINITY, min, max, fallback);
        Assertions.assertEquals(fallback, negInf, 0.0001f, "-Infinity scale must recover cleanly to fallback");

        // Confinement with Infinite scale
        float confinementPos = applyConfinementScale(Float.POSITIVE_INFINITY, 3, min, max);
        Assertions.assertEquals(fallback, confinementPos, 0.0001f, "+Infinity confinement must recover to fallback");

        float confinementNeg = applyConfinementScale(Float.NEGATIVE_INFINITY, 3, min, max);
        Assertions.assertEquals(fallback, confinementNeg, 0.0001f, "-Infinity confinement must recover to fallback");

        // Recovery with Infinite scale
        float recoveryPos = applyRecoveryScale(Float.POSITIVE_INFINITY, 1.15f, min, max);
        Assertions.assertEquals(fallback, recoveryPos, 0.0001f, "+Infinity recovery must recover to fallback");

        float recoveryNeg = applyRecoveryScale(Float.NEGATIVE_INFINITY, 1.15f, min, max);
        Assertions.assertEquals(fallback, recoveryNeg, 0.0001f, "-Infinity recovery must recover to fallback");
    }

    @Test
    @DisplayName("Verify Astronomical Overflow Non-Crashing Saturated Clamp")
    public void testAstronomicalOverflowGuards() {
        float min = 0.10f;
        float max = 1.20f;
        float fallback = 1.0f;

        // Directly inject Float.MAX_VALUE * 2.0f
        float overflow = Float.MAX_VALUE * 2.0f;
        Assertions.assertTrue(Float.isInfinite(overflow), "Float.MAX_VALUE * 2.0f overflows to positive infinity in float arithmetic");

        float sanitizedOverflow = sanitizeScale(overflow, min, max, fallback);
        Assertions.assertEquals(fallback, sanitizedOverflow, 0.0001f, "Overflowed infinity must recover safely to fallback");

        // Huge astronomical finite value (1e38f) clamps to maxAllowed
        float hugeValue = 1e38f;
        float clampedHuge = sanitizeScale(hugeValue, min, max, fallback);
        Assertions.assertEquals(max, clampedHuge, 0.0001f, "Astronomical finite float must clamp to maxAllowed");

        // Negative astronomical finite value (-1e38f) clamps to minAllowed
        float negHugeValue = -1e38f;
        float clampedNegHuge = sanitizeScale(negHugeValue, min, max, fallback);
        Assertions.assertEquals(min, clampedNegHuge, 0.0001f, "Negative astronomical finite float must clamp to minAllowed");
    }
}
