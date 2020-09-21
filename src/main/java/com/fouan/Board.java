package com.fouan;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

public class Board {

    private final Output output;
    private List<Zone> zones;
    private Zone survivorStartingZone;
    private Zone exitZone;
    private Survivor survivor;
    private int width;
    private int height;

    public Board(Output output) {
        this.output = output;
    }

    public void init() {
        width = 6;
        height = 6;

        initZones();
        survivorStartingZone = getZone(0, 0).orElseThrow(IllegalArgumentException::new);
        exitZone = getZone(width - 1, height - 1).orElseThrow(IllegalArgumentException::new);
        survivor = new Survivor(survivorStartingZone);
    }

    private Optional<Zone> getZone(int x, int y) {
        return getZone(Position.builder()
                .x(x)
                .y(y)
                .build());
    }

    private Optional<Zone> getZone(Position position) {
        return zones.stream()
                .filter(zone -> zone.getPosition().equals(position))
                .findAny();
    }

    private void initZones() {
        zones = new ArrayList<>(width * height);
        for (int j = 0; j < height; j++) {
            for (int i = 0; i < width; i++) {
                zones.add(new Zone(new Position(i, j)));
            }
        }
        zones.forEach(zone -> Arrays.stream(Direction.values())
                .map(direction -> getZone(direction.from(zone.getPosition())))
                .filter(Optional::isPresent)
                .map(Optional::get)
                .forEach(zone::addConnection));
    }

    public boolean isObjectiveComplete() {
        return survivor.getZone().equals(exitZone);
    }

    public Survivor getSurvivor() {
        return survivor;
    }

    public void displayBoard() {
        StringBuilder stringBuilder = new StringBuilder();
        for (int i = 0; i < width; i++) {
            stringBuilder.append("----");
        }
        stringBuilder.append("-");

        for (int i = 0, zonesSize = zones.size(); i < zonesSize; i++) {
            if (i % height == 0) {
                output.println(stringBuilder.toString());
            }
            Zone zone = zones.get(i);
            if (zone.equals(survivor.getZone())) {
                output.print("| O ");
            } else if (zone.equals(survivorStartingZone)) {
                output.print("| S ");
            } else if (zone.equals(exitZone)) {
                output.print("| E ");
            } else {
                output.print("|   ");
            }
            if (i % height == height - 1) {
                output.println("|");
            }
        }
        output.println(stringBuilder.toString());
    }
}
