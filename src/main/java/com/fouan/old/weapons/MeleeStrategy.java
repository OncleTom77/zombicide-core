package com.fouan.old.weapons;

import com.fouan.old.actor.Zombie;
import com.fouan.old.board.Zone;
import com.fouan.old.command.Command;
import com.fouan.old.command.SurvivorAttackCommand;
import com.fouan.old.game.state.StateContext;

import javax.inject.Named;
import java.util.List;
import java.util.stream.Collectors;

@Named("meleeStrategy")
public class MeleeStrategy implements WeaponStrategy {

    @Override
    public Command apply(StateContext context, Weapon weapon) {
        Zone targetZone = context.getPlayingSurvivor().getZone();

        AttackResult attackResult = weapon.use();
        List<Zombie> killableZombies = targetZone.getZombies()
                .stream()
                .filter(zombie -> zombie.canBeKilledByWeapon(weapon))
                .collect(Collectors.toList());

        //TODO: when there are more killable zombies than successful hits, asks survivor which zombie(s) to kill
        // only when zombies are of different types
        List<Zombie> targets = killableZombies.subList(0, Math.min(killableZombies.size(), attackResult.getHitCount()));

        return new SurvivorAttackCommand(context, weapon, targetZone, targets);
    }
}
