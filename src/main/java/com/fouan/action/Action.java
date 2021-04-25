package com.fouan.action;

import com.fouan.board.Board;
import com.fouan.actor.Survivor;

public interface Action {

    boolean isPossible(Survivor survivor);

    void execute(Survivor survivor, Board board);
}
