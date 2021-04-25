package com.fouan.action;

import com.fouan.board.Board;
import com.fouan.actor.Survivor;

import javax.inject.Named;

@Named
public class DoNothing implements Action {
    @Override
    public boolean isPossible(Survivor survivor) {
        return true;
    }

    @Override
    public void execute(Survivor survivor, Board board) {
    }

    @Override
    public String toString() {
        return "Do nothing";
    }
}
