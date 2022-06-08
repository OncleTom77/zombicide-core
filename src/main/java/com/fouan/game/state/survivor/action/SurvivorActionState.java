package com.fouan.game.state.survivor.action;

import com.fouan.game.state.State;
import com.fouan.game.state.StateContext;

public interface SurvivorActionState extends State {

    boolean isPossible(StateContext context);
}
