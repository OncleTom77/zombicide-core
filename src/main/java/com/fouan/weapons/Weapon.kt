package com.fouan.weapons

import com.fouan.dice.DiceRoller

abstract class Weapon protected constructor(val range: Range, val dice: Int, val accuracy: Int, val damage: Int) {

    fun use(diceRoller: DiceRoller): AttackResult {
        val rolls = (0 until dice)
            .map { _: Int -> diceRoller.roll() }
        val hitCount = rolls.count { roll -> roll >= accuracy }
        return AttackResult(this, rolls, hitCount)
    }
}
