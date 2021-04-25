package com.fouan.actor;

public enum ZombieType {
    WALKER(2, 1, 1),
    FATTY(3, 2, 1),
    RUNNER(1, 1, 1),
    ABOMINATION(3, 3, 5),
    NECROMANCER(2, 1, 1),
    ;

    private final int damage;
    private final int minDamageToDestroy;
    private final int experienceProvided;

    ZombieType(int damage, int minDamageToDestroy, int experienceProvided) {
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
}
