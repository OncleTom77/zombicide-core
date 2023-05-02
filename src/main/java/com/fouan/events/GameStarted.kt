package com.fouan.events

class GameStarted(turn: Int, private val timestamp: Int) : Event<GameStarted>(turn)
