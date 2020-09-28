package com.fouan.character;

import com.fouan.board.Zone;
import com.fouan.io.Output;
import com.fouan.weapon.Weapon;

public class Survivor {
    public static final int LIFE_POINTS = 3;
    private final Output output;
    private Zone zone;
    private Weapon weapon;
    private int wounds;

    public Survivor(Zone initialZone, Weapon weapon, Output output) {
        this.zone = initialZone;
        this.weapon = weapon;
        this.output = output;
        wounds = 0;
    }

    public void changesZone(Zone zone) {
        this.zone.removeSurvivor(this);
        this.zone = zone;
        zone.addSurvivor(this);
    }

    public boolean attacks() {
        return weapon.hit();
    }

    public Zone getZone() {
        return zone;
    }

    public boolean canFight() {
        return zone.containsZombie();
    }

    public boolean isAlive() {
        return wounds < LIFE_POINTS;
    }

    public void suffersInjury() {
        wounds += 1;
    }

    public void displayWounds() {
        this.output.display("Wounds counter: " + wounds);
    }
}
