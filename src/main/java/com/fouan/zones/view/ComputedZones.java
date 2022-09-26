package com.fouan.zones.view;

import com.fouan.actors.ActorId;
import com.fouan.zones.Position;
import com.fouan.zones.Zone;
import lombok.Getter;

import java.util.*;

public final class ComputedZones {

    private final Map<Position, ComputedZone> zones;

    public ComputedZones() {
        zones = new HashMap<>();
    }

    public Collection<ComputedZone> all() {
        return zones.values();
    }

    public void add(ComputedZone zone) {
        if (zones.containsKey(zone.getPosition())) {
            throw new IllegalStateException("Zone already exists");
        }

        zones.put(zone.getPosition(), zone);
    }

    public void update(Position position, ComputedZone zone) {
        if (!zones.containsKey(zone.getPosition())) {
            throw new IllegalStateException("Zone does not exist");
        }

        zones.put(position, zone);
    }

    public Optional<ComputedZone> findByPosition(Position position) {
        return Optional.ofNullable(zones.get(position));
    }

    @Getter
    public static class ComputedZone {
        private final Zone zone;

        private final List<ActorId> actorIds;

        public ComputedZone(Zone zone) {
            this(zone, Collections.emptyList());
        }

        public ComputedZone(Zone zone, List<ActorId> actorIds) {
            this.zone = zone;
            this.actorIds = actorIds;
        }

        public Position getPosition() {
            return zone.getPosition();
        }
    }
}
