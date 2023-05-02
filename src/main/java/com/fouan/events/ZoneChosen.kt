package com.fouan.events;

import com.fouan.zones.Position;
import com.fouan.zones.Zone;

import java.time.ZoneId;
import java.util.List;

public class ZoneChosen extends Event<ZoneChosen> implements GameEvent {

    private final Position position;

    public ZoneChosen(int turn, Position position) {
        super(turn);
        this.position = position;
    }

    public Position getPosition() {
        return position;
    }
}
