package com.fouan.action;

import com.fouan.board.Board;
import com.fouan.character.Survivor;
import com.fouan.io.Output;

import javax.inject.Named;

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
            output.display("Zombie is dead");
            board.removeZombie();
        }
    }

    @Override
    public String toString() {
        return "Combat";
    }
}
