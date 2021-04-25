package com.fouan.character;

import com.fouan.board.Zone;
import com.fouan.io.Output;

public abstract class Character {
    protected final Output output;
    protected Zone zone;

    protected Character(Output output) {
        this.output = output;
    }

    public void changesZone(Zone zone) {
        this.zone.removeCharacter(this);
        this.zone = zone;
        zone.addCharacter(this);
    }

    public Zone getZone() {
        return zone;
    }
}
