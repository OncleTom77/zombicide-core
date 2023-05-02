package com.fouan.events

import com.fouan.zones.Position

class ZoneChosen(turn: Int, val position: Position) : Event<ZoneChosen>(turn), GameEvent
