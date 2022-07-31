package com.fouan.game.state;

import com.fouan.actor.Zombie;
import com.fouan.actor.ZombiePhase;
import com.fouan.board.Zone;
import com.fouan.board.ZoneUtils;
import com.fouan.command.Command;
import com.fouan.game.GameResult;

import javax.inject.Named;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.stream.Collectors;

@Named("zombiePhaseState")
public class ZombiePhaseState extends AbstractComputeGameResultState {

    private final ZombiePhase zombiePhase;
    private final State endGameState;
    private final State spawnZombieState;

    public ZombiePhaseState(ZombiePhase zombiePhase, @Named("endGameState") State endGameState, @Named("spawnZombieState") State spawnZombieState) {
        this.zombiePhase = zombiePhase;
        this.endGameState = endGameState;
        this.spawnZombieState = spawnZombieState;
    }

    @Override
    public List<Command> run(StateContext context) {
        List<Command> commands = new ArrayList<>();
        List<Zone> zones = context.getZones().getZones();

        List<Zombie> activatedZombies = zones.stream()
                .map(zone -> {
                    List<Zombie> attackingZombies = zone.getZombies()
                            .stream()
                            .filter(Zombie::canFight)
                            .toList();

                    if (!attackingZombies.isEmpty()) {
                        commands.addAll(zombiePhase.handleZombieAttacks(attackingZombies, zone.getSurvivors()));
                    }

                    return attackingZombies;
                })
                .flatMap(Collection::stream)
                .collect(Collectors.toCollection(ArrayList::new));

        // TODO: game can be lost here, do not try to move Zombies
        // TODO: execute/apply zombie attacks before moving non-activated zombies, as Survivors could have been killed, changing their destination Zone

        List<Zone> defaultNoisiestZones = ZoneUtils.getNoisiestZones(context.getZones().getZones(), false);
        zones.stream()
                .filter(Zone::containsZombie)
                .map(zone -> zombiePhase.handleZombieMove(context.getZones(), zone, activatedZombies, defaultNoisiestZones))
                .forEach(commands::addAll);

        return commands;
    }

    @Override
    public State getNextState(StateContext context) {
        if (computeGameResult(context) != GameResult.UNDEFINED) {
            return endGameState;
        }

        return spawnZombieState;
    }
}
