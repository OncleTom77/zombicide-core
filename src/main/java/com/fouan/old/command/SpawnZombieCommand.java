package com.fouan.old.command;

import com.fouan.old.actor.ActorFactory;
import com.fouan.old.actor.Zombie;
import com.fouan.old.board.Zone;
import com.fouan.old.cards.SpawnInfo;
import com.fouan.display.Output;

import java.util.List;
import java.util.stream.IntStream;

public class SpawnZombieCommand implements Command {

    private final SpawnInfo spawnInfo;
    private final Zone zone;
    private final ActorFactory actorFactory;
    private List<Zombie> generatedZombies;

    public SpawnZombieCommand(SpawnInfo spawnInfo, Zone zone, ActorFactory actorFactory) {
        this.spawnInfo = spawnInfo;
        this.zone = zone;
        this.actorFactory = actorFactory;
    }

    @Override
    public void execute() {
        generatedZombies = IntStream.range(0, spawnInfo.quantity())
                .mapToObj(value -> actorFactory.generateZombie(zone, spawnInfo.type()))
                .toList();
    }

    @Override
    public void executeVisual(Output output) {
        output.display("Zombie spawner generates " + spawnInfo.quantity() + " " + spawnInfo.type());
    }

    @Override
    public void undo() {
        zone.removeActors(generatedZombies);
        generatedZombies.forEach(zombie -> zombie.setZone(null));
    }
}
