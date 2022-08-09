package com.fouan.old.command;

import com.fouan.old.actor.Survivor;
import com.fouan.old.board.Zone;
import com.fouan.display.Output;

public class ZombieAttackCommand implements Command {

    private final Survivor survivor;
    private final int wounds;
    private final Zone survivorZone;

    public ZombieAttackCommand(Survivor survivor, int wounds) {
        this.survivor = survivor;
        this.wounds = wounds;
        this.survivorZone = survivor.getZone();
    }

    @Override
    public void execute() {
        survivor.suffersInjury(wounds);
        if (survivor.isDead()) {
            survivorZone.removeActor(survivor);
            survivor.setZone(null);
        }
    }

    @Override
    public void executeVisual(Output output) {
        if (survivor.isDead()) {
            output.display(survivor + " is dead!");
        }
    }

    @Override
    public void undo() {
        if (survivor.isDead()) {
            survivorZone.addActor(survivor);
            survivor.setZone(survivorZone);
        }
        survivor.heals(wounds);
    }
}