package com.fouan.weapon;

import com.fouan.io.Output;

import javax.inject.Named;

@Named
public class Axe implements Weapon {
//    private final boolean dual = true;
//    private final boolean noisy = false;
//    private final boolean canOpenDoor = true;
//    private final boolean noisyOpeningDoor = true;
//    private final int range = 0;
//    private final int dice = 1;
    private static final int accuracy = 4;
//    private final int damage = 1;

    private final DiceRoller diceRoller;
    private final Output output;

    public Axe(DiceRoller diceRoller, Output output) {
        this.diceRoller = diceRoller;
        this.output = output;
    }

    @Override
    public boolean hit() {
        int roll = diceRoller.roll();
        output.display("Die: " + roll);
        return roll >= accuracy;
    }
}
