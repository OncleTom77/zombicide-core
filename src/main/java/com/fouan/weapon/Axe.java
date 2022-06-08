package com.fouan.weapon;

import com.fouan.io.Output;

import javax.inject.Named;

@Named
public class Axe extends Weapon {

    public Axe(DiceRoller diceRoller, Output output) {
        super(new Range(0, 0), 1, 4, 1, diceRoller, output);
    }

    @Override
    public String toString() {
        return "Axe";
    }
}
