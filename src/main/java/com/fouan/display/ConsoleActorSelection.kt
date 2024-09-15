package com.fouan.display

import com.fouan.actors.Actor
import javax.inject.Named

@Named
class ConsoleActorSelection(private val output: Output, private val choiceMaker: ChoiceMaker) : ActorSelection {

    override fun <T : Actor> select(actors: List<T>): T {
        output.display("Choose actor:")

        displayChoices(actors)

        val choice: Int = choiceMaker.getChoice(0, actors.size - 1)
        return actors[choice]
    }

    override fun <T : Actor> select(actors: List<T>, quantity: Int): List<T> {
        output.display("Choose actors:")

        displayChoices(actors)

        val choices: Set<Int> = choiceMaker.getChoices(0, actors.size - 1, quantity)
        return actors.filterIndexed { index, _ -> index in choices }
    }

    private fun <T : Actor> displayChoices(actors: List<T>) {
        for (i in actors.indices) {
            output.display(i.toString() + ": " + actors[i])
        }
    }
}