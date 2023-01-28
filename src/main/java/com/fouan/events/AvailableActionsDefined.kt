package com.fouan.events

import com.fouan.actions.Actions

class AvailableActionsDefined(turn: Int, val actions: List<Actions>) : Event<AvailableActionsDefined>(turn), GameEvent