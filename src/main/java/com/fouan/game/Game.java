package com.fouan.game;

import com.fouan.board.Board;
import com.fouan.io.Output;

import javax.inject.Named;
import java.util.Random;

@Named
public class Game {

    private final LoopGame loopGame;
    private final Board board;

    public Game(LoopGame loopGame, Output output, Random random) {
        this.loopGame = loopGame;
        this.board = new Board(output, random);
    }

    public void start() {
        // init game: load map, survivors, configure objectives, etc.
        board.init();

        // begin game
        loopGame.run(board);
    }

}
