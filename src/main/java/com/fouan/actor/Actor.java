package com.fouan.actor;

import com.fouan.board.Zone;
import com.fouan.io.Output;

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
