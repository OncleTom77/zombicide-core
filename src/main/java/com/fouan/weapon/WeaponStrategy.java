package com.fouan.weapon;

import com.fouan.command.Command;
import com.fouan.game.state.StateContext;

public interface WeaponStrategy {

    Command apply(StateContext context, Weapon weapon);
}
