package com.fouan.weapon;

public abstract class Weapon {
//    private final boolean dual = true;
//    private final boolean noisy = false;
//    private final boolean canOpenDoor = true;
//    private final boolean noisyOpeningDoor = true;
    private final int damage = 1;
    protected final int range;
    protected final int dice;
    protected final int accuracy;

    protected Weapon(int range, int dice, int accuracy) {
        this.range = range;
        this.dice = dice;
        this.accuracy = accuracy;
    }

    public abstract AttackResult use();

    public int getRange() {
        return range;
    }

    public int getDamage() {
        return damage;
    }
}
