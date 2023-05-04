package com.fouan.zones;

import lombok.AllArgsConstructor;
import lombok.Getter;

import java.util.Comparator;
import java.util.List;
import java.util.Objects;

import static lombok.AccessLevel.PRIVATE;

@AllArgsConstructor(access = PRIVATE)
public final class Zone {

    public static final Comparator<Zone> ZONE_POSITION_COMPARATOR = (o1, o2) -> Position.BOARD_COMPARATOR.compare(o1.getPosition(), o2.getPosition());

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

    public Position getPosition() {
        return position;
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

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Zone zone = (Zone) o;
        return Objects.equals(position, zone.position);
    }

    @Override
    public int hashCode() {
        return Objects.hash(position);
    }

    @Override
    public String toString() {
        return "Zone{" +
                "position=" + position +
                '}';
    }
}
