package com.fouan.actors

enum class DangerLevel(private val experienceThreshold: Int) {
    BLUE(6),
    YELLOW(18),
    ORANGE(42),
    RED(43);

    companion object {
        @JvmStatic
        fun fromExperience(experience: Int): DangerLevel {
            for (value in values()) {
                if (experience <= value.experienceThreshold) {
                    return value
                }
            }
            throw IllegalStateException("No danger level found")
        }
    }
}
