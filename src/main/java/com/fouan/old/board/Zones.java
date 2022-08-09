package com.fouan.old.board;

import com.fouan.old.game.Direction;
import com.fouan.display.Output;
import com.fouan.zones.Position;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

public class Zones {
    private final List<Zone> zones;
    private final int width;
    private final int height;

    private Zones(List<Zone> zones, int width, int height) {
        if (width < 0 || height < 0) {
            throw new IllegalArgumentException("width and height must be greater than 0");
        }

        this.zones = zones;
        this.width = width;
        this.height = height;
        connectZones();
    }

    public static Zones emptyZones(int width, int height) {
        List<Zone> emptyZones = initZones(width, height);
        return new Zones(emptyZones, width, height);
    }

    private static List<Zone> initZones(int width, int height) {
        List<Zone> zones = new ArrayList<>(width * height);
        for (int j = 0; j < height; j++) {
            for (int i = 0; i < width; i++) {
                zones.add(new Zone(new Position(i, j)));
            }
        }
        return zones;
    }

    private void connectZones() {
        for (Zone zone : zones) {
            for (Direction direction : Direction.values()) {
                Position nextPosition = direction.apply(zone.getPosition());
                getZone(nextPosition).ifPresent(neighborZone -> zone.addConnection(direction, neighborZone));
            }
        }
    }

    public Optional<Zone> getZone(int x, int y) {
        return getZone(new Position(x, y));
    }

    public Optional<Zone> getZone(Position position) {
        return zones.stream()
                .filter(zone -> zone.isOn(position))
                .findAny();
    }

    public List<Zone> findZones(BoardMarker marker) {
        return zones.stream()
                .filter(zone -> zone.containsMarker(marker))
                .collect(Collectors.toList());
    }

    public Optional<Zone> findZone(BoardMarker marker) {
        return findZones(marker).stream()
                .findFirst();
    }

    public void displayZones(Output output) {
        StringBuilder horizontalLine = new StringBuilder("-");
        horizontalLine.append("-----".repeat(Math.max(0, width)));

        StringBuilder line = new StringBuilder();
        output.display(horizontalLine.toString());
        for (int i = 0, zonesSize = zones.size(); i < zonesSize; i++) {
            Zone zone = zones.get(i);
            line.append("| ")
                    .append(zone.getStringRepresentation())
                    .append(" ");

            boolean endOfLine = i % width == width - 1;
            if (endOfLine) {
                line.append("|");
                output.display(line.toString());
                line.delete(0, line.length());
                output.display(horizontalLine.toString());
            }
        }
    }

    public List<Zone> getZones() {
        return zones.stream()
                .toList();
    }
}
