package com.fouan.command;

import com.fouan.actor.ActorFactory;
import com.fouan.actor.Zombie;
import com.fouan.actor.ZombieType;
import com.fouan.board.Zone;
import com.fouan.io.Output;

import java.util.Objects;

public class GenerateZombieAfterSplitCommand implements Command {

    private final Zone zone;
    private final ZombieType type;
    private final ActorFactory actorFactory;
    private Zombie generatedZombie;

    public GenerateZombieAfterSplitCommand(Zone zone, ZombieType type, ActorFactory actorFactory) {
        this.zone = zone;
        this.type = type;
        this.actorFactory = actorFactory;
    }

    @Override
    public void execute() {
        generatedZombie = actorFactory.generateZombie(zone, type);
    }

    @Override
    public void executeVisual(Output output) {
        output.display("A zombie move implying a split generated 1 " + type + " zombie in " + zone);
    }

    @Override
    public void undo() {
        zone.removeActor(generatedZombie);
        generatedZombie.setZone(null);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        GenerateZombieAfterSplitCommand that = (GenerateZombieAfterSplitCommand) o;
        return Objects.equals(zone, that.zone) && type == that.type && Objects.equals(generatedZombie, that.generatedZombie);
    }

    @Override
    public int hashCode() {
        return Objects.hash(zone, type, generatedZombie);
    }
}
