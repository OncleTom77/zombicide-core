package com.fouan.board;

import com.fouan.character.Zombie;

import java.util.ArrayList;
import java.util.List;

public class Zone {

    private final Position position;
    private final List<Zone> connectedZones;
    private final List<Zombie> zombies;

    public Zone(Position position) {
        this.position = position;
        connectedZones = new ArrayList<>();
        zombies = new ArrayList<>();
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

    boolean isOn(Position position) {
        return this.position.equals(position);
    }

    public boolean containsZombie() {
        return !zombies.isEmpty();
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
