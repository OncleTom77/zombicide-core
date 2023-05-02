package com.fouan.events;

import com.fouan.actors.ActorId;
import lombok.Getter;

@Getter
public final class SurvivorHealed
        extends Event<SurvivorHealed>
        implements ActorEvent {

    private final ActorId survivorId;
    private final int gainedLifePoints;

    public SurvivorHealed(int turn, ActorId survivorId, int gainedLifePoints) {
        super(turn);
        this.survivorId = survivorId;
        this.gainedLifePoints = gainedLifePoints;
    }
}
