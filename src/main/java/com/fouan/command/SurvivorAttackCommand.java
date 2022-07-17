package com.fouan.command;

import com.fouan.actor.Zombie;
import com.fouan.board.Zone;
import com.fouan.game.state.StateContext;
import com.fouan.io.Output;
import com.fouan.weapon.Weapon;

import java.util.List;

public class SurvivorAttackCommand implements Command {

    private final StateContext context;
    private final Weapon weapon;
    private final Zone targetZone;
    private final List<Zombie> killedZombies;

    public SurvivorAttackCommand(StateContext context, Weapon weapon, Zone targetZone, List<Zombie> killedZombies) {
        this.context = context;
        this.weapon = weapon;
        this.targetZone = targetZone;
        this.killedZombies = killedZombies;
    }

    @Override
    public void execute() {
        targetZone.removeActors(killedZombies);
        killedZombies.forEach(zombie -> zombie.setZone(null));
        context.setActionCounter(context.getActionCounter() + 1);
    }

    @Override
    public void executeVisual(Output output) {
        output.display(context.getPlayingSurvivor() + " attacks with " + weapon + " on " + targetZone);
        switch (killedZombies.size()) {
            case 0 -> output.display("attack missed");
            case 1 -> output.display(String.format("%d zombie is dead", killedZombies.size()));
            default -> output.display(String.format("%d zombies are dead", killedZombies.size()));
        }
    }

    @Override
    public void undo() {
        targetZone.addActors(killedZombies);
        killedZombies.forEach(zombie -> zombie.setZone(targetZone));
        context.setActionCounter(context.getActionCounter() - 1);
    }
}
