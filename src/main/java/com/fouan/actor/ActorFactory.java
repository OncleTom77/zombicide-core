package com.fouan.actor;

import com.fouan.board.Zone;
import com.fouan.io.Output;
import com.fouan.weapon.Weapon;

import javax.inject.Named;

@Named
public class ActorFactory {

    private final Output output;

    public ActorFactory(Output output) {
        this.output = output;
    }

    public Zombie generateZombie(Zone initialZone, ZombieType type) {
        return new Zombie(output, initialZone, type);
    }

    public Survivor generateAsim(Zone initialZone, Weapon weapon) {
        return new Survivor(output, initialZone, "Asim", weapon);
    }

    public Survivor generateBerin(Zone initialZone, Weapon weapon) {
        return new Survivor(output, initialZone, "Berin", weapon);
    }
}
