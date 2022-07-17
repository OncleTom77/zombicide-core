package com.fouan.game.state.survivor.action;

import com.fouan.actor.Survivor;
import com.fouan.command.Command;
import com.fouan.game.state.State;
import com.fouan.game.state.StateContext;
import com.fouan.io.Output;
import com.fouan.weapon.Weapon;
import com.fouan.weapon.WeaponStrategy;

import javax.inject.Named;

@Named
public class SurvivorCombatState implements SurvivorActionState {
    private final Output output;
    private final WeaponStrategy meleeStrategy;
    private final State endSurvivorActionState;

    public SurvivorCombatState(Output output, @Named("meleeStrategy") WeaponStrategy meleeStrategy, @Named("endSurvivorActionState") State endSurvivorActionState) {
        this.output = output;
        this.meleeStrategy = meleeStrategy;
        this.endSurvivorActionState = endSurvivorActionState;
    }

    @Override
    public State run(StateContext context) {

        // TODO: handle Melee action, Ranged & Magic actions
        //  1. Choose the weapon(s) according to context (no zombie in the player's zone --> ignore melee weapon)
        //  2. According to weapon type, resolve a Melee or a Ranged action
        //      1. If Melee: roll dices and choose zombies to kill
        //      2. If Ranged: choose the Zone, roll the dice, follow priority rules to kill Zombies, apply Friendly Fire for missing hits

        Survivor survivor = context.getPlayingSurvivor();
        Weapon weapon = survivor.getWeapon();

        Command command = getStrategy(weapon).apply(context, weapon);
        command.execute();
        command.executeVisual(output);

        return endSurvivorActionState;
    }

    private WeaponStrategy getStrategy(Weapon weapon) {
        if (weapon.isMelee()) {
            return meleeStrategy;
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
