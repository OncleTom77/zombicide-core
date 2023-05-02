package com.fouan.events;

import com.fouan.actors.DangerLevel;
import com.fouan.old.cards.SpawnInfo;
import com.fouan.zones.Zone;
import lombok.Getter;

@Getter
public final class ZombieSpawnZoneAdded
        extends Event<ZombieSpawnZoneAdded>
        implements ZoneEvent {

    private final Zone zone;
    private final DangerLevel dangerLevel;
    private final SpawnInfo spawnInfo;

    public ZombieSpawnZoneAdded(int turn, Zone zone, DangerLevel dangerLevel, SpawnInfo spawnInfo) {
        super(turn);
        this.zone = zone;
        this.dangerLevel = dangerLevel;
        this.spawnInfo = spawnInfo;
    }
}
