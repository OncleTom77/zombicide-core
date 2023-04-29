package com.fouan.events

import lombok.Getter

@Getter
class SurvivorsDefeated(turn: Int) : Event<SurvivorsDefeated>(turn), EndGameEvent
