package com.fouan.board;

import com.fouan.character.Survivor;
import com.fouan.character.Zombie;
import com.fouan.game.Direction;
import com.fouan.game.GameResult;
import com.fouan.io.Output;
import com.fouan.weapon.Axe;
import com.fouan.weapon.DiceRoller;

import java.util.*;

public class Board {

    private final Output output;
    private final Random random;
    private final DiceRoller diceRoller;
    private List<Zone> zones;
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
        width = 9;
        height = 6;

        zones = initZones(width, height);
        Zone survivorStartingZone = getZone(0, 0).orElseThrow(IllegalArgumentException::new);
        survivorStartingZone.addMarker(BoardMarker.STARTING_ZONE);
        getZone(width - 1, height - 1).orElseThrow(IllegalArgumentException::new)
                .addMarker(BoardMarker.EXIT_ZONE);
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
        connectZones(zones);
        return zones;
    }

    private void connectZones(List<Zone> zones) {
        zones.forEach(zone -> Arrays.stream(Direction.values())
                .forEach(direction -> getZone(zones, direction.apply(zone.getPosition()))
                        .ifPresent(neighborZone -> zone.addConnection(direction, neighborZone))));
    }

    public boolean isObjectiveComplete() {
        return survivor.getZone().containsMarker(BoardMarker.EXIT_ZONE);
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
        displayZones();
        displaySurvivorWounds();
    }

    private void displaySurvivorWounds() {
        survivor.displayWounds();
    }

    private void displayZones() {
        StringBuilder horizontalLine = new StringBuilder();
        for (int i = 0; i < width; i++) {
            horizontalLine.append("----");
        }
        horizontalLine.append("-");

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

    public boolean allSurvivorsDead() {
        return survivor.isDead();
    }

    public GameResult computeGameResult() {
        if (isObjectiveComplete()) {
            return GameResult.SURVIVORS_VICTORY;
        } else if (allSurvivorsDead()) {
            return GameResult.SURVIVORS_DEFEAT;
        }
        return GameResult.UNDEFINED;
    }
}
