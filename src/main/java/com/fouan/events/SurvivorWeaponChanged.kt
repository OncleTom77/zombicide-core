package com.fouan.events

import com.fouan.actors.ActorId
import com.fouan.old.weapons.Weapon

class SurvivorWeaponChanged(turn: Int, val survivorId: ActorId, val newWeapon: Weapon) :
    Event<SurvivorWeaponChanged>(turn), GameEvent
