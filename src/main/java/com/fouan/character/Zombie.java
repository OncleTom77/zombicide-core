package com.fouan.character;

import com.fouan.board.Zone;

import java.util.List;
import java.util.Random;

public class Zombie {
    private Zone zone;
    private final Random random;

    public Zombie(Zone initialZone, Random random) {
        this.zone = initialZone;
        this.random = random;
    }

    private void changeZone(Zone zone) {
        this.zone = zone;
    }

    public Zone getZone() {
        return zone;
    }

    public void move() {
        List<Zone> possibleZones = zone.getConnectedZones();
        int randomZoneIndex = random.nextInt(possibleZones.size());
        Zone newZone = possibleZones.get(randomZoneIndex);

        changeZone(newZone);
    }
}
