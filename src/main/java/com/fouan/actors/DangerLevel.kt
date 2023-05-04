package com.fouan.actors;

public enum DangerLevel {
    BLUE(6),
    YELLOW(18),
    ORANGE(42),
    RED(43);

    private final int experienceThreshold;

    DangerLevel(int experienceThreshold) {
        this.experienceThreshold = experienceThreshold;
    }

    public static DangerLevel fromExperience(int experience) {
        for (var value : values()) {
            if (experience <= value.experienceThreshold) {
                return value;
            }
        }

        throw new IllegalStateException("No danger level found");
    }
}
