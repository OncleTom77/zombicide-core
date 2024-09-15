package com.fouan.events

class GameStarted(turn: Int, private val timestamp: Long) : Event<GameStarted>(turn)
