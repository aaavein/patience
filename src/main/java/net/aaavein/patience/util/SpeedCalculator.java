package net.aaavein.patience.util;

public final class SpeedCalculator {

    public static float getCraftingSpeed(int experienceLevel, float baseSpeed, float speedPerLevel, int maxLevel) {
        int effectiveLevel = Math.min(experienceLevel, maxLevel);
        return baseSpeed + (speedPerLevel * effectiveLevel);
    }

    private SpeedCalculator() {}
}