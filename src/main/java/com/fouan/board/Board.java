package com.fouan.board;

import com.fouan.actor.Survivor;
import com.fouan.actor.Zombie;
import com.fouan.game.Direction;
import com.fouan.game.GameResult;
import com.fouan.io.Output;
import com.fouan.weapon.Axe;
import com.fouan.weapon.DiceRoller;

import java.util.*;
import java.util.stream.Collectors;

public class Board {

    private final Output output;
    private final DiceRoller diceRoller;
    private final ZombieSpawner zombieSpawner;
    private List<Zone> zones;
    private int width;
    private int height;

    public Board(Output output, DiceRoller diceRoller, ZombieSpawner zombieSpawner) {
        this.output = output;
        this.diceRoller = diceRoller;
        this.zombieSpawner = zombieSpawner;
    }

    public void init() {
        width = 9;
        height = 6;

        zones = initZones(width, height);
        getZone(0, 0).orElseThrow().addMarker(BoardMarker.STARTING_ZONE);
        getZone(width - 1, height - 1).orElseThrow().addMarker(BoardMarker.EXIT_ZONE);
        getZone(width - 1, 0).orElseThrow().addMarker(BoardMarker.ZOMBIE_SPAWN);

        initSurvivor();
        spawnZombies();
    }

    private void initSurvivor() {
        Zone startingZone = zones.stream()
                .filter(zone -> zone.containsMarker(BoardMarker.STARTING_ZONE))
                .findFirst()
                .orElseThrow();
        new Survivor(startingZone, new Axe(diceRoller, output), output);
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

    private List<Zone> findZones(BoardMarker marker) {
        return zones.stream()
                .filter(zone -> zone.containsMarker(marker))
                .collect(Collectors.toList());
    }

    public Survivor getSurvivor() {
        return zones.stream()
                .map(Zone::getSurvivor)
                .filter(Objects::nonNull)
                .findFirst()
                .orElseThrow();
    }

    private List<Zombie> getZombies() {
        return zones.stream()
                .map(Zone::getZombies)
                .flatMap(Collection::stream)
                .collect(Collectors.toList());
    }

    /**
     * First, play zombies that can attack
     * Then, play zombies that must move
     */
    public void playZombiesPhase() {
        List<Zombie> zombies = getZombies();
        zombies.stream()
                .filter(Zombie::canFight)
                .forEach(Zombie::fights);
        zombies.stream()
                .filter(zombie -> !zombie.canFight())
                .forEach(Zombie::moves);
    }

    public void displayBoard() {
        displayZones();
        displaySurvivorWounds();
    }

    private void displaySurvivorWounds() {
        getSurvivor().displayWounds();
    }

    private void displayZones() {
        StringBuilder horizontalLine = new StringBuilder();
        for (int i = 0; i < width; i++) {
            horizontalLine.append("-----");
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

    public boolean isObjectiveComplete() {
        return findZones(BoardMarker.EXIT_ZONE)
                .stream()
                .findFirst()
                .orElseThrow()
                .containsSurvivor();
    }

    public boolean allSurvivorsDead() {
        return getSurvivor().isDead();
    }

    public GameResult computeGameResult() {
        if (isObjectiveComplete()) {
            return GameResult.SURVIVORS_VICTORY;
        } else if (allSurvivorsDead()) {
            return GameResult.SURVIVORS_DEFEAT;
        }
        return GameResult.UNDEFINED;
    }

    public void spawnZombies() {
        DangerLevel dangerLevel = getSurvivor().getDangerLevel();
        findZones(BoardMarker.ZOMBIE_SPAWN)
                .forEach(zone -> zombieSpawner.spawnZombies(dangerLevel, zone));
    }
}
