package com.fouan.actor;

import com.fouan.board.Zone;
import com.fouan.io.Output;
import com.fouan.weapon.Weapon;

public class Zombie extends Actor {

    private final ZombieType type;

    public Zombie(Output output, Zone initialZone, ZombieType type) {
        super(output, initialZone);
        this.type = type;
    }

    public boolean canFight() {
        return zone.containsSurvivor();
    }

    public boolean canBeKilledByWeapon(Weapon weapon) {
        return weapon.getDamage() >= type.getMinDamageToDestroy();
    }

    public ZombieType getType() {
        return type;
    }
}
