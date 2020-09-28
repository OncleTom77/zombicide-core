package com.fouan.character;

import com.fouan.board.Zone;
import com.fouan.io.Output;

import java.util.List;
import java.util.Random;

public class Zombie {
    private Zone zone;
    private final Random random;
    private final Output output;

    public Zombie(Zone initialZone, Random random, Output output) {
        this.zone = initialZone;
        this.random = random;
        this.output = output;
        initialZone.addZombie(this);
    }

    private void changesZone(Zone zone) {
        this.zone.removeZombie(this);
        this.zone = zone;
        zone.addZombie(this);
    }

    public void plays() {
        if (canFight()) {
            fights();
        } else {
            moves();
        }
    }

    private void fights() {
        output.display("Zombie attacks the survivor!");
        zone.getSurvivor()
                .suffersInjury();
    }

    private boolean canFight() {
        return zone.containsSurvivor();
    }

    public void moves() {
        List<Zone> possibleZones = zone.getConnectedZones();
        int randomZoneIndex = random.nextInt(possibleZones.size());
        Zone newZone = possibleZones.get(randomZoneIndex);

        changesZone(newZone);
    }

    public Zone getZone() {
        return zone;
    }
}
