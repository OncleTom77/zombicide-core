package com.fouan.actor;

import com.fouan.board.Zone;
import com.fouan.game.ActorSelection;
import com.fouan.io.Output;
import com.fouan.weapon.Weapon;

import java.util.List;
import java.util.Random;

public class Zombie extends Actor {

    private final Random random;
    private final ZombieType type;

    public Zombie(Random random, Output output, ActorSelection actorSelection, Zone initialZone, ZombieType type) {
        super(output, actorSelection, initialZone);
        this.random = random;
        this.type = type;
    }

    public boolean canFight() {
        return zone.containsSurvivor();
    }

    public void moves() {
        List<Zone> possibleZones = zone.getConnectedZones();
        int randomZoneIndex = random.nextInt(possibleZones.size());
        Zone newZone = possibleZones.get(randomZoneIndex);

        changesZone(newZone);
    }

    public boolean canBeKilledByWeapon(Weapon weapon) {
        return weapon.getDamage() >= type.getMinDamageToDestroy();
    }

    public ZombieType getType() {
        return type;
    }
}