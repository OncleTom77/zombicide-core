package com.fouan;

import java.util.*;

public class Board {

    private final Output output;
    private final Random random;
    private List<Zone> zones;
    private Zone survivorStartingZone;
    private Zone exitZone;
    private Survivor survivor;
    private Zombie zombie;
    private int width;
    private int height;

    public Board(Output output, Random random) {
        this.output = output;
        this.random = random;
    }

    public void init() {
        width = 6;
        height = 6;

        zones = initZones(width, height);
        survivorStartingZone = getZone(0, 0).orElseThrow(IllegalArgumentException::new);
        exitZone = getZone(width - 1, height - 1).orElseThrow(IllegalArgumentException::new);
        survivor = new Survivor(survivorStartingZone);
        zombie = initZombie(random, zones);
    }

    private Zombie initZombie(Random random, List<Zone> zones) {
        int randomZoneIndex = random.nextInt(zones.size());
        Zone zombieStartingZone = zones.get(randomZoneIndex);
        return new Zombie(zombieStartingZone, random);
    }

    private Optional<Zone> getZone(int x, int y) {
        return getZone(zones, Position.builder()
                .x(x)
                .y(y)
                .build());
    }

    private Optional<Zone> getZone(List<Zone> zones, Position position) {
        return zones.stream()
                .filter(zone -> zone.isOn(position))
                .findAny();
    }

    private List<Zone> initZones(int width, int height) {
        List<Zone> zones = new ArrayList<>(width * height);
        for (int j = 0; j < height; j++) {
            for (int i = 0; i < width; i++) {
                zones.add(new Zone(new Position(i, j)));
            }
        }
        zones.forEach(zone -> Arrays.stream(Direction.values())
                .map(direction -> getZone(zones, direction.from(zone.getPosition())))
                .filter(Optional::isPresent)
                .map(Optional::get)
                .forEach(zone::addConnection));
        return zones;
    }

    public boolean isObjectiveComplete() {
        return survivor.getZone().equals(exitZone);
    }

    public Survivor getSurvivor() {
        return survivor;
    }

    public void playZombiePhase() {
        zombie.move();
    }

    public void displayBoard() {
        StringBuilder horizontalLine = new StringBuilder();
        for (int i = 0; i < width; i++) {
            horizontalLine.append("----");
        }
        horizontalLine.append("-");

        for (int i = 0, zonesSize = zones.size(); i < zonesSize; i++) {
            if (i % height == 0) {
                output.println(horizontalLine.toString());
            }
            Zone zone = zones.get(i);
            printZone(zone, i % height == height - 1);
        }
        output.println(horizontalLine.toString());
    }

    private void printZone(Zone zone, boolean displayEndOfLine) {
        if (zone.equals(survivor.getZone())) {
            output.print("| O ");
        } else if (zone.equals(zombie.getZone())) {
            output.print("| Z ");
        }  else if (zone.equals(survivorStartingZone)) {
            output.print("| S ");
        } else if (zone.equals(exitZone)) {
            output.print("| E ");
        } else {
            output.print("|   ");
        }
        if (displayEndOfLine) {
            output.println("|");
        }
    }
}
