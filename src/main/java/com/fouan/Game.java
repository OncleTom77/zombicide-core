package com.fouan;

import javax.inject.Inject;
import javax.inject.Named;

@Named
public class Game {

    private final LoopGame loopGame;

    @Inject
    public Game(LoopGame loopGame) {
        this.loopGame = loopGame;
    }

    public void start() {
        // init game: load map, survivors, configure objectives, etc.

        // begin game
        loopGame.run();
    }
}
