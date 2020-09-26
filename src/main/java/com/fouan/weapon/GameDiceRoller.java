package com.fouan.weapon;

import javax.inject.Named;
import java.util.Random;

@Named
public class GameDiceRoller implements DiceRoller {

    private final Random random;

    public GameDiceRoller(Random random) {
        this.random = random;
    }

    @Override
    public int roll() {
        return random.nextInt(6) + 1;
    }
}
