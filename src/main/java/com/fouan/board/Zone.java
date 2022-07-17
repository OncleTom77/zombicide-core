package com.fouan.board;

import com.fouan.actor.Actor;
import com.fouan.actor.Survivor;
import com.fouan.actor.Zombie;
import com.fouan.game.Direction;

import java.util.*;
import java.util.stream.Collectors;

public class Zone {

    private final Position position;
    private final Map<Direction, Zone> connectedZones;
    private final Set<Actor> actors;
    private final List<BoardMarker> markers;

    public Zone(Position position) {
        this.position = position;
        connectedZones = new HashMap<>(4);
        actors = new HashSet<>();
        markers = new ArrayList<>();
    }

    public boolean isOn(Position position) {
        return this.position.equals(position);
    }

    public void addConnection(Direction direction, Zone zone) {
        connectedZones.put(direction, zone);
    }

    public void addActor(Actor actor) {
        actors.add(actor);
    }

    public void removeActor(Actor actor) {
        actors.remove(actor);
    }

    public void addActors(List<? extends Actor> actorsToAdd) {
        actors.addAll(actorsToAdd);
    }

    public void removeActors(List<? extends Actor> actorsToRemove) {
        actors.removeAll(actorsToRemove);
    }

    public boolean containsZombie() {
        return actors.stream()
                .anyMatch(actor -> actor instanceof Zombie);
    }

    public List<Zombie> getZombies() {
        return actors.stream()
                .filter(actor -> actor instanceof Zombie)
                .map(actor -> (Zombie) actor)
                .collect(Collectors.toList());
    }

    public boolean containsSurvivor() {
        return actors.stream()
                .anyMatch(actor -> actor instanceof Survivor);
    }

    public List<Survivor> getSurvivors() {
        return actors.stream()
                .filter(actor -> actor instanceof Survivor)
                .map(actor -> (Survivor) actor)
                .collect(Collectors.toList());
    }

    public void addMarker(BoardMarker marker) {
        markers.add(marker);
    }

    public void removeMarker(BoardMarker marker) {
        markers.remove(marker);
    }

    public boolean containsMarker(BoardMarker marker) {
        return markers.contains(marker);
    }

    public Position getPosition() {
        return position;
    }

    public List<Zone> getConnectedZones() {
        return new ArrayList<>(connectedZones.values());
    }

    public Optional<Zone> getConnectedZone(Direction direction) {
        return Optional.ofNullable(connectedZones.get(direction));
    }

    public List<Zone> getAllInDirection(Direction direction) {
        return getAllInDirection(new ArrayList<>(), direction);
    }

    private List<Zone> getAllInDirection(List<Zone> acc, Direction direction) {
        getConnectedZone(direction).ifPresent(neighborZone -> {
            acc.add(neighborZone);
            neighborZone.getAllInDirection(acc, direction);
        });
        return acc;
    }

    @Override
    public String toString() {
        return "Zone{" + position + '}';
    }

    String getStringRepresentation() {
        if (containsSurvivor()) {
            return "Su";
        } else if (containsZombie()) {
            return "Zo";
        } else if (containsMarker(BoardMarker.STARTING_ZONE)) {
            return "St";
        } else if (containsMarker(BoardMarker.EXIT_ZONE)) {
            return "Ex";
        } else if (containsMarker(BoardMarker.ZOMBIE_SPAWN)) {
            return "Sp";
        } else {
            return "  ";
        }
    }
}
