package com.fouan.game.view;

import com.fouan.events.Event;

public interface GameView {

    int getCurrentTurn();

    void fireEvent(Event<?> event);

    int rollDice();

    boolean isGameDone();

    void rollbackTurn(int turn);
}
