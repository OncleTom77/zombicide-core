package com.fouan.old.weapons;

import com.fouan.dice.DiceRoller;
import com.fouan.display.Output;

import javax.inject.Named;

@Named
public class WeaponFactory {

    private final Output output;
    private final DiceRoller diceRoller;

    public WeaponFactory(Output output, DiceRoller diceRoller) {
        this.output = output;
        this.diceRoller = diceRoller;
    }

    public Axe generateAxe() {
        return new Axe(diceRoller, output);
    }
}
