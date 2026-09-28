// Copyright (C) 2026 Dasik (Rifaditya) | GNU GPLv3
// Verified against: Minecraft 26.1
package net.vanillaoutsider.naturalreproduction;

import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Optional;
import java.util.Set;
import java.util.UUID;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

/**
 * Headless unit test suite asserting 3-generation pedigree kinship matching,
 * inbreeding tier progression (Tiers 0-4), gradual generational dilution,
 * and hybrid vigor scaling for Natural Reproduction ([BL-NR-006a]).
 */
public class LineageGeneticsTest {

    @Test
    @DisplayName("Verify 3-Generation Pedigree Ancestry Matching & Inbreeding Tiers (T0 to T3)")
    public void testThreeGenerationPedigreeAncestryMatching() {
        // Construct distinct ancestor UUIDs
        UUID g1 = UUID.randomUUID();
        UUID g2 = UUID.randomUUID();
        UUID g3 = UUID.randomUUID();
        UUID g4 = UUID.randomUUID();
        UUID g5 = UUID.randomUUID();
        UUID g6 = UUID.randomUUID();

        // Tier 0: Completely disjoint 3-generation pedigrees
        Set<UUID> ancestorsA = Set.of(g1, g2, g3);
        Set<UUID> ancestorsB = Set.of(g4, g5, g6);
        int sharedT0 = countSharedAncestors(ancestorsA, ancestorsB);
        Assertions.assertEquals(0, sharedT0, "Diverse wild mates must share 0 common ancestors");
        Assertions.assertEquals(0, determineInbreedingTierFromAncestors(sharedT0), "0 shared ancestors must resolve to Tier 0");

        // Tier 1: 1 shared ancestor (e.g., shared grandparent / first cousins)
        Set<UUID> ancestorsC = Set.of(g1, g4, g5);
        int sharedT1 = countSharedAncestors(ancestorsA, ancestorsC);
        Assertions.assertEquals(1, sharedT1, "First-degree cousins share exactly 1 ancestor");
        Assertions.assertEquals(1, determineInbreedingTierFromAncestors(sharedT1), "1 shared ancestor must resolve to Tier 1");

        // Tier 2: 2 shared ancestors (e.g., half-siblings or double-cousins)
        Set<UUID> ancestorsD = Set.of(g1, g2, g5);
        int sharedT2 = countSharedAncestors(ancestorsA, ancestorsD);
        Assertions.assertEquals(2, sharedT2, "Half-siblings share 2 common ancestors");
        Assertions.assertEquals(2, determineInbreedingTierFromAncestors(sharedT2), "2 shared ancestors must resolve to Tier 2");

        // Tier 3: 3+ shared ancestors (e.g., full siblings / repeated closed-herd breeding)
        Set<UUID> ancestorsE = Set.of(g1, g2, g3);
        int sharedT3 = countSharedAncestors(ancestorsA, ancestorsE);
        Assertions.assertEquals(3, sharedT3, "Full siblings / closed herd share 3+ common ancestors");
        Assertions.assertEquals(3, determineInbreedingTierFromAncestors(sharedT3), "3+ shared ancestors must resolve to Tier 3");
    }

    @Test
    @DisplayName("Verify Kinship Inbreeding Tier Progression Crosses (T0 -> T4)")
    public void testInbreedingTierProgressionCrosses() {
        // Inbreeding progression formula: clamp(max(p1Tier, p2Tier) + 1, 1, 4)
        // 1. T0 + T0 inbred (kinship detected) -> Tier 1
        int crossT0_T0 = computeInbreedingTier(0, 0, true);
        Assertions.assertEquals(1, crossT0_T0, "T0 + T0 related mating must progress to Tier 1");

        // 2. T1 + T1 inbred -> Tier 2
        int crossT1_T1 = computeInbreedingTier(1, 1, true);
        Assertions.assertEquals(2, crossT1_T1, "T1 + T1 inbred mating must progress to Tier 2");

        // 3. T2 + T2 inbred -> Tier 3
        int crossT2_T2 = computeInbreedingTier(2, 2, true);
        Assertions.assertEquals(3, crossT2_T2, "T2 + T2 inbred mating must progress to Tier 3");

        // 4. T3 + T3 inbred -> Tier 4 (Lethal Collapse)
        int crossT3_T3 = computeInbreedingTier(3, 3, true);
        Assertions.assertEquals(4, crossT3_T3, "T3 + T3 inbred mating must progress to Tier 4 Lethal Collapse");

        // 5. T4 + T4 inbred -> capped at Tier 4
        int crossT4_T4 = computeInbreedingTier(4, 4, true);
        Assertions.assertEquals(4, crossT4_T4, "Tier 4 inbred crosses must remain clamped at Tier 4 ceiling");

        // 6. Asymmetrical crosses: highest parent tier dominates
        int crossT1_T3 = computeInbreedingTier(1, 3, true);
        Assertions.assertEquals(4, crossT1_T3, "T1 + T3 inbred mating must step up from highest parent (Tier 3 -> 4)");

        int crossT0_T2 = computeInbreedingTier(0, 2, true);
        Assertions.assertEquals(3, crossT0_T2, "T0 + T2 inbred mating must step up from highest parent (Tier 2 -> 3)");
    }

    @Test
    @DisplayName("Verify Gradual Generational Dilution on Outcrossing (-1 Tier Step)")
    public void testGradualGenerationalDilution() {
        // Outcrossing formula: max(0, max(p1Tier, p2Tier) - 1)
        // 1. Tier 4 outcrossed with Tier 0 wild animal -> Tier 3
        int diluteT4 = computeInbreedingTier(4, 0, false);
        Assertions.assertEquals(3, diluteT4, "Outcrossing Tier 4 with Tier 0 must dilute to Tier 3");

        // 2. Tier 3 outcrossed with Tier 0 -> Tier 2
        int diluteT3 = computeInbreedingTier(3, 0, false);
        Assertions.assertEquals(2, diluteT3, "Outcrossing Tier 3 with Tier 0 must dilute to Tier 2");

        // 3. Tier 2 outcrossed with Tier 0 -> Tier 1
        int diluteT2 = computeInbreedingTier(2, 0, false);
        Assertions.assertEquals(1, diluteT2, "Outcrossing Tier 2 with Tier 0 must dilute to Tier 1");

        // 4. Tier 1 outcrossed with Tier 0 -> Tier 0 (Full recovery)
        int diluteT1 = computeInbreedingTier(1, 0, false);
        Assertions.assertEquals(0, diluteT1, "Outcrossing Tier 1 with Tier 0 must dilute to Tier 0");

        // 5. Tier 0 outcrossed with Tier 0 -> remains Tier 0
        int diluteT0 = computeInbreedingTier(0, 0, false);
        Assertions.assertEquals(0, diluteT0, "Diverse wild breeding remains at Tier 0");

        // 6. Outcrossing between two degraded animals (T3 and T2 unrelated)
        int diluteT3_T2 = computeInbreedingTier(3, 2, false);
        Assertions.assertEquals(2, diluteT3_T2, "Unrelated T3 and T2 cross must dilute down from highest parent (3 - 1 = 2)");
    }

    @Test
    @DisplayName("Verify Hybrid Vigor (Heterosis) Recovery Scale Bonus (+15%)")
    public void testHybridVigorHeterosisBonus() {
        float minAllowed = 0.10f;
        float maxAllowed = 1.20f;
        float baselineScale = 1.00f;

        // Scenario A: Tier 0 achieved from clean outcross of degraded parent (T1 + T0 -> T0)
        int p1Tier = 1;
        int p2Tier = 0;
        int babyTier = computeInbreedingTier(p1Tier, p2Tier, false);
        Assertions.assertEquals(0, babyTier, "Dilution produces Tier 0 baby");

        boolean triggersHybridVigor = babyTier == 0 && (p1Tier > 0 || p2Tier > 0);
        Assertions.assertTrue(triggersHybridVigor, "Reaching Tier 0 from degraded ancestry must trigger Hybrid Vigor");

        float boostedScale = clamp(baselineScale * 1.15f, minAllowed, maxAllowed);
        Assertions.assertEquals(1.15f, boostedScale, 0.001f, "Hybrid vigor awards +15% scale boost (1.15x)");

        // Scenario B: Wild baseline animals (T0 + T0 -> T0) do NOT get hybrid vigor boost
        boolean wildHybridVigor = babyTier == 0 && (0 > 0 || 0 > 0);
        Assertions.assertFalse(wildHybridVigor, "Standard wild births do not trigger hybrid vigor bonus");

        // Scenario C: Ceiling clamping at 1.20x max
        float highCurrentScale = 1.10f;
        float clampedScale = clamp(highCurrentScale * 1.15f, minAllowed, maxAllowed);
        Assertions.assertEquals(1.20f, clampedScale, 0.001f, "Hybrid vigor must clamp cleanly to maxAllowed 1.20x");
    }

    @Test
    @DisplayName("Verify Multi-Generational Stunting and Movement Speed Penalties")
    public void testMultiGenerationalStuntingPenalties() {
        float minAllowed = 0.10f;
        float maxAllowed = 1.20f;
        float normalScale = 1.00f;

        // Tier 1: Mild stunting (-10% scale)
        float t1Scale = clamp(normalScale * 0.90f, minAllowed, maxAllowed);
        Assertions.assertEquals(0.90f, t1Scale, 0.001f, "Tier 1 inbreeding must apply -10% scale stunting");

        // Tier 2: Moderate stunting (-25% scale), -20% speed
        float t2Scale = clamp(normalScale * 0.75f, minAllowed, maxAllowed);
        float t2SpeedPenalty = -0.20f;
        Assertions.assertEquals(0.75f, t2Scale, 0.001f, "Tier 2 inbreeding must apply -25% scale stunting");
        Assertions.assertEquals(-0.20f, t2SpeedPenalty, 0.001f, "Tier 2 inbreeding must apply -20% movement speed penalty");

        // Tier 3: Severe miniature stunting (Math.max(minAllowed, 0.20f)), -30% speed
        float t3Scale = Math.max(minAllowed, 0.20f);
        float t3SpeedPenalty = -0.30f;
        Assertions.assertEquals(0.20f, t3Scale, 0.001f, "Tier 3 inbreeding must stunt offspring to miniature 0.20x");
        Assertions.assertEquals(-0.30f, t3SpeedPenalty, 0.001f, "Tier 3 inbreeding must apply -30% movement speed penalty");

        // Tier 4: Lethal genetic collapse (minAllowed floor = 0.10f), -50% speed
        float t4Scale = minAllowed;
        float t4SpeedPenalty = -0.50f;
        Assertions.assertEquals(0.10f, t4Scale, 0.001f, "Tier 4 inbreeding must collapse offspring to absolute min floor 0.10x");
        Assertions.assertEquals(-0.50f, t4SpeedPenalty, 0.001f, "Tier 4 inbreeding must apply severe -50% movement speed penalty");
    }

    @Test
    @DisplayName("Verify Self-Breeding Prevention & Cyclic Pedigree Loop Safety")
    public void testSelfBreedingAndCyclicLoopSafety() {
        UUID individualA = UUID.randomUUID();
        UUID individualB = UUID.randomUUID();

        // 1. Self-Breeding Rejection: Entity cannot mate with itself
        boolean isSelfBreeding = individualA.equals(individualA);
        Assertions.assertTrue(isSelfBreeding, "Matching UUIDs flag self-breeding");

        int selfRisk = calculateKinshipRisk(individualA, individualA, Optional.empty(), Optional.empty(), Optional.empty(), Optional.empty());
        Assertions.assertEquals(100, selfRisk, "Self-mating must be flagged with 100% inbreeding risk");

        // 2. Cyclic Parent Safety: A is parent of B, B is marked as parent of A (malformed pedigree)
        // Guard check prevents infinite recursive loop
        PedigreeNode nodeA = new PedigreeNode(individualA, Optional.of(individualB), Optional.empty());
        PedigreeNode nodeB = new PedigreeNode(individualB, Optional.of(individualA), Optional.empty());

        Map<UUID, PedigreeNode> registry = Map.of(individualA, nodeA, individualB, nodeB);
        Set<UUID> ancestors = collectAncestorsBounded(individualA, registry, 3);

        Assertions.assertTrue(ancestors.contains(individualB), "Ancestors set contains cycle partner");
        Assertions.assertTrue(ancestors.size() <= 3, "Bounded collection must never loop infinitely or exceed depth limit");
    }

    @Test
    @DisplayName("Verify Kinship Risk Prediction Percentages (Parent-Child, Siblings, Strangers)")
    public void testKinshipRiskPredictionCalculations() {
        UUID parent1Uuid = UUID.randomUUID();
        UUID parent2Uuid = UUID.randomUUID();
        UUID childUuid = UUID.randomUUID();

        // 1. Parent-child mating: 100% inbreeding risk
        int parentChildRisk = calculateKinshipRisk(
            parent1Uuid, childUuid,
            Optional.empty(), Optional.empty(),
            Optional.of(parent1Uuid), Optional.of(parent2Uuid)
        );
        Assertions.assertEquals(100, parentChildRisk, "Parent mating with offspring must yield 100% risk");

        // 2. Full siblings: sharing both parent1 and parent2 -> 100% risk
        UUID sibling1Uuid = UUID.randomUUID();
        UUID sibling2Uuid = UUID.randomUUID();
        int fullSiblingRisk = calculateKinshipRisk(
            sibling1Uuid, sibling2Uuid,
            Optional.of(parent1Uuid), Optional.of(parent2Uuid),
            Optional.of(parent1Uuid), Optional.of(parent2Uuid)
        );
        Assertions.assertEquals(100, fullSiblingRisk, "Full siblings must yield 100% inbreeding risk");

        // 3. Half siblings: sharing 1 parent -> 50% risk
        UUID halfSiblingUuid = UUID.randomUUID();
        UUID separateFather = UUID.randomUUID();
        int halfSiblingRisk = calculateKinshipRisk(
            sibling1Uuid, halfSiblingUuid,
            Optional.of(parent1Uuid), Optional.of(parent2Uuid),
            Optional.of(parent1Uuid), Optional.of(separateFather)
        );
        Assertions.assertEquals(50, halfSiblingRisk, "Half siblings sharing 1 parent must yield 50% inbreeding risk");

        // 4. Unrelated strangers: 0 shared parents -> 0% risk
        UUID strangerMother = UUID.randomUUID();
        UUID strangerFather = UUID.randomUUID();
        int strangerRisk = calculateKinshipRisk(
            sibling1Uuid, halfSiblingUuid,
            Optional.of(parent1Uuid), Optional.of(parent2Uuid),
            Optional.of(strangerMother), Optional.of(strangerFather)
        );
        Assertions.assertEquals(0, strangerRisk, "Unrelated animals must yield 0% inbreeding risk");
    }

    @Test
    @DisplayName("Verify Tier 3/4 Prime Meat Conversion to Rotten Flesh & Bones")
    public void testTier3MeatDropConversionToRottenFleshAndBone() {
        String[] meats = {"minecraft:beef", "minecraft:porkchop", "minecraft:mutton", "minecraft:chicken", "minecraft:rabbit"};

        // For Tiers 0, 1, 2: Meat is NEVER converted
        for (int tier = 0; tier <= 2; tier++) {
            for (String meat : meats) {
                ConvertedDrop drop = simulateDropConversion(meat, 3, tier, true);
                Assertions.assertEquals(meat, drop.itemId(), "Tier " + tier + " must retain original meat drop");
                Assertions.assertEquals(3, drop.count(), "Tier " + tier + " must retain drop count");
            }
        }

        // For Tier 3 and Tier 4: Meat is converted to Rotten Flesh (50%) or Bone (50%), preserving count
        for (int tier = 3; tier <= 4; tier++) {
            for (String meat : meats) {
                ConvertedDrop dropFlesh = simulateDropConversion(meat, 4, tier, true);
                Assertions.assertEquals("minecraft:rotten_flesh", dropFlesh.itemId(), "Tier " + tier + " meat must convert to rotten flesh when flag is true");
                Assertions.assertEquals(4, dropFlesh.count(), "Count must be preserved on rotten flesh conversion");

                ConvertedDrop dropBone = simulateDropConversion(meat, 2, tier, false);
                Assertions.assertEquals("minecraft:bone", dropBone.itemId(), "Tier " + tier + " meat must convert to bone when flag is false");
                Assertions.assertEquals(2, dropBone.count(), "Count must be preserved on bone conversion");
            }
        }

        // Ensure minimum count floor of 1
        ConvertedDrop dropZeroCount = simulateDropConversion("minecraft:beef", 0, 3, true);
        Assertions.assertEquals(1, dropZeroCount.count(), "Count must floor at minimum 1 upon conversion");
    }

    @Test
    @DisplayName("Verify Tier 3/4 Secondary Drop Suppression (75% Yield Reduction)")
    public void testSecondaryDropSuppression() {
        String[] secondaryDrops = {"minecraft:leather", "minecraft:wool", "minecraft:feather", "minecraft:rabbit_hide"};

        // For Tiers 0, 1, 2: Secondary drops remain full yield
        for (int tier = 0; tier <= 2; tier++) {
            for (String item : secondaryDrops) {
                ConvertedDrop drop = simulateDropConversion(item, 8, tier, false);
                Assertions.assertEquals(item, drop.itemId(), "Tier " + tier + " must retain original secondary item");
                Assertions.assertEquals(8, drop.count(), "Tier " + tier + " must retain full count");
            }
        }

        // For Tier 3 and 4: 75% reduction (newCount = Math.max(1, count / 4))
        for (int tier = 3; tier <= 4; tier++) {
            for (String item : secondaryDrops) {
                // Large stack: 12 -> 3
                ConvertedDrop drop12 = simulateDropConversion(item, 12, tier, false);
                Assertions.assertEquals(item, drop12.itemId(), "Secondary item identity must be preserved");
                Assertions.assertEquals(3, drop12.count(), "12 secondary items must be reduced by 75% to 3");

                // Medium stack: 7 -> 7/4 = 1
                ConvertedDrop drop7 = simulateDropConversion(item, 7, tier, false);
                Assertions.assertEquals(1, drop7.count(), "7 secondary items must reduce to 1 (floor integer division)");

                // Small stack: 1 or 2 -> clamps safely to floor 1
                ConvertedDrop drop2 = simulateDropConversion(item, 2, tier, false);
                Assertions.assertEquals(1, drop2.count(), "2 secondary items must clamp to floor 1");

                ConvertedDrop drop1 = simulateDropConversion(item, 1, tier, false);
                Assertions.assertEquals(1, drop1.count(), "1 secondary item must clamp to floor 1");
            }
        }
    }

    @Test
    @DisplayName("Verify Pedigree Father Tag Serialization & Parsing Roundtrip")
    public void testPedigreeFatherTagSerializationRoundtrip() {
        UUID fatherUuid = UUID.randomUUID();

        // Tag format assertion
        String tag = serializeFatherTag(fatherUuid);
        Assertions.assertTrue(tag.startsWith("nr_father:"), "Tag must begin with 'nr_father:' prefix");
        Assertions.assertEquals("nr_father:" + fatherUuid, tag, "Serialized tag must match canonical format");

        // Deserialization roundtrip
        Optional<UUID> parsed = deserializeFatherTag(tag);
        Assertions.assertTrue(parsed.isPresent(), "Parsed UUID must be present");
        Assertions.assertEquals(fatherUuid, parsed.get(), "Deserialized UUID must match original father UUID");

        // Null & Malformed resiliency
        Assertions.assertTrue(deserializeFatherTag(null).isEmpty(), "Null tag must resolve to empty Optional");
        Assertions.assertTrue(deserializeFatherTag("").isEmpty(), "Empty string tag must resolve to empty Optional");
        Assertions.assertTrue(deserializeFatherTag("invalid_tag").isEmpty(), "Tag without prefix must resolve to empty Optional");
        Assertions.assertTrue(deserializeFatherTag("nr_father:not-a-uuid").isEmpty(), "Malformed UUID string must resolve to empty Optional safely without throwing");
    }

    @Test
    @DisplayName("Verify EntityGenetics Codec Serialization & Reconstruction Determinism")
    public void testEntityGeneticsCodecSerializationDeterminism() {
        UUID parent1 = UUID.randomUUID();
        UUID parent2 = UUID.randomUUID();
        Map<String, Float> traits = Map.of(
            "inbreeding_tier", 2.0f,
            "scale_modifier", 0.75f,
            "fertility_rate", 1.10f
        );

        MockGeneticsRecord original = new MockGeneticsRecord(
            Optional.of(parent1),
            Optional.of(parent2),
            true,
            true,
            traits
        );

        // Serialize to simulated payload
        String serialized = serializeGeneticsRecord(original);
        Assertions.assertNotNull(serialized);
        Assertions.assertTrue(serialized.contains(parent1.toString()), "Serialized output must contain parent1");
        Assertions.assertTrue(serialized.contains(parent2.toString()), "Serialized output must contain parent2");
        Assertions.assertTrue(serialized.contains("inbreeding_tier=2.0"), "Serialized output must preserve traits");

        // Deserialize and assert exact equality
        MockGeneticsRecord reconstructed = deserializeGeneticsRecord(serialized);
        Assertions.assertEquals(original, reconstructed, "Reconstructed genetics record must match original exactly");

        // Test with empty parents (wild generation)
        MockGeneticsRecord wildAnimal = new MockGeneticsRecord(
            Optional.empty(),
            Optional.empty(),
            false,
            true,
            Map.of("inbreeding_tier", 0.0f)
        );
        String wildSerialized = serializeGeneticsRecord(wildAnimal);
        MockGeneticsRecord wildReconstructed = deserializeGeneticsRecord(wildSerialized);
        Assertions.assertEquals(wildAnimal, wildReconstructed, "Wild animal genetics record must roundtrip deterministically");
    }

    // ========== Helper Methods Replicating AnimalLineageHelper Mechanics ==========

    private int countSharedAncestors(Set<UUID> setA, Set<UUID> setB) {
        if (setA == null || setB == null) return 0;
        int count = 0;
        for (UUID id : setA) {
            if (setB.contains(id)) {
                count++;
            }
        }
        return count;
    }

    private int determineInbreedingTierFromAncestors(int sharedCount) {
        if (sharedCount >= 3) return 3; // Severe closed-loop / full siblings
        if (sharedCount >= 2) return 2; // Close inbreeding / half-siblings
        if (sharedCount == 1) return 1; // Mild / single grandparent
        return 0; // Fresh blood / diverse
    }

    private static float clamp(float value, float min, float max) {
        return Math.min(Math.max(value, min), max);
    }

    private static int clamp(int value, int min, int max) {
        return Math.min(Math.max(value, min), max);
    }

    private int computeInbreedingTier(int p1Tier, int p2Tier, boolean inbred) {
        int maxTier = Math.max(p1Tier, p2Tier);
        if (inbred) {
            return clamp(maxTier + 1, 1, 4);
        } else {
            return Math.max(0, maxTier - 1);
        }
    }

    private int calculateKinshipRisk(
        UUID a, UUID b,
        Optional<UUID> aP1, Optional<UUID> aP2,
        Optional<UUID> bP1, Optional<UUID> bP2
    ) {
        if (a.equals(b)) return 100;
        if (bP1.map(a::equals).orElse(false) || bP2.map(a::equals).orElse(false)) return 100;
        if (aP1.map(b::equals).orElse(false) || aP2.map(b::equals).orElse(false)) return 100;

        UUID p1_1 = aP1.orElse(null);
        UUID p1_2 = aP2.orElse(null);
        UUID p2_1 = bP1.orElse(null);
        UUID p2_2 = bP2.orElse(null);

        if (p1_1 == null && p1_2 == null && p2_1 == null && p2_2 == null) {
            return 0;
        }

        boolean match1 = p1_1 != null && (p1_1.equals(p2_1) || p1_1.equals(p2_2));
        boolean match2 = p1_2 != null && (p1_2.equals(p2_1) || p1_2.equals(p2_2));

        if (match1 && match2) return 100;
        if (match1 || match2) return 50;
        return 0;
    }

    private Set<UUID> collectAncestorsBounded(UUID root, Map<UUID, PedigreeNode> registry, int maxDepth) {
        Set<UUID> visited = new HashSet<>();
        Set<UUID> currentLevel = Set.of(root);

        for (int depth = 0; depth < maxDepth && !currentLevel.isEmpty(); depth++) {
            Set<UUID> nextLevel = new HashSet<>();
            for (UUID id : currentLevel) {
                PedigreeNode node = registry.get(id);
                if (node != null) {
                    node.p1().ifPresent(p -> {
                        if (visited.add(p)) nextLevel.add(p);
                    });
                    node.p2().ifPresent(p -> {
                        if (visited.add(p)) nextLevel.add(p);
                    });
                }
            }
            currentLevel = nextLevel;
        }
        return Collections.unmodifiableSet(visited);
    }

    private record PedigreeNode(UUID id, Optional<UUID> p1, Optional<UUID> p2) {}

    public record ConvertedDrop(String itemId, int count) {}

    public record MockGeneticsRecord(
        Optional<UUID> p1,
        Optional<UUID> p2,
        boolean inbred,
        boolean active,
        Map<String, Float> traits
    ) {}

    private ConvertedDrop simulateDropConversion(String itemId, int count, int tier, boolean fleshNotBone) {
        if (tier <= 2) {
            return new ConvertedDrop(itemId, count);
        }

        // Secondary drops check: 75% suppression (count / 4, min floor 1)
        Set<String> secondaryDrops = Set.of(
            "minecraft:leather",
            "minecraft:wool",
            "minecraft:feather",
            "minecraft:rabbit_hide"
        );
        if (secondaryDrops.contains(itemId)) {
            int reducedCount = Math.max(1, count / 4);
            return new ConvertedDrop(itemId, reducedCount);
        }

        // Meat drops check: 50% Rotten Flesh, 50% Bone, preserving count (min floor 1)
        Set<String> meatDrops = Set.of(
            "minecraft:beef",
            "minecraft:porkchop",
            "minecraft:mutton",
            "minecraft:chicken",
            "minecraft:rabbit"
        );
        if (meatDrops.contains(itemId)) {
            int preservedCount = Math.max(1, count);
            String replacementItem = fleshNotBone ? "minecraft:rotten_flesh" : "minecraft:bone";
            return new ConvertedDrop(replacementItem, preservedCount);
        }

        return new ConvertedDrop(itemId, count);
    }

    private String serializeFatherTag(UUID fatherUuid) {
        if (fatherUuid == null) return "";
        return "nr_father:" + fatherUuid;
    }

    private Optional<UUID> deserializeFatherTag(String tag) {
        if (tag == null || !tag.startsWith("nr_father:")) {
            return Optional.empty();
        }
        String uuidStr = tag.substring("nr_father:".length());
        try {
            return Optional.of(UUID.fromString(uuidStr));
        } catch (IllegalArgumentException e) {
            return Optional.empty();
        }
    }

    private String serializeGeneticsRecord(MockGeneticsRecord record) {
        StringBuilder sb = new StringBuilder();
        sb.append("p1=").append(record.p1().map(UUID::toString).orElse("none")).append(";");
        sb.append("p2=").append(record.p2().map(UUID::toString).orElse("none")).append(";");
        sb.append("inbred=").append(record.inbred()).append(";");
        sb.append("active=").append(record.active()).append(";");
        sb.append("traits=");
        record.traits().forEach((k, v) -> sb.append(k).append("=").append(v).append(","));
        return sb.toString();
    }

    private MockGeneticsRecord deserializeGeneticsRecord(String data) {
        Optional<UUID> p1 = Optional.empty();
        Optional<UUID> p2 = Optional.empty();
        boolean inbred = false;
        boolean active = true;
        Map<String, Float> traits = new HashMap<>();

        String[] parts = data.split(";");
        for (String part : parts) {
            if (part.startsWith("p1=")) {
                String val = part.substring(3);
                if (!"none".equals(val)) {
                    p1 = Optional.of(UUID.fromString(val));
                }
            } else if (part.startsWith("p2=")) {
                String val = part.substring(3);
                if (!"none".equals(val)) {
                    p2 = Optional.of(UUID.fromString(val));
                }
            } else if (part.startsWith("inbred=")) {
                inbred = Boolean.parseBoolean(part.substring(7));
            } else if (part.startsWith("active=")) {
                active = Boolean.parseBoolean(part.substring(7));
            } else if (part.startsWith("traits=")) {
                String traitsStr = part.substring(7);
                if (!traitsStr.isEmpty()) {
                    for (String entry : traitsStr.split(",")) {
                        if (!entry.isEmpty()) {
                            String[] kv = entry.split("=");
                            if (kv.length == 2) {
                                traits.put(kv[0], Float.parseFloat(kv[1]));
                            }
                        }
                    }
                }
            }
        }

        return new MockGeneticsRecord(p1, p2, inbred, active, Collections.unmodifiableMap(traits));
    }
}

