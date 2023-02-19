package com.fouan.events;

import com.fouan.actors.ActorId;
import lombok.Getter;

@Getter
public final class SurvivorLostLifePoints
        extends Event<SurvivorLostLifePoints>
        implements ActorEvent {

    private final ActorId survivorId;
    private final ActorId attackerId;
    private final int damage;

    public SurvivorLostLifePoints(int turn, ActorId survivorId, ActorId attackerId, int damage) {
        super(turn);
        this.survivorId = survivorId;
        this.attackerId = attackerId;
        this.damage = damage;
    }
}
