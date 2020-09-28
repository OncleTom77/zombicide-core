package com.fouan.board;

import com.fouan.character.Survivor;
import com.fouan.character.Zombie;

import java.util.ArrayList;
import java.util.List;

public class Zone {

    private final Position position;
    private final List<Zone> connectedZones;
    private final List<Zombie> zombies;
    private final List<Survivor> survivors;

    public Zone(Position position) {
        this.position = position;
        connectedZones = new ArrayList<>();
        zombies = new ArrayList<>();
        survivors = new ArrayList<>();
    }

    public boolean isOn(Position position) {
        return this.position.equals(position);
    }

    public void addConnection(Zone zone) {
        connectedZones.add(zone);
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

    public Survivor getSurvivor() {
        return survivors.stream()
                .findFirst()
                .orElse(null);
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
