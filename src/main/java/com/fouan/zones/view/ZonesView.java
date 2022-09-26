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
import java.util.stream.Collectors;
import java.util.stream.Stream;

@Named
public final class ZonesView implements ZonesCommands, ZonesQueries {

    private final Deque<ZoneEvent> history = new LinkedList<>();
    private final ComputedZones zones = new ComputedZones();
    private final ComputedConnections connections = new ComputedConnections();

    @EventListener
    public void handleZoneEvent(ZoneEvent event) {
        history.push(event);
    }

    @EventListener
    public void handleBoardInitialized(BoardInitialized event) {
        for (Zone zone : event.getZones()) {
            zones.add(new ComputedZones.ComputedZone(zone));
        }
        event.getConnections()
                .forEach(connections::add);
    }

    @EventListener
    public void handleSurvivorAdded(SurvivorAdded event) {
        ComputedZones.ComputedZone computedZone = zones.findByPosition(event.getZone().getPosition())
                .orElseThrow();

        List<ActorId> actorIds = new ArrayList<>(computedZone.getActorIds());
        actorIds.add(event.getSurvivor().getId());

        zones.update(
                computedZone.getPosition(),
                new ComputedZones.ComputedZone(computedZone.getZone(), actorIds)
        );
    }

    @Override
    public void clear() {
        history.clear();
    }

    @Override
    public List<Zone> findAll() {
        return zones.all()
                .stream()
                .map(ComputedZones.ComputedZone::getZone)
                .toList();
    }

    @Override
    public Optional<Zone> findByActorId(ActorId actorId) {
        return zones.all()
                .stream()
                .filter(computedZone -> computedZone.getActorIds().contains(actorId))
                .map(ComputedZones.ComputedZone::getZone)
                .findFirst();
    }

    @Override
    public List<Zone> findByMarker(ZoneMarker marker) {
        return zones.all()
                .stream()
                .map(ComputedZones.ComputedZone::getZone)
                .filter(zone -> zone.getMarkers().contains(marker))
                .toList();
    }

    @Override
    public Set<ActorId> findActorIdsOn(Zone zone) {
        return zones.all()
                .stream()
                .filter(computedZone -> computedZone.getZone().getPosition().equals(zone.getPosition()))
                .map(ComputedZones.ComputedZone::getActorIds)
                .flatMap(Collection::stream)
                .collect(Collectors.toSet());
    }

    @Override
    public List<Zone> findConnectedZones(Zone zone) {
        return connections.findByZone(zone)
                .stream()
                .flatMap(connection -> Stream.of(connection.start(), connection.end()))
                .filter(connectedZone -> !connectedZone.equals(zone))
                .distinct()
                .toList();
    }
}
