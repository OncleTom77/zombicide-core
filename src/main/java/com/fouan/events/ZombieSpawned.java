package com.fouan.events;

import com.fouan.actors.zombies.Zombie;
import com.fouan.zones.Zone;
import lombok.Getter;

@Getter
public final class ZombieSpawned
        extends Event<ZombieSpawned>
        implements ActorEvent, ZoneEvent {

    private final Zombie zombie;
    private final Zone zone;

    public ZombieSpawned(int turn, Zombie zombie, Zone zone) {
        super(turn);
        this.zombie = zombie;
        this.zone = zone;
    }
}
