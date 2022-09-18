package com.fouan.weapons;

import lombok.AllArgsConstructor;
import lombok.Getter;

import java.util.stream.IntStream;

import static lombok.AccessLevel.PROTECTED;

@Getter
@AllArgsConstructor(access = PROTECTED)
public abstract class Weapon {

    protected final Range range;
    protected final int dice;
    protected final int accuracy;
    protected final int damage;

    public AttackResult use(int diceResult) {
        var rolls = IntStream.range(0, dice)
                .map(value -> diceResult)
                .boxed()
                .toList();

        var hitCount = rolls.stream()
                .filter(roll -> roll >= accuracy)
                .count();

        return new AttackResult(this, Math.toIntExact(hitCount));
    }
}
