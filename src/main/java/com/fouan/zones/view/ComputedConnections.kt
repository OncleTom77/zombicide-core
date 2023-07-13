package com.fouan.zones.view;

import com.fouan.zones.Zone;

import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

public class ComputedConnections {

    private final Set<Connection> connections;

    public ComputedConnections() {
        connections = new HashSet<>();
    }

    public void add(Connection connection) {
        connections.add(connection);
    }

    public Set<Connection> findByZone(Zone zone) {
        return connections.stream()
                .filter(connection -> connection.implies(zone))
                .collect(Collectors.toSet());
    }

    public Set<Connection> all() {
        return connections;
    }
}
