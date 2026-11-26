// Copyright (C) 2026 Dasik (Rifaditya) | GNU GPLv3
package net.vanillaoutsider.naturalreproduction;

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.io.ByteArrayInputStream;
import java.io.DataInputStream;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Stream;

import static org.junit.jupiter.api.Assertions.*;

/**
 * NaturalReproductionSidednessContractTest: Pillar 5 Sidedness, Manifest & License Hygiene Contract Test Suite.
 *
 * Verifies across all version anchors:
 * - Line 1 of all .java files has the exact GPLv3 license header.
 * - Common sources & bytecode contain zero unguarded client package imports or constant pool references.
 * - Obsolete version guard reflection is purged and zero unremapped Class.forName calls exist in entrypoint.
 * - fabric.mod.json declares universal environment ("*") and complete entrypoints ("main", "modmenu").
 * - natural-reproduction.mixins.json exists and all registered mixin classes exist in source.
 */
public class NaturalReproductionSidednessContractTest {

    private static final String EXPECTED_LICENSE = "// Copyright (C) 2026 Dasik (Rifaditya) | GNU GPLv3";

    private static Path findProjectRoot() {
        Path dir = Paths.get(System.getProperty("user.dir"));
        if (Files.exists(dir.resolve("src/main/resources/fabric.mod.json"))) {
            return dir;
        }
        Path candidateSub = dir.resolve("Natural Reproduction v26.2/Natural Reproduction 26.2");
        if (Files.exists(candidateSub.resolve("src/main/resources/fabric.mod.json"))) {
            return candidateSub;
        }
        Path candidateDelayed = dir.resolve("Delayed Gratification Collection/Natural Reproduction/Natural Reproduction v26.2/Natural Reproduction 26.2");
        if (Files.exists(candidateDelayed.resolve("src/main/resources/fabric.mod.json"))) {
            return candidateDelayed;
        }
        Path current = dir;
        while (current != null) {
            Path sub = current.resolve("Natural Reproduction v26.2/Natural Reproduction 26.2");
            if (Files.exists(sub.resolve("src/main/resources/fabric.mod.json"))) {
                return sub;
            }
            if (Files.exists(current.resolve("src/main/resources/fabric.mod.json"))) {
                return current;
            }
            current = current.getParent();
        }
        return dir;
    }

    @Test
    @DisplayName("Pillar 5: Exact single-line GPLv3 license header present on line 1 across all sources")
    void testLicenseHeaders() throws IOException {
        Path root = findProjectRoot();
        Path[] sourceRoots = new Path[] {
            root.resolve("src/main/java"),
            root.resolve("src/test/java")
        };

        for (Path srcRoot : sourceRoots) {
            if (!Files.exists(srcRoot)) {
                continue;
            }
            try (Stream<Path> stream = Files.walk(srcRoot)) {
                List<Path> javaFiles = stream.filter(p -> p.toString().endsWith(".java")).toList();
                assertFalse(javaFiles.isEmpty(), "Java source files must exist in " + srcRoot);
                for (Path file : javaFiles) {
                    List<String> lines = Files.readAllLines(file, StandardCharsets.UTF_8);
                    assertFalse(lines.isEmpty(), "Java file is empty: " + file);
                    String line1 = lines.get(0).trim();
                    assertEquals(EXPECTED_LICENSE, line1,
                        "Missing or incorrect license header on line 1 in: " + file.toString().replace('\\', '/'));
                }
            }
        }
    }

    @Test
    @DisplayName("Pillar 5: Common sources contain zero unguarded client package imports")
    void testCommonSourceSidedness() throws IOException {
        Path root = findProjectRoot();
        Path srcMain = root.resolve("src/main/java");
        assertTrue(Files.exists(srcMain), "src/main/java must exist");

        try (Stream<Path> stream = Files.walk(srcMain)) {
            List<Path> javaFiles = stream.filter(p -> p.toString().endsWith(".java")).toList();
            assertFalse(javaFiles.isEmpty(), "Java source files must exist in src/main/java");

            for (Path file : javaFiles) {
                String normalized = file.toString().replace('\\', '/');
                if (normalized.contains("/client/") || normalized.endsWith("Client.java")) {
                    continue;
                }
                String content = Files.readString(file, StandardCharsets.UTF_8);
                if (content.contains("@Environment(EnvType.CLIENT)")) {
                    continue;
                }

                assertFalse(content.contains("net.minecraft.client."),
                    "Forbidden client package reference in common source file: " + normalized);
                assertFalse(content.contains("com.mojang.blaze3d."),
                    "Forbidden Blaze3D reference in common source file: " + normalized);
            }
        }
    }

    @Test
    @DisplayName("Pillar 5: Common bytecode contains zero unguarded client package references")
    void testCommonBytecodeSidedness() throws IOException {
        Path root = findProjectRoot();
        Path classesRoot = root.resolve("build/classes/java/main");
        if (Files.exists(classesRoot)) {
            try (Stream<Path> stream = Files.walk(classesRoot)) {
                List<Path> classFiles = stream.filter(p -> p.toString().endsWith(".class")).toList();
                for (Path classFile : classFiles) {
                    String normalized = classFile.toString().replace('\\', '/');
                    if (normalized.contains("/client/")
                            || normalized.contains("ModMenuIntegration")
                            || normalized.contains("YaclScreenHelper")) {
                        continue;
                    }
                    String rawBytes = new String(Files.readAllBytes(classFile), StandardCharsets.ISO_8859_1);
                    assertFalse(rawBytes.contains("net/minecraft/client/"),
                        "Forbidden net/minecraft/client/ constant pool reference in common bytecode: " + normalized);
                    assertFalse(rawBytes.contains("com/mojang/blaze3d/"),
                        "Forbidden com/mojang/blaze3d/ constant pool reference in common bytecode: " + normalized);
                }
            }
        }
    }

    @Test
    @DisplayName("Pillar 5: Obsolete reflection version guard is purged and zero unremapped Class.forName in entrypoint")
    void testPurgedReflectionHygiene() throws IOException {
        Path root = findProjectRoot();
        Path guardPath = root.resolve("src/main/java/net/vanillaoutsider/naturalreproduction/util/" + "ModVersion" + "Guard.java");
        assertFalse(Files.exists(guardPath), "Reflection guard class must not exist");

        Path entrypointPath = root.resolve("src/main/java/net/vanillaoutsider/naturalreproduction/NaturalReproductionFabric.java");
        assertTrue(Files.exists(entrypointPath), "NaturalReproductionFabric.java must exist");
        String content = Files.readString(entrypointPath, StandardCharsets.UTF_8);
        assertFalse(content.contains("ModVersion" + "Guard"), "NaturalReproductionFabric.java must not reference legacy version guard");
        assertFalse(content.contains("Class.forName"), "NaturalReproductionFabric.java must not contain Class.forName");
    }

    @Test
    @DisplayName("Pillar 5: fabric.mod.json declares universal environment and required entrypoints")
    void testFabricModJsonHygiene() throws IOException {
        Path root = findProjectRoot();
        Path fabricModPath = root.resolve("src/main/resources/fabric.mod.json");
        assertTrue(Files.exists(fabricModPath), "fabric.mod.json must exist");

        String content = Files.readString(fabricModPath, StandardCharsets.UTF_8);
        JsonObject json = JsonParser.parseString(content).getAsJsonObject();

        assertTrue(json.has("environment"), "fabric.mod.json must declare 'environment'");
        assertEquals("*", json.get("environment").getAsString(), "fabric.mod.json 'environment' must be '*'");

        assertTrue(json.has("entrypoints"), "fabric.mod.json must declare 'entrypoints'");
        JsonObject entrypoints = json.getAsJsonObject("entrypoints");
        assertTrue(entrypoints.has("main"), "entrypoints must contain 'main'");
        assertTrue(entrypoints.has("modmenu"), "entrypoints must contain 'modmenu'");
    }

    @Test
    @DisplayName("Pillar 5: natural-reproduction.mixins.json exists and all registered mixin classes exist")
    void testMixinDescriptorIntegrity() throws IOException {
        Path root = findProjectRoot();
        Path mixinConfigPath = root.resolve("src/main/resources/natural-reproduction.mixins.json");
        assertTrue(Files.exists(mixinConfigPath), "natural-reproduction.mixins.json must exist");

        String content = Files.readString(mixinConfigPath, StandardCharsets.UTF_8);
        JsonObject json = JsonParser.parseString(content).getAsJsonObject();

        assertTrue(json.has("package"), "Mixin config must declare 'package'");
        String pkg = json.get("package").getAsString().replace('.', '/');

        List<String> mixinNames = new ArrayList<>();
        if (json.has("mixins") && json.get("mixins").isJsonArray()) {
            JsonArray mixinsArray = json.getAsJsonArray("mixins");
            for (JsonElement elem : mixinsArray) {
                mixinNames.add(elem.getAsString());
            }
        }
        if (json.has("client") && json.get("client").isJsonArray()) {
            JsonArray clientArray = json.getAsJsonArray("client");
            for (JsonElement elem : clientArray) {
                mixinNames.add(elem.getAsString());
            }
        }
        if (json.has("server") && json.get("server").isJsonArray()) {
            JsonArray serverArray = json.getAsJsonArray("server");
            for (JsonElement elem : serverArray) {
                mixinNames.add(elem.getAsString());
            }
        }

        assertFalse(mixinNames.isEmpty(), "natural-reproduction.mixins.json must declare mixin classes");

        Path srcMain = root.resolve("src/main/java");
        for (String mixinName : mixinNames) {
            Path classPath = srcMain.resolve(pkg).resolve(mixinName + ".java");
            assertTrue(Files.exists(classPath), "Mixin class file must exist in source: " + classPath);
        }
    }

    @Test
    @DisplayName("Pillar 5: YaclScreenHelper bytecode invokes ClientSupportScreenHelper.openKofi(Screen) with zero obsolete refs")
    void testYaclScreenHelperKofiAbiParity() throws IOException {
        Path root = findProjectRoot();
        Path classesRoot = root.resolve("build/classes/java/main");
        if (!Files.exists(classesRoot)) {
            return;
        }
        Path clientClassesDir = classesRoot.resolve("net/vanillaoutsider/naturalreproduction/client");
        Path yaclClass = clientClassesDir.resolve("YaclScreenHelper.class");
        assertTrue(Files.exists(yaclClass), "Compiled YaclScreenHelper.class must exist at: " + yaclClass);

        String obsoleteOwner = "net/dasik/social/api/config/" + "DasikSupport" + "Helper";
        String methodName = "open" + "Kofi";
        String screenDesc = "(Lnet/minecraft/client/gui/screens/Screen;)V";
        String obsoleteRef = obsoleteOwner + ";" + methodName + screenDesc;

        String expectedOwner = "net/dasik/social/api/client/gui/ClientSupportScreenHelper";
        String expectedRef = expectedOwner + ";" + methodName + screenDesc;

        List<Path> yaclClasses;
        try (Stream<Path> stream = Files.list(clientClassesDir)) {
            yaclClasses = stream
                .filter(p -> {
                    String name = p.getFileName().toString();
                    return name.equals("YaclScreenHelper.class")
                        || (name.startsWith("YaclScreenHelper$") && name.endsWith(".class"));
                })
                .toList();
        }
        assertFalse(yaclClasses.isEmpty(), "YaclScreenHelper class files must exist");

        int expectedMethodRefCount = 0;
        Set<String> allUtf8 = new HashSet<>();

        for (Path classFile : yaclClasses) {
            byte[] bytes = Files.readAllBytes(classFile);
            try (DataInputStream dis = new DataInputStream(new ByteArrayInputStream(bytes))) {
                int magic = dis.readInt();
                assertEquals(0xCAFEBABE, magic, "Invalid class file magic in: " + classFile);
                dis.readUnsignedShort(); // minor_version
                dis.readUnsignedShort(); // major_version
                int cpCount = dis.readUnsignedShort();

                int[] tags = new int[cpCount];
                String[] utf8 = new String[cpCount];
                int[] firstIdx = new int[cpCount];
                int[] secondIdx = new int[cpCount];

                for (int i = 1; i < cpCount; i++) {
                    int tag = dis.readUnsignedByte();
                    tags[i] = tag;
                    switch (tag) {
                        case 1:
                            utf8[i] = dis.readUTF();
                            allUtf8.add(utf8[i]);
                            break;
                        case 3:
                        case 4:
                            dis.readInt();
                            break;
                        case 5:
                        case 6:
                            dis.readLong();
                            i++;
                            break;
                        case 7:
                        case 8:
                        case 16:
                        case 19:
                        case 20:
                            firstIdx[i] = dis.readUnsignedShort();
                            break;
                        case 9:
                        case 10:
                        case 11:
                        case 12:
                        case 17:
                        case 18:
                            firstIdx[i] = dis.readUnsignedShort();
                            secondIdx[i] = dis.readUnsignedShort();
                            break;
                        case 15:
                            dis.readUnsignedByte();
                            dis.readUnsignedShort();
                            break;
                        default:
                            fail("Unknown constant pool tag " + tag + " in " + classFile);
                    }
                }

                for (int i = 1; i < cpCount; i++) {
                    if (tags[i] == 10 || tags[i] == 11) {
                        int classIdx = firstIdx[i];
                        int natIdx = secondIdx[i];
                        String owner = utf8[firstIdx[classIdx]];
                        String invokedName = utf8[firstIdx[natIdx]];
                        String invokedDesc = utf8[secondIdx[natIdx]];
                        String fullRef = owner + ";" + invokedName + invokedDesc;
                        assertNotEquals(obsoleteRef, fullRef,
                            "Forbidden obsolete ABI reference found in " + classFile + ": " + fullRef);
                        if (expectedRef.equals(fullRef)) {
                            expectedMethodRefCount++;
                        }
                    }
                }
            }
        }

        assertTrue(allUtf8.contains(expectedOwner),
            "YaclScreenHelper constant pool UTF-8 must contain " + expectedOwner);
        assertTrue(allUtf8.contains(methodName),
            "YaclScreenHelper constant pool UTF-8 must contain " + methodName);
        assertTrue(allUtf8.contains(screenDesc),
            "YaclScreenHelper constant pool UTF-8 must contain " + screenDesc);
        assertTrue(expectedMethodRefCount >= 1,
            "YaclScreenHelper must invoke " + expectedRef + " at least once");
    }
}
