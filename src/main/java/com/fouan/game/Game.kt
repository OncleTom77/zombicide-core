package com.fouan.game;

import com.fouan.game.view.GameView;
import com.fouan.phases.Phase;

import javax.inject.Named;
import javax.inject.Qualifier;

@Named
public final class Game {

    private final GameView gameView;

    private final Phase initializePhase;

    private final Round round;

    public Game(GameView gameView,
                @Named("initializationPhase") Phase initializePhase,
                Round round) {
        this.gameView = gameView;
        this.initializePhase = initializePhase;
        this.round = round;
    }

    public void run() {
        initializePhase.play();
        round.start();
    }
}
