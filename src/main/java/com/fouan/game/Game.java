package com.fouan.game;

import com.fouan.game.view.GameView;
import com.fouan.old.game.state.State;
import lombok.AllArgsConstructor;

import javax.inject.Named;

//@Named
@AllArgsConstructor
public final class Game {

    private final GameView gameView;
    @Named("initGameState")
    private final State initialState;


    public void run() {
        while (!gameView.isGameDone()) {
        }
    }
}
