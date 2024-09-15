package com.fouan.events

import com.fouan.actors.zombies.Zombie

class ZombiesChosen(turn: Int, val chosenZombies: List<Zombie>) : Event<ZombiesChosen>(turn)