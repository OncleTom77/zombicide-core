package com.fouan.display

interface ChoiceMaker {
    fun getChoices(min: Int, max: Int, nb: Int): Set<Int>
    fun getChoice(min: Int, max: Int): Int
    fun getChoice(choices: Set<Int>): Int
}
