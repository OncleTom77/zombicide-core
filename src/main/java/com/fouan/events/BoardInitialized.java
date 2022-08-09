package com.fouan.events;

import com.fouan.zones.Zone;
import lombok.Getter;

import java.util.List;

@Getter
public final class BoardInitialized
        extends Event<BoardInitialized>
        implements ZoneEvent {

    private final List<Zone> zones;

    public BoardInitialized(int turn, List<Zone> zones) {
        super(turn);
        this.zones = List.copyOf(zones);
    }
}
