package com.fouan.character;

import com.fouan.board.Zone;

public class Survivor {
    private Zone zone;

    public Survivor(Zone initialZone) {
        this.zone = initialZone;
    }

    public void changeZone(Zone zone) {
        this.zone = zone;
    }

    public Zone getZone() {
        return zone;
    }
}
