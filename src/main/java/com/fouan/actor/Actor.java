package com.fouan.actor;

import com.fouan.board.Zone;
import com.fouan.game.ActorSelection;
import com.fouan.io.Output;

import java.util.Collections;

public abstract class Actor {
    protected final Output output;
    protected final ActorSelection actorSelection;
    protected Zone zone;

    protected Actor(Output output, ActorSelection actorSelection, Zone initialZone) {
        this.output = output;
        this.actorSelection = actorSelection;
        this.zone = initialZone;
        initialZone.addActor(this);
    }

    public void changesZone(Zone zone) {
        this.zone.removeActors(Collections.singletonList(this));
        this.zone = zone;
        zone.addActor(this);
    }

    public Zone getZone() {
        return zone;
    }
}
