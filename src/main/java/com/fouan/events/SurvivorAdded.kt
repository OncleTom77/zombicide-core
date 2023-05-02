package com.fouan.events;

import com.fouan.actors.Survivor;
import com.fouan.zones.Zone;
import lombok.Getter;

@Getter
public final class SurvivorAdded
        extends Event<SurvivorAdded>
        implements ActorEvent {

    private final Survivor survivor;
    private final Zone zone;

    public SurvivorAdded(int turn, Survivor survivor, Zone zone) {
        super(turn);
        this.survivor = survivor;
        this.zone = zone;
    }
}
