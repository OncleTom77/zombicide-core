package com.fouan.old.weapons;

import com.fouan.dice.DiceRoller;
import com.fouan.display.Output;
import com.fouan.weapons.Range;

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
