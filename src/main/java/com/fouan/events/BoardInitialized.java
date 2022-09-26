package com.fouan.events;

import com.fouan.zones.Zone;
import com.fouan.zones.view.Connection;
import lombok.Getter;

import java.util.List;
import java.util.Set;

@Getter
public final class BoardInitialized
        extends Event<BoardInitialized>
        implements ZoneEvent {

    private final List<Zone> zones;
    private final Set<Connection> connections;

    public BoardInitialized(int turn, List<Zone> zones, Set<Connection> connections) {
        super(turn);
        this.zones = List.copyOf(zones);
        this.connections = Set.copyOf(connections);
    }
}
