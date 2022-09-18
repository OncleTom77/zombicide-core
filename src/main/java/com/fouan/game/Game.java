package com.fouan.game;

import com.fouan.game.view.GameView;
import lombok.AllArgsConstructor;

//@Named
@AllArgsConstructor
public final class Game {

    private final GameView gameView;


    public void run() {
        while (!gameView.isGameDone()) {

        }
    }
}
