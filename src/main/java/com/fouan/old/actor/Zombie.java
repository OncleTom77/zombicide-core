package com.fouan.old.actor;

import com.fouan.old.board.Zone;
import com.fouan.display.Output;
import com.fouan.old.weapons.Weapon;

import java.util.Objects;

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

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Zombie zombie = (Zombie) o;
        return type == zombie.type && Objects.equals(zone, zombie.zone);
    }

    @Override
    public int hashCode() {
        return Objects.hash(type, zone);
    }

}
