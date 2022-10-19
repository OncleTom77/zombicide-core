package com.fouan.actors;

import com.fouan.game.view.GameView;
import com.fouan.zones.Zone;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.EqualsAndHashCode;
import lombok.Getter;

@Getter
@AllArgsConstructor
@EqualsAndHashCode
public abstract class Actor {

    protected final ActorId id;

    public abstract void moveTo(Zone zoneToMove, GameView gameView);

    public abstract void attack(ActorId actorId, GameView gameView);
}
