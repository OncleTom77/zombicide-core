package com.fouan.weapon;

import com.fouan.io.Output;

import java.util.List;
import java.util.stream.IntStream;

public abstract class Weapon {
    //    private final boolean dual = true;
//    private final boolean noisy = false;
//    private final boolean canOpenDoor = true;
//    private final boolean noisyOpeningDoor = true;
    protected final Range range;
    protected final int dice;
    protected final int accuracy;
    protected final int damage;
    protected final DiceRoller diceRoller;
    protected final Output output;

    protected Weapon(Range range, int dice, int accuracy, int damage, DiceRoller diceRoller, Output output) {
        this.range = range;
        this.dice = dice;
        this.accuracy = accuracy;
        this.damage = damage;
        this.diceRoller = diceRoller;
        this.output = output;
    }

    public AttackResult use() {
        List<Integer> rolls = IntStream.range(0, dice)
                .map(value -> diceRoller.roll())
                .boxed()
                .toList();
        output.display("Dice: " + rolls);

        long hitCount = rolls.stream()
                .filter(roll -> roll >= accuracy)
                .count();

        return new AttackResult(this, (int) hitCount);
    }

    public Range getRange() {
        return range;
    }

    public boolean isMelee() {
        return range.getMax() == 0;
    }

    public int getDamage() {
        return damage;
    }
}
