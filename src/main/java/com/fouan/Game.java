package com.fouan;

import javax.inject.Inject;
import javax.inject.Named;

@Named
public class Game {

    private final LoopGame loopGame;
    private final Board board;

    @Inject
    public Game(LoopGame loopGame, Output output) {
        this.loopGame = loopGame;
        this.board = new Board(output);
    }

    public void start() {
        // init game: load map, survivors, configure objectives, etc.
        board.init();

        // begin game
        loopGame.run(board);
    }

}
