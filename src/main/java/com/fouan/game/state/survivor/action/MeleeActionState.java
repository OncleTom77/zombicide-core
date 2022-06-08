package com.fouan.game.state.survivor.action;

import com.fouan.actor.Zombie;
import com.fouan.board.Zone;
import com.fouan.command.CombatCommand;
import com.fouan.game.state.State;
import com.fouan.game.state.StateContext;
import com.fouan.io.Output;
import com.fouan.weapon.AttackResult;
import com.fouan.weapon.Weapon;

import javax.inject.Named;
import java.util.List;
import java.util.stream.Collectors;

@Named("meleeActionState")
public class MeleeActionState implements State {

    private final Output output;
    private final State endSurvivorActionState;

    public MeleeActionState(Output output, @Named("endSurvivorActionState") State endSurvivorActionState) {
        this.output = output;
        this.endSurvivorActionState = endSurvivorActionState;
    }

    @Override
    public State run(StateContext context) {
        Weapon weapon = context.getSelectedWeapon();
        Zone targetZone = context.getTargetZone();

        AttackResult attackResult = weapon.use();
        List<Zombie> killableZombies = targetZone.getZombies()
                .stream()
                .filter(zombie -> zombie.canBeKilledByWeapon(weapon))
                .collect(Collectors.toList());

        //TODO: when there are more killable zombies than successful hits, asks survivor which zombie(s) to kill
        // only when zombies are of different types
        List<Zombie> killedZombies = killableZombies.subList(0, Math.min(killableZombies.size(), attackResult.getHitCount()));

        CombatCommand command = new CombatCommand(context.getPlayingSurvivor(), weapon, targetZone, killedZombies);
        command.execute();
        command.executeVisual(output);

        return endSurvivorActionState;
    }
}
