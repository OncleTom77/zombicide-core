package com.fouan.events;

import com.fouan.actors.ActorId;
import com.fouan.zones.Zone;
import lombok.Getter;

@Getter
public final class ZombieMoved
        extends Event<ZombieMoved>
        implements ZoneEvent {

    private final ActorId zombieId;
    private final Zone zone;

    public ZombieMoved(int turn, ActorId zombieId, Zone zone) {
        super(turn);
        this.zombieId = zombieId;
        this.zone = zone;
    }
}
