package com.fouan.board;

import com.fouan.character.Survivor;
import com.fouan.character.Zombie;
import com.fouan.game.Direction;
import com.fouan.io.Output;
import com.fouan.weapon.Axe;
import com.fouan.weapon.DiceRoller;

import java.util.*;

public class Board {

    private final Output output;
    private final Random random;
    private final DiceRoller diceRoller;
    private List<Zone> zones;
    private Zone survivorStartingZone;
    private Zone exitZone;
    private Survivor survivor;
    private Zombie zombie;
    private int width;
    private int height;

    public Board(Output output, Random random, DiceRoller diceRoller) {
        this.output = output;
        this.random = random;
        this.diceRoller = diceRoller;
    }

    public void init() {
        width = 6;
        height = 6;

        zones = initZones(width, height);
        survivorStartingZone = getZone(0, 0).orElseThrow(IllegalArgumentException::new);
        exitZone = getZone(width - 1, height - 1).orElseThrow(IllegalArgumentException::new);
        survivor = new Survivor(survivorStartingZone, new Axe(diceRoller, output), output);
        zombie = initZombie(random, zones, output);
    }

    private Zombie initZombie(Random random, List<Zone> zones, Output output) {
        int randomZoneIndex = random.nextInt(zones.size());
        Zone zombieStartingZone = zones.get(randomZoneIndex);
        return new Zombie(zombieStartingZone, random, output);
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
        if (zombie != null) {
            zombie.plays();
        }
    }

    public void removeZombie() {
        zones.forEach(zone -> zone.removeZombie(zombie));
        zombie = null;
    }

    public void displayBoard() {
        StringBuilder horizontalLine = new StringBuilder();
        for (int i = 0; i < width; i++) {
            horizontalLine.append("----");
        }
        horizontalLine.append("-");

        StringBuilder stringBuilder = new StringBuilder();
        output.display(horizontalLine.toString());
        for (int i = 0, zonesSize = zones.size(); i < zonesSize; i++) {
            Zone zone = zones.get(i);
            stringBuilder.append(toString(zone));

            boolean endOfLine = i % height == height - 1;
            if (endOfLine) {
                stringBuilder.append("|");
                output.display(stringBuilder.toString());
                stringBuilder.delete(0, stringBuilder.length());
                output.display(horizontalLine.toString());
            }
        }
    }

    private String toString(Zone zone) {
        StringBuilder stringBuilder = new StringBuilder();
        if (zone.equals(survivor.getZone())) {
            stringBuilder.append("| O ");
        } else if (zombie != null && zone.equals(zombie.getZone())) {
            stringBuilder.append("| Z ");
        } else if (zone.equals(survivorStartingZone)) {
            stringBuilder.append("| S ");
        } else if (zone.equals(exitZone)) {
            stringBuilder.append("| E ");
        } else {
            stringBuilder.append("|   ");
        }
        return stringBuilder.toString();
    }

    public boolean hasSurvivorAlive() {
        return survivor.isAlive();
    }
}
