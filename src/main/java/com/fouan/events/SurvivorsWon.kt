package com.fouan.events

class SurvivorsWon(turn: Int, val timestamp: Int) : Event<SurvivorsWon>(turn), EndGameEvent
