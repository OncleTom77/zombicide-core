package com.fouan.events;

import com.fouan.actors.ActorId;
import com.fouan.zones.Zone;
import org.jetbrains.annotations.NotNull;

import java.util.List;

public class AvailableZonesForSurvivorMoveDefined extends Event<AvailableZonesForSurvivorMoveDefined> implements GameEvent {
    private final List<Zone> zones;

    public AvailableZonesForSurvivorMoveDefined(int turn, List<Zone> zones) {
        super(turn);
        this.zones = zones;
    }

    public List<Zone> getZones() {
        return zones;
    }
}
