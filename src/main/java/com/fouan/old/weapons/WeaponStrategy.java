package com.fouan.old.weapons;

import com.fouan.old.command.Command;
import com.fouan.old.game.state.StateContext;

public interface WeaponStrategy {

    Command apply(StateContext context, Weapon weapon);
}
