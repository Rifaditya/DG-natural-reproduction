// Copyright (C) 2026 Dasik (Rifaditya) | GNU GPLv3
// Verified against: Minecraft 26.2
package net.vanillaoutsider.naturalreproduction;

public final class ModVersionGuard {
    private ModVersionGuard() {}

    public static void checkClass(String modName, String requiredClassName) {
        try {
            Class.forName(requiredClassName, true, Thread.currentThread().getContextClassLoader());
        } catch (ClassNotFoundException e) {
            throw new RuntimeException("\n" +
                "=====================================================================\n" +
                " [" + modName + "] Minecraft API Mismatch!\n" +
                " A required Minecraft class (" + requiredClassName + ") was not found.\n" +
                " Try updating your Minecraft version one drop at a time until it works, or\n" +
                " download a matching JAR for your Minecraft release from Modrinth or CurseForge.\n" +
                "=====================================================================");
        }
    }
}
