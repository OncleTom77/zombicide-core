package com.fouan.old.weapons;

public class AttackResult {
    private final Weapon weapon;
    private final int hitCount;

    public AttackResult(Weapon weapon, int hitCount) {
        this.weapon = weapon;
        this.hitCount = hitCount;
    }

    public Weapon getWeapon() {
        return weapon;
    }

    public int getHitCount() {
        return hitCount;
    }
}
