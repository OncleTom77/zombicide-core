package com.fouan.action;

import com.fouan.board.Board;
import com.fouan.character.Survivor;

public interface Action {

    boolean isPossible(Survivor survivor);

    void execute(Survivor survivor, Board board);
}
