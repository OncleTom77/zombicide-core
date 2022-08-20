package com.fouan.dice

import java.util.*
import javax.inject.Named

@Named
internal class GameDiceRoller(
    private val random: Random,
) : DiceRoller {

    override fun roll() = random.nextInt(6) + 1
}
