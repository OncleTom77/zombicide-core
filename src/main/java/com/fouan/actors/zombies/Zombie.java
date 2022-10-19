package com.fouan.actors.zombies;

import com.fouan.events.SurvivorLostLifePoints;
import com.fouan.events.ZombieMoved;
import com.fouan.actors.Actor;
import com.fouan.actors.ActorId;
import com.fouan.game.view.GameView;
import com.fouan.zones.Zone;
import lombok.Getter;

@Getter
public abstract class Zombie extends Actor {

    protected final String name;
    protected final int damage;
    protected final int experienceProvided;

    protected Zombie(ActorId id, String name, int damage, int experienceProvided) {
        super(id);
        this.name = name;
        this.damage = damage;
        this.experienceProvided = experienceProvided;
    }

    @Override
    public void moveTo(Zone zoneToMove, GameView gameView) {
        gameView.fireEvent(
            new ZombieMoved(gameView.getCurrentTurn(), id, zoneToMove)
        );
    }

    @Override
    public void attack(ActorId actorId, GameView gameView) {
        gameView.fireEvent(
            new SurvivorLostLifePoints(gameView.getCurrentTurn(), actorId, id, damage)
        );
    }
}
