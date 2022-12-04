package com.fouan.game.view;

import com.fouan.actors.ActorId;
import com.fouan.events.Event;

public interface GameView {

    int getCurrentTurn();

    void fireEvent(Event<?> event);

    int rollDice();

    boolean isTurnEnded(ActorId actorId);

    boolean isGameDone();

    void rollbackTurn(int turn);
}
