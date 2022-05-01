package com.fouan.actor;

import com.fouan.board.Zone;
import com.fouan.game.ActorSelection;
import com.fouan.io.Output;
import com.fouan.weapon.Weapon;

import javax.inject.Named;
import java.util.Random;

@Named
public class ActorFactory {

    private final Random random;
    private final Output output;
    private final ActorSelection actorSelection;

    public ActorFactory(Random random, Output output, ActorSelection actorSelection) {
        this.random = random;
        this.output = output;
        this.actorSelection = actorSelection;
    }

    public Zombie generateZombie(Zone initialZone, ZombieType type) {
        return new Zombie(random, output, actorSelection, initialZone, type);
    }

    public Survivor generateAsim(Zone initialZone, Weapon weapon) {
        return new Survivor(output, actorSelection, initialZone, "Asim", weapon);
    }

    public Survivor generateBerin(Zone initialZone, Weapon weapon) {
        return new Survivor(output, actorSelection, initialZone, "Berin", weapon);
    }
}
