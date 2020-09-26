package com.fouan.character;

import com.fouan.board.Zone;
import com.fouan.weapon.Weapon;

public class Survivor {
    private Zone zone;
    private Weapon weapon;

    public Survivor(Zone initialZone, Weapon weapon) {
        this.zone = initialZone;
        this.weapon = weapon;
    }

    public void changeZone(Zone zone) {
        this.zone = zone;
    }

    public boolean attack() {
        return weapon.hit();
    }

    public Zone getZone() {
        return zone;
    }

    public boolean canFight() {
        return zone.containsZombie();
    }
}
