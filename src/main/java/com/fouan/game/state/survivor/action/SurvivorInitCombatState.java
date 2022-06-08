package com.fouan.game.state.survivor.action;

import com.fouan.actor.Survivor;
import com.fouan.game.state.State;
import com.fouan.game.state.StateContext;
import com.fouan.io.Output;
import com.fouan.weapon.Weapon;

import javax.inject.Named;

@Named
public class SurvivorInitCombatState implements SurvivorActionState {
    private final Output output;
    private final State meleeActionState;

    public SurvivorInitCombatState(Output output, @Named("meleeActionState") State meleeActionState) {
        this.output = output;
        this.meleeActionState = meleeActionState;
    }

    @Override
    public State run(StateContext context) {

        // TODO: handle Melee action, Ranged & Magic actions
        //  1. Choose the weapon(s)
        //  2. According to weapon type, resolve a Melee or a Ranged action
        //      1. If Melee: roll dices and choose zombies to kill
        //      2. If Ranged: choose the Zone, roll the dice, follow priority rules to kill Zombies, apply Friendly Fire for missing hits

        Survivor survivor = context.getPlayingSurvivor();
        Weapon weapon = survivor.getWeapon();
        context.setSelectedWeapon(weapon);
        context.setTargetZone(survivor.getZone());

        if (weapon.isMelee()) {
            return meleeActionState;
        }

        throw new UnsupportedOperationException();
    }

    @Override
    public boolean isPossible(StateContext context) {
        return context.getPlayingSurvivor().canFight();
    }

    @Override
    public String toString() {
        return "Combat";
    }
}
