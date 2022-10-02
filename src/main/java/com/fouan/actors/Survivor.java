package com.fouan.actors;

import com.fouan.events.SurvivorMoved;
import com.fouan.events.ZombieLostLifePoints;
import com.fouan.game.view.GameView;
import com.fouan.weapons.Weapon;
import com.fouan.zones.Zone;
import lombok.Builder;
import lombok.EqualsAndHashCode;
import lombok.Getter;

@Getter
@EqualsAndHashCode(callSuper = true)
public final class Survivor extends Actor {

    public static final int LIFE_POINTS = 3;

    private final String name;
    private final Weapon weapon;
    private final int experience;
    private final DangerLevel dangerLevel;

    public Survivor(ActorId id, int lifePoints, String name, Weapon weapon, int experience) {
        super(id, lifePoints);
        this.name = name;
        this.weapon = weapon;
        this.experience = experience;
        this.dangerLevel = DangerLevel.fromExperience(experience);
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
}
