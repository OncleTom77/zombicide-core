package com.fouan.game.state;

import com.fouan.actor.Survivor;
import com.fouan.board.BoardMarker;
import com.fouan.board.Zone;
import com.fouan.game.GameResult;

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
