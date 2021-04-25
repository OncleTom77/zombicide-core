package com.fouan.action;

import com.fouan.actor.Survivor;
import com.fouan.actor.Zombie;
import com.fouan.board.Board;
import com.fouan.io.Output;
import com.fouan.weapon.AttackResult;

import javax.inject.Named;
import java.util.List;
import java.util.stream.Collectors;

import static java.util.Collections.singletonList;

@Named
public class Combat implements Action {

    private final Output output;

    public Combat(Output output) {
        this.output = output;
    }

    @Override
    public boolean isPossible(Survivor survivor) {
        return survivor.canFight();
    }

    @Override
    public void execute(Survivor survivor, Board board) {
        output.display("Combat!!!");

        AttackResult attackResult = survivor.attacks();
        List<Zombie> killableZombies = survivor.getZone()
                .getZombies()
                .stream()
                .filter(zombie -> zombie.canBeKilledByWeapon(survivor.getWeapon()))
                .collect(Collectors.toList());

        //TODO: when there are more killable zombies than successful hits, asks survivor which zombie(s) to kill
        // only when zombies are of different types
        List<Zombie> killedZombies = killableZombies.subList(0, Math.min(killableZombies.size(), attackResult.getHitCount()));
        switch (killedZombies.size()) {
            case 0 -> output.display("attack missed");
            case 1 -> output.display(String.format("%d zombie is dead", killedZombies.size()));
            default -> output.display(String.format("%d zombies are dead", killedZombies.size()));
        }
        removeZombies(killedZombies);
    }

    private void removeZombies(List<Zombie> killedZombies) {
        killedZombies.forEach(zombie -> zombie.getZone().removeActors(singletonList(zombie)));
    }

    @Override
    public String toString() {
        return "Combat";
    }
}
