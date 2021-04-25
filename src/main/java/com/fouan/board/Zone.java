package com.fouan.board;

import com.fouan.character.Character;
import com.fouan.character.Survivor;
import com.fouan.character.Zombie;
import com.fouan.game.Direction;

import java.util.*;
import java.util.stream.Collectors;

public class Zone {

    private final Position position;
    private final Map<Direction, Zone> connectedZones;
    private final List<Character> characters;
    private final List<BoardMarker> markers;

    public Zone(Position position) {
        this.position = position;
        connectedZones = new HashMap<>(4);
        characters = new ArrayList<>();
        markers = new ArrayList<>();
    }

    public boolean isOn(Position position) {
        return this.position.equals(position);
    }

    public void addConnection(Direction direction, Zone zone) {
        connectedZones.put(direction, zone);
    }

    public void addCharacter(Character character) {
        characters.add(character);
    }

    public void removeCharacter(Character character) {
        characters.remove(character);
    }

    public boolean containsZombie() {
        return characters.stream()
                .anyMatch(character -> character instanceof Zombie);
    }

    public List<Zombie> getZombies() {
        return characters.stream()
                .filter(character -> character instanceof Zombie)
                .map(character -> (Zombie) character)
                .collect(Collectors.toList());
    }

    public boolean containsSurvivor() {
        return characters.stream()
                .anyMatch(character -> character instanceof Survivor);
    }

    public Survivor getSurvivor() {
        return characters.stream()
                .filter(character -> character instanceof Survivor)
                .map(character -> (Survivor) character)
                .findFirst()
                .orElse(null);
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
