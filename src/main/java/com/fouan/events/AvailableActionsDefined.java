package com.fouan.events;

import com.fouan.actions.Actions;
import lombok.Getter;

import java.util.List;

@Getter
public final class AvailableActionsDefined extends Event<AvailableActionsDefined> implements GameEvent{

    private final List<Actions> actions;

    public AvailableActionsDefined(int turn, List<Actions> actions) {
        super(turn);
        this.actions = actions;
    }
}
