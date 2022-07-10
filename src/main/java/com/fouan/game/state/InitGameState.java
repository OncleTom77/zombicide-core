package com.fouan.game.state;

import com.fouan.actor.ActorFactory;
import com.fouan.actor.Survivor;
import com.fouan.board.*;
import com.fouan.command.Command;
import com.fouan.command.InitGameCommand;
import com.fouan.weapon.WeaponFactory;

import javax.inject.Named;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Named("initGameState")
public class InitGameState implements State {

    private final State playerActionDecisionState;
    private final ActorFactory actorFactory;
    private final WeaponFactory weaponFactory;
    private final ZombieSpawner zombieSpawner;

    public InitGameState(@Named("playerActionDecisionState") State playerActionDecisionState,
                         ActorFactory actorFactory,
                         WeaponFactory weaponFactory,
                         ZombieSpawner zombieSpawner) {
        this.playerActionDecisionState = playerActionDecisionState;
        this.actorFactory = actorFactory;
        this.weaponFactory = weaponFactory;
        this.zombieSpawner = zombieSpawner;
    }

    @Override
    public List<Command> run(StateContext context) {
        return initGame(context);
    }

    private List<Command> initGame(StateContext context) {
        int width = 9;
        int height = 6;

        Zones zones = Zones.emptyZones(width, height);
        zones.getZone(0, 0).orElseThrow().addMarker(BoardMarker.STARTING_ZONE);
        zones.getZone(width - 1, height - 1).orElseThrow().addMarker(BoardMarker.EXIT_ZONE);
        zones.getZone(1, 0).orElseThrow().addMarker(BoardMarker.ZOMBIE_SPAWN);

        List<Survivor> survivors = initSurvivors(zones);

        List<Command> commands = new ArrayList<>();
        commands.add(new InitGameCommand(context, zones, survivors));
        commands.addAll(spawnZombies(zones));
        commands.addAll(spawnZombies(zones));

        return commands;
    }

    private List<Command> spawnZombies(Zones zones) {
        return zones.findZones(BoardMarker.ZOMBIE_SPAWN)
                .stream()
                .map(zone -> zombieSpawner.spawnZombies(DangerLevel.BLUE, zone))
                .filter(Optional::isPresent)
                .map(Optional::get)
                .toList();
    }

    private List<Survivor> initSurvivors(Zones zones) {
        Zone startingZone = zones.findZone(BoardMarker.STARTING_ZONE)
                .orElseThrow();

        return List.of(
                actorFactory.generateAsim(startingZone, weaponFactory.generateAxe()),
                actorFactory.generateBerin(startingZone, weaponFactory.generateAxe())
        );
    }

    @Override
    public State getNextState(StateContext context) {
        return playerActionDecisionState;
    }
}
