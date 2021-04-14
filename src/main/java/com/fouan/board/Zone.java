package com.fouan.board;

import com.fouan.character.Survivor;
import com.fouan.character.Zombie;
import com.fouan.game.Direction;

import java.util.*;

public class Zone {

    private final Position position;
    private final Map<Direction, Zone> connectedZones;
    private final List<Zombie> zombies;
    private final List<Survivor> survivors;
    private final List<BoardMarker> markers;

    public Zone(Position position) {
        this.position = position;
        connectedZones = new HashMap<>(4);
        zombies = new ArrayList<>();
        survivors = new ArrayList<>();
        markers = new ArrayList<>();
    }

    public boolean isOn(Position position) {
        return this.position.equals(position);
    }

    public void addConnection(Direction direction, Zone zone) {
        connectedZones.put(direction, zone);
    }

    public void addZombie(Zombie zombie) {
        zombies.add(zombie);
    }

    public void removeZombie(Zombie zombie) {
        zombies.remove(zombie);
    }

    public boolean containsZombie() {
        return !zombies.isEmpty();
    }

    public void removeSurvivor(Survivor survivor) {
        survivors.remove(survivor);
    }

    public void addSurvivor(Survivor survivor) {
        survivors.add(survivor);
    }

    public boolean containsSurvivor() {
        return !survivors.isEmpty();
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

    public Survivor getSurvivor() {
        return survivors.stream()
                .findFirst()
                .orElse(null);
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
            return "O";
        } else if (containsZombie()) {
            return "Z";
        } else if (containsMarker(BoardMarker.STARTING_ZONE)) {
            return "S";
        } else if (containsMarker(BoardMarker.EXIT_ZONE)) {
            return "E";
        } else {
            return " ";
        }
    }
}
