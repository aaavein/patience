package net.aaavein.patience.util;

public final class SpeedCalculator {
    private static final float BASE_SPEED = 1.0F;
    private static final float SPEED_PER_LEVEL = 0.02F;
    private static final int MAX_LEVEL_BONUS = 200;

    public static float getCraftingSpeed(int experienceLevel) {
        int effectiveLevel = Math.min(experienceLevel, MAX_LEVEL_BONUS);
        return BASE_SPEED + SPEED_PER_LEVEL * effectiveLevel;
    }

    private SpeedCalculator() {}
}