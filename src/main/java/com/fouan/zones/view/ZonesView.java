package com.fouan.zones.view;

import com.fouan.actors.ActorId;
import com.fouan.events.*;
import com.fouan.zones.Position;
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
        zones.update(event.getZone().getPosition(), computedZone -> {
                    List<ActorId> actorIds = new ArrayList<>(computedZone.getActorIds());
                    actorIds.add(event.getSurvivor().getId());
                    return new ComputedZones.ComputedZone(computedZone.getZone(), actorIds);
                }
        );
    }

    @EventListener
    public void handleZombieSpawned(ZombieSpawned event) {
        zones.update(event.getZone().getPosition(), computedZone -> {
                    List<ActorId> actorIds = new ArrayList<>(computedZone.getActorIds());
                    actorIds.add(event.getZombie().getId());
                    return new ComputedZones.ComputedZone(computedZone.getZone(), actorIds);
                }
        );
    }

    @EventListener
    public void handleSurvivorMoved(SurvivorMoved event) {
        ComputedZones.ComputedZone oldZone = zones.findByActorId(event.getActorId())
                .orElseThrow();

        zones.update(oldZone.getPosition(), computedZone -> {
            List<ActorId> actorIds = computedZone.getActorIds()
                    .stream()
                    .filter(actorId -> !actorId.equals(event.getActorId()))
                    .toList();
            return new ComputedZones.ComputedZone(computedZone.getZone(), actorIds);
        });

        zones.update(event.getPosition(), computedZone -> {
            List<ActorId> actorIds = new ArrayList<>(computedZone.getActorIds());
            actorIds.add(event.getActorId());
            return new ComputedZones.ComputedZone(computedZone.getZone(), actorIds);
        });
    }

    @EventListener
    public void handleSurvivorDied(SurvivorDied event) {
        ComputedZones.ComputedZone oldZone = zones.findByActorId(event.getSurvivorId())
                .orElseThrow();

        zones.update(oldZone.getPosition(), computedZone -> {
            List<ActorId> actorIds = computedZone.getActorIds()
                    .stream()
                    .filter(actorId -> !actorId.equals(event.getSurvivorId()))
                    .toList();
            return new ComputedZones.ComputedZone(computedZone.getZone(), actorIds);
        });
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
    public Set<ActorId> findActorIdsOn(Position position) {
        return zones.all()
                .stream()
                .filter(computedZone -> computedZone.getZone().getPosition().equals(position))
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
