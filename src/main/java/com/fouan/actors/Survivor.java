package com.fouan.actors;

import com.fouan.events.SurvivorMoved;
import com.fouan.events.ZombieLostLifePoints;
import com.fouan.game.view.GameView;
import com.fouan.weapons.Weapon;
import com.fouan.zones.Zone;
import lombok.EqualsAndHashCode;
import lombok.Getter;

@Getter
@EqualsAndHashCode(callSuper = true)
public final class Survivor extends Actor {

    private final String name;
    private final Weapon weapon;
    private final int experience;
    private final DangerLevel dangerLevel;
    private final int lifePoints;
    private final int actionsCount;

    public Survivor(ActorId id, int lifePoints, String name, Weapon weapon, int experience, int actionsCount) {
        super(id);
        this.name = name;
        this.weapon = weapon;
        this.experience = experience;
        this.dangerLevel = DangerLevel.fromExperience(experience);
        this.lifePoints = lifePoints;
        this.actionsCount = actionsCount;
    }

    @Override
    public void moveTo(Zone zoneToMove, GameView gameView) {
        gameView.fireEvent(
            new SurvivorMoved(gameView.getCurrentTurn(), id, zoneToMove)
        );
    }

    @Override
    public void attack(ActorId actorId, GameView gameView) {
        var damages = weapon.use(gameView.rollDice());

        gameView.fireEvent(
            new ZombieLostLifePoints(gameView.getCurrentTurn(), actorId, id, weapon, damages.hitCount())
        );
    }

    public LifeStatus getLifeStatus() {
        return lifePoints > 0 ? LifeStatus.ALIVE : LifeStatus.DEAD;
    }

    public enum LifeStatus {
        ALIVE, DEAD
    }
}
