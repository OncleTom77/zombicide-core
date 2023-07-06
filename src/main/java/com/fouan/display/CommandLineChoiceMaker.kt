package com.fouan.display

import javax.inject.Named

@Named
class CommandLineChoiceMaker(private val output: Output) : ChoiceMaker {

    override fun getChoices(min: Int, max: Int, nb: Int): Set<Int> {
        val result = mutableSetOf<Int>()
        for (i in 0 until nb) {
            val choices = IntRange(min, max)
                .filterNot { result.contains(it) }
                .toSet()
            result.add(getChoice(choices))
        }
        return result
    }

    override fun getChoice(min: Int, max: Int): Int {
        val choices = IntRange(min, max).toSet()
        return getChoice(choices)
    }

    override fun getChoice(choices: Set<Int>): Int {
        do {
            val choice = read()
            if (choices.contains(choice)) {
                return choice
            }
            output.display("Try again")
        } while (true)
    }

    private fun read(): Int {
        do {
            try {
                return readlnOrNull()!!.toInt()
            } catch (ignored: Exception) {
            }
            output.display("Try again")
        } while (true)
    }
}
