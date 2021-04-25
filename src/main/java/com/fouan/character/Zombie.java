package com.fouan.character;

import com.fouan.board.Zone;
import com.fouan.io.Output;

import java.util.List;
import java.util.Random;

public class Zombie extends Character {
    private final int damageInflicted = 1;

    private final Random random;

    public Zombie(Zone initialZone, Random random, Output output) {
        super(output);
        this.random = random;
        this.zone = initialZone;
        initialZone.addCharacter(this);
    }

    public void fights() {
        output.display("Zombie attacks the survivor!");
        zone.getSurvivor()
                .suffersInjury(damageInflicted);
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
}
