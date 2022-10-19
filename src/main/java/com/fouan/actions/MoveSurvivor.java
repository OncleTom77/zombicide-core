package com.fouan.actions;

import com.fouan.actors.Survivor;
import com.fouan.actors.view.ActorsView;
import com.fouan.events.ActionChosen;
import com.fouan.game.view.GameView;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Component;

@Component
public final class MoveSurvivor implements Action {

    private final GameView gameView;
    private final ActorsView actorsView;

    public MoveSurvivor(GameView gameView, ActorsView actorsView) {
        this.gameView = gameView;
        this.actorsView = actorsView;
    }

    @Override
    public void execute() {
        int turn = gameView.getCurrentTurn();

        actorsView.findCurrentSurvivorForTurn(turn).ifPresent(
            survivor -> survivor.moveTo(null, gameView)
        );
    }

    @EventListener
    public void handleActionChosen(ActionChosen event) {
        if (event.getAction() == Actions.MOVE) {
            execute();
        }
    }
}
