package com.fouan.actions

import com.fouan.actors.view.ActorsView
import com.fouan.events.ActionChosen
import com.fouan.events.AvailableZonesForSurvivorMoveDefined
import com.fouan.events.SurvivorMoved
import com.fouan.events.ZoneChosen
import com.fouan.game.view.GameView
import com.fouan.zones.view.ZonesView
import org.springframework.context.event.EventListener
import org.springframework.stereotype.Component

@Component
class MoveSurvivor(
        private val gameView: GameView,
        private val actorsView: ActorsView,
        private val zonesView: ZonesView
) : Action {

    @EventListener
    fun handleActionChosen(event: ActionChosen) {
        if (event.action !== Actions.MOVE) return

        val zones = actorsView.findCurrentSurvivorIdForTurn(event.turn)
                .flatMap { actorsView.findSurvivorWithZoneBy(it) }
                .map { zonesView.findConnectedZones(it.zone) }
                .orElseThrow()
        gameView.fireEvent(AvailableZonesForSurvivorMoveDefined(event.turn, zones))
    }

    @EventListener
    fun handleZoneChosen(event: ZoneChosen) {
        actorsView.findCurrentSurvivorIdForTurn(event.turn)
                .flatMap { actorsView.findSurvivorBy(it) }
                .ifPresent { gameView.fireEvent(SurvivorMoved(event.turn, it.id, event.position)) }
    }
}