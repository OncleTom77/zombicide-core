package com.fouan.actor;

import com.fouan.board.Zone;
import com.fouan.command.MoveCommand;
import com.fouan.io.Output;
import com.fouan.weapon.Weapon;

import java.util.List;
import java.util.Random;

public class Zombie extends Actor {

    private final Random random;
    private final ZombieType type;

    public Zombie(Random random, Output output, Zone initialZone, ZombieType type) {
        super(output, initialZone);
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

        MoveCommand moveCommand = new MoveCommand(this, newZone);
        moveCommand.execute();
        moveCommand.executeVisual(output);
    }

    public boolean canBeKilledByWeapon(Weapon weapon) {
        return weapon.getDamage() >= type.getMinDamageToDestroy();
    }

    public ZombieType getType() {
        return type;
    }
}
