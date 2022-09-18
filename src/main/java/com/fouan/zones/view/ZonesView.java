package com.fouan.zones.view;

import com.fouan.actors.ActorId;
import com.fouan.events.BoardInitialized;
import com.fouan.events.SurvivorAdded;
import com.fouan.events.ZoneEvent;
import com.fouan.zones.Zone;
import com.fouan.zones.Zone.ZoneMarker;
import org.springframework.context.event.EventListener;

import javax.inject.Named;
import java.util.*;

@Named
final class ZonesView implements ZonesCommands, ZonesQueries {

    private final Deque<ZoneEvent> history = new LinkedList<>();

    @EventListener
    public void handleZoneEvent(ZoneEvent event) {
        history.push(event);
    }

    @EventListener
    public void handleBoardInitialized(BoardInitialized event) {

    }

    @EventListener
    public void handleSurvivorAdded(SurvivorAdded event) {

    }

    @Override
    public void clear() {
        history.clear();
    }

    @Override
    public Optional<Zone> findByActorId(ActorId actorId) {
        return Optional.empty();
    }

    @Override
    public List<Zone> findByMarker(ZoneMarker marker) {
        return null;
    }

    @Override
    public Set<ActorId> findActorIdsOn(Zone zone) {
        return null;
    }

    @Override
    public List<Zone> findConnectedZones(Zone zone) {
        return null;
    }
}
