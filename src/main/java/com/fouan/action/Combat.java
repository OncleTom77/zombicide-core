package com.fouan.action;

import com.fouan.board.Board;
import com.fouan.character.Survivor;
import com.fouan.character.Zombie;
import com.fouan.io.Output;

import javax.inject.Named;
import java.util.List;

@Named
public class Combat implements Action {

    private final Output output;

    public Combat(Output output) {
        this.output = output;
    }

    @Override
    public boolean isPossible(Survivor survivor) {
        return survivor.canFight();
    }

    @Override
    public void execute(Survivor survivor, Board board) {
        output.display("Combat!!!");
        if (survivor.attacks() > 0) {
            List<Zombie> zombies = survivor.getZone()
                    .getZombies();
            //TODO: when there is more than one zombie, asks survivor which zombie to kill
            Zombie zombie = zombies
                    .stream()
                    .findFirst()
                    .orElseThrow(IllegalArgumentException::new);

            output.display("Zombie is dead");
            board.removeZombie(zombie);
        }
    }

    @Override
    public String toString() {
        return "Combat";
    }
}
