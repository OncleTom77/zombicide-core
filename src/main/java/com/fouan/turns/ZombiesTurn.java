package com.fouan.turns;

import com.fouan.events.ZombiesTurnEnded;
import com.fouan.events.ZombiesTurnStarted;
import com.fouan.game.view.GameView;
import lombok.AllArgsConstructor;

@AllArgsConstructor
public final class ZombiesTurn implements Turn {

    private final GameView gameView;

    @Override
    public void play() {

        startZombiesTurn();

        endZombiesTurn();
    }

    private void startZombiesTurn() {
        gameView.fireEvent(new ZombiesTurnStarted(gameView.getCurrentTurn()));
    }

    private void endZombiesTurn() {
        gameView.fireEvent(new ZombiesTurnEnded(gameView.getCurrentTurn()));
    }
}
