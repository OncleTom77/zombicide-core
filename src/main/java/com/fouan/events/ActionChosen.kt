package com.fouan.events

import com.fouan.actions.Actions

class ActionChosen(turn: Int, val action: Actions) : Event<ActionChosen>(turn), GameEvent