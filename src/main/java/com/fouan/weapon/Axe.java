package com.fouan.weapon;

import com.fouan.io.Output;

import javax.inject.Named;
import java.util.List;
import java.util.stream.IntStream;

import static java.util.stream.Collectors.toList;

@Named
public class Axe extends Weapon {
    private final DiceRoller diceRoller;
    private final Output output;

    public Axe(DiceRoller diceRoller, Output output) {
        super(0, 1, 4);
        this.diceRoller = diceRoller;
        this.output = output;
    }

    @Override
    public AttackResult use() {
        List<Integer> rolls = IntStream.range(0, dice)
                .map(value -> diceRoller.roll())
                .boxed()
                .collect(toList());
        output.display("Dice: " + rolls);

        long hitCount = rolls.stream()
                .filter(roll -> roll >= accuracy)
                .count();

        return new AttackResult(this, (int) hitCount);
    }
}
