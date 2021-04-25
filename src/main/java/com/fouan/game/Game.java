package com.fouan.game;

import com.fouan.board.Board;
import com.fouan.board.ZombieSpawner;
import com.fouan.io.Output;
import com.fouan.weapon.DiceRoller;

import javax.inject.Named;

@Named
public class Game {

    private final LoopGame loopGame;
    private final Board board;

    public Game(LoopGame loopGame, Output output, DiceRoller diceRoller, ZombieSpawner zombieSpawner) {
        this.loopGame = loopGame;
        this.board = new Board(output, diceRoller, zombieSpawner);
    }

    public void start() {
        // init game: load map, survivors, configure objectives, etc.
        board.init();

        // begin game
        loopGame.run(board);
    }

}
