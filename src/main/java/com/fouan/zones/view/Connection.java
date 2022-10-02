package com.fouan.zones.view;

import com.fouan.zones.Position;
import com.fouan.zones.Zone;

import java.util.Objects;

public record Connection(Zone start, Zone end) {

    public Connection(Zone start, Zone end) {
        if (start == null || end == null) {
            throw new IllegalArgumentException("Start and end must not be null");
        }
        if (start.equals(end)) {
            throw new IllegalArgumentException("Start and end must be different");
        }
        if (Zone.ZONE_POSITION_COMPARATOR.compare(start, end) < 0) {
            this.start = start;
            this.end = end;
        } else {
            this.start = end;
            this.end = start;
        }
    }

    public boolean implies(Zone zone) {
        Position position = zone.getPosition();
        return start.getPosition().equals(position)
                || end.getPosition().equals(position);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Connection that = (Connection) o;
        return Objects.equals(start, that.start) && Objects.equals(end, that.end);
    }

    @Override
    public int hashCode() {
        return Objects.hash(start, end);
    }

    @Override
    public String toString() {
        return "Connection{" +
                "start=" + start +
                ", end=" + end +
                '}';
    }
}
