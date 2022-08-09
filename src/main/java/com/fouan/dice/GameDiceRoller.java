package com.fouan.dice;

import lombok.AllArgsConstructor;

import javax.inject.Named;
import java.util.Random;

@Named
@AllArgsConstructor
final class GameDiceRoller implements DiceRoller {

    private final Random random;

    @Override
    public int roll() {
        return random.nextInt(6) + 1;
    }
}
