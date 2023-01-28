package com.fouan.weapons;

import com.fouan.dice.DiceRoller;

import java.util.stream.IntStream;

public abstract class Weapon {

    protected final Range range;
    protected final int dice;
    protected final int accuracy;
    protected final int damage;

    protected Weapon(Range range, int dice, int accuracy, int damage) {
        this.range = range;
        this.dice = dice;
        this.accuracy = accuracy;
        this.damage = damage;
    }

    public AttackResult use(DiceRoller diceRoller) {
        var rolls = IntStream.range(0, dice)
                .map(value -> diceRoller.roll())
                .boxed()
                .toList();

        var hitCount = rolls.stream()
                .filter(roll -> roll >= accuracy)
                .count();

        return new AttackResult(this, rolls, Math.toIntExact(hitCount));
    }

    public Range getRange() {
        return this.range;
    }

    public int getDice() {
        return this.dice;
    }

    public int getAccuracy() {
        return this.accuracy;
    }

    public int getDamage() {
        return this.damage;
    }
}
