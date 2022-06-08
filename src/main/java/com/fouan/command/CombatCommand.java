package com.fouan.command;

import com.fouan.actor.Survivor;
import com.fouan.actor.Zombie;
import com.fouan.board.Zone;
import com.fouan.io.Output;
import com.fouan.weapon.Weapon;

import java.util.List;

public class CombatCommand implements Command {

    private final Survivor survivor;
    private final Weapon weapon;
    private final Zone targetZone;
    private final List<Zombie> killedZombies;

    public CombatCommand(Survivor survivor, Weapon weapon, Zone targetZone, List<Zombie> killedZombies) {
        this.survivor = survivor;
        this.weapon = weapon;
        this.targetZone = targetZone;
        this.killedZombies = killedZombies;
    }

    @Override
    public void execute() {
        targetZone.removeActors(killedZombies);
    }

    @Override
    public void executeVisual(Output output) {
        output.display(survivor + " attacks with " + weapon + " on " + targetZone);
        switch (killedZombies.size()) {
            case 0 -> output.display("attack missed");
            case 1 -> output.display(String.format("%d zombie is dead", killedZombies.size()));
            default -> output.display(String.format("%d zombies are dead", killedZombies.size()));
        }
    }

    @Override
    public void undo() {
        killedZombies.forEach(targetZone::addActor);
    }
}
