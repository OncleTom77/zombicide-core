package com.fouan.events;

import com.fouan.actors.ActorId;
import lombok.Getter;

@Getter
public final class SurvivorGainedExperience
        extends Event<SurvivorGainedExperience>
        implements ActorEvent {

    private final ActorId survivorId;
    private final int gainedExperiences;

    public SurvivorGainedExperience(int turn, ActorId survivorId, int gainedExperiences) {
        super(turn);
        this.survivorId = survivorId;
        this.gainedExperiences = gainedExperiences;
    }
}
