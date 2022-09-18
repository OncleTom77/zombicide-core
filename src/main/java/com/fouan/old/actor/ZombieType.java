package com.fouan.old.actor;

public enum ZombieType {
    WALKER("Walker", 2, 1, 1),
    FATTY("Faty", 3, 2, 1),
    RUNNER("Runner", 1, 1, 1),
    ABOMINATION("Abomination", 3, 3, 5),
    NECROMANCER("Necromancer", 2, 1, 1),
    ;

    private final String name;
    private final int damage;
    private final int minDamageToDestroy;
    private final int experienceProvided;

    ZombieType(String name, int damage, int minDamageToDestroy, int experienceProvided) {
        this.name = name;
        this.damage = damage;
        this.minDamageToDestroy = minDamageToDestroy;
        this.experienceProvided = experienceProvided;
    }

    public int getDamage() {
        return damage;
    }

    public int getMinDamageToDestroy() {
        return minDamageToDestroy;
    }

    @Override
    public String toString() {
        return name;
    }
}
