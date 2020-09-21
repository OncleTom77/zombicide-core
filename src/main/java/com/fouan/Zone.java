package com.fouan;

import java.util.ArrayList;
import java.util.List;

public class Zone {

    private final Position position;
    private List<Zone> connectedZones;

    public Zone(Position position) {
        this.position = position;
        connectedZones = new ArrayList<>();
    }

    public void addConnection(Zone zone) {
        connectedZones.add(zone);
    }

    public Position getPosition() {
        return position;
    }

    public List<Zone> getConnectedZones() {
        return connectedZones;
    }

    @Override
    public String toString() {
        return "Zone{" + position + '}';
    }
}
