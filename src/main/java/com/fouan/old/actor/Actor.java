package com.fouan.old.actor;

import com.fouan.old.board.Zone;
import com.fouan.display.Output;

public abstract class Actor {
    protected final Output output;
    protected Zone zone;

    protected Actor(Output output, Zone initialZone) {
        this.output = output;
        this.zone = initialZone;
        initialZone.addActor(this);
    }

    public Zone getZone() {
        return zone;
    }

    public void setZone(Zone zone) {
        this.zone = zone;
    }
}
