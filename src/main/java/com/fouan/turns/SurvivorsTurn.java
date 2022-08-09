
package com.fouan.turns;

import com.fouan.events.SurvivorsTurnEnded;
import com.fouan.events.SurvivorsTurnStarted;
import com.fouan.game.view.GameView;
import lombok.AllArgsConstructor;

@AllArgsConstructor
public final class SurvivorsTurn implements Turn {

    private final GameView gameView;

    @Override
    public void play() {

        startSurvivorsTurn();

        // Foreach survivor
        // 1 Define available actions
        // 2 Wait for selected action from displayer
        // 3 Execute action

        endSurvivorsTurn();
    }

    private void startSurvivorsTurn() {
        var newTurn = gameView.getCurrentTurn() + 1;
        gameView.fireEvent(new SurvivorsTurnStarted(newTurn));
    }

    private void endSurvivorsTurn() {
        gameView.fireEvent(new SurvivorsTurnEnded(gameView.getCurrentTurn()));
    }
}
