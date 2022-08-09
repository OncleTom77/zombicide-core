package com.fouan.old.game.state;

import com.fouan.old.actor.Survivor;
import com.fouan.old.board.BoardMarker;
import com.fouan.old.board.Zone;
import com.fouan.old.game.GameResult;

import java.util.HashSet;
import java.util.List;

public abstract class AbstractComputeGameResultState implements State {

    protected GameResult computeGameResult(StateContext context) {
        if (allSurvivorsDead(context)) {
            return GameResult.SURVIVORS_DEFEAT;
        }
        if (isObjectiveComplete(context)) {
            return GameResult.SURVIVORS_VICTORY;
        }
        return GameResult.UNDEFINED;
    }

    private boolean isObjectiveComplete(StateContext context) {
        List<Survivor> livingSurvivors = context.getLivingSurvivors();
        Zone exitZone = context.getZones()
                .findZone(BoardMarker.EXIT_ZONE)
                .orElseThrow();

        return new HashSet<>(exitZone.getSurvivors()).containsAll(livingSurvivors);
    }

    private boolean allSurvivorsDead(StateContext context) {
        return context.getLivingSurvivors()
                .isEmpty();
    }
}
