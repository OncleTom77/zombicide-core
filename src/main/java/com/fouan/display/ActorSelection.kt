package com.fouan.display

import com.fouan.actors.Actor

interface ActorSelection {
    fun <T: Actor> select(actors: List<T>): T
    fun <T: Actor> select(actors: List<T>, quantity: Int): List<T>
}