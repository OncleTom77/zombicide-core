package com.fouan.zones;

import lombok.AllArgsConstructor;
import lombok.Getter;

import java.util.List;

import static lombok.AccessLevel.PRIVATE;

@AllArgsConstructor(access = PRIVATE)
public final class Zone {

    @Getter
    private final Position position;
    private final List<ZoneMarker> markers;

    public static Zone startingZone(Position position) {
        return new Zone(position, List.of(ZoneMarker.STARTING_ZONE));
    }

    public static Zone exitZone(Position position) {
        return new Zone(position, List.of(ZoneMarker.EXIT_ZONE));
    }

    public static Zone zombieSpawnZone(Position position) {
        return new Zone(position, List.of(ZoneMarker.ZOMBIE_SPAWN));
    }

    public static Zone normalZone(Position position) {
        return new Zone(position, List.of(ZoneMarker.NORMAL_ZONE));
    }

    public List<ZoneMarker> getMarkers() {
        return List.copyOf(markers);
    }

    public enum ZoneMarker {
        STARTING_ZONE,
        EXIT_ZONE,
        ZOMBIE_SPAWN,
        NORMAL_ZONE,
    }
}
