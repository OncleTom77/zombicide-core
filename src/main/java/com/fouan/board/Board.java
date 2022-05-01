package com.fouan.board;

import com.fouan.actor.ActorFactory;
import com.fouan.actor.Survivor;
import com.fouan.actor.Zombie;
import com.fouan.actor.ZombiePhase;
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
    private final ActorFactory actorFactory;
    private final ZombiePhase zombiePhase;
    private List<Zone> zones;
    private int width;
    private int height;
    private List<Survivor> survivors;

    public Board(Output output, DiceRoller diceRoller, ZombieSpawner zombieSpawner, ActorFactory actorFactory, ZombiePhase zombiePhase) {
        this.output = output;
        this.diceRoller = diceRoller;
        this.zombieSpawner = zombieSpawner;
        this.actorFactory = actorFactory;
        this.zombiePhase = zombiePhase;
    }

    public void init() {
        width = 9;
        height = 6;

        zones = initZones(width, height);
        getZone(0, 0).orElseThrow().addMarker(BoardMarker.STARTING_ZONE);
        getZone(width - 1, height - 1).orElseThrow().addMarker(BoardMarker.EXIT_ZONE);
        getZone(1, 0).orElseThrow().addMarker(BoardMarker.ZOMBIE_SPAWN);

        survivors = initSurvivors();
        spawnZombies();
        spawnZombies();
    }

    private List<Survivor> initSurvivors() {
        Zone startingZone = zones.stream()
                .filter(zone -> zone.containsMarker(BoardMarker.STARTING_ZONE))
                .findFirst()
                .orElseThrow();

        return List.of(
                actorFactory.generateAsim(startingZone, new Axe(diceRoller, output)),
                actorFactory.generateBerin(startingZone, new Axe(diceRoller, output))
        );
    }

    private Optional<Zone> getZone(int x, int y) {
        return getZone(zones, new Position(x, y));
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

    public List<Survivor> getLivingSurvivors() {
        return survivors.stream()
                .filter(survivor -> !survivor.isDead())
                .toList();
    }

    /**
     * First, play zombies that can attack
     * Then, play zombies that must move
     */
    public void playZombiesPhase() {
        zombiePhase.play(zones);
    }

    public void displayBoard() {
        displayZones();
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
        List<Survivor> livingSurvivors = getLivingSurvivors();
        if (livingSurvivors.isEmpty()) {
            return false;
        }

        Zone exitZone = findZones(BoardMarker.EXIT_ZONE).stream()
                .findFirst()
                .orElseThrow();
        return new HashSet<>(exitZone.getSurvivors()).containsAll(livingSurvivors);
    }

    public boolean allSurvivorsDead() {
        return getLivingSurvivors().isEmpty();
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
        DangerLevel dangerLevel = getLivingSurvivors().stream()
                .map(Survivor::getDangerLevel)
                .max(DangerLevel::compareTo)
                .orElseThrow();
        findZones(BoardMarker.ZOMBIE_SPAWN)
                .forEach(zone -> zombieSpawner.spawnZombies(dangerLevel, zone));
    }
}
