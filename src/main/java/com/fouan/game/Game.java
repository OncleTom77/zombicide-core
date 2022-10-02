package com.fouan.game;

import com.fouan.game.view.GameView;
import com.fouan.phases.Phase;
import com.fouan.phases.SurvivorsPhase;
import lombok.AllArgsConstructor;

import javax.inject.Named;

@Named
@AllArgsConstructor
public final class Game {

    private final GameView gameView;
    //    @Named("initGameState")
//    private final State initialState;
    @Named("initializationPhase")
    private final Phase initializePhase;

    public void run() {
        initializePhase.play();
        new SurvivorsPhase(gameView).play();
//        while (!gameView.isGameDone()) {
//        }
    }
}
