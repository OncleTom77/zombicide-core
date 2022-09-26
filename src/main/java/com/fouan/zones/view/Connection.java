package com.fouan.zones.view;

import com.fouan.zones.Position;
import com.fouan.zones.Zone;
import lombok.EqualsAndHashCode;

@EqualsAndHashCode(callSuper = true)
public record Connection(Zone start, Zone end) {
    public Connection {
        if (start == null || end == null) {
            throw new IllegalArgumentException("Start and end must not be null");
        }
        if (start.equals(end)) {
            throw new IllegalArgumentException("Start and end must be different");
        }

    }

    public boolean implies(Zone zone) {
        Position position = zone.getPosition();
        return start.getPosition().equals(position)
                || end.getPosition().equals(position);
    }
}
