package com.fouan.command;

import com.fouan.actor.ActorFactory;
import com.fouan.actor.Zombie;
import com.fouan.board.Zone;
import com.fouan.cards.SpawnInfo;
import com.fouan.io.Output;

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
        generatedZombies = IntStream.range(0, spawnInfo.getQuantity())
                .mapToObj(value -> actorFactory.generateZombie(zone, spawnInfo.getType()))
                .toList();
    }

    @Override
    public void executeVisual(Output output) {
        output.display("Zombie spawner generates " + spawnInfo.getQuantity() + " " + spawnInfo.getType());
    }

    @Override
    public void undo() {
        zone.removeActors(generatedZombies);
        generatedZombies.forEach(zombie -> zombie.setZone(null));
    }
}
