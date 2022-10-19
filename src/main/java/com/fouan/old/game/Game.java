package com.fouan.old.game;

import javax.inject.Named;

//@Named
public class Game {

    private final LoopGame loopGame;

    public Game(LoopGame loopGame) {
        this.loopGame = loopGame;
    }

    public void start() {
        loopGame.run();
    }

}
