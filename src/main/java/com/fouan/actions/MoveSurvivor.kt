package com.fouan.actions

import com.fouan.actors.view.ActorsQueries
import com.fouan.events.ActionChosen
import com.fouan.events.AvailableZonesForSurvivorMoveDefined
import com.fouan.events.SurvivorMoved
import com.fouan.events.ZoneChosen
import com.fouan.game.view.GameView
import com.fouan.zones.view.ZonesQueries
import org.springframework.context.event.EventListener
import org.springframework.stereotype.Component

@Component
class MoveSurvivor(
    private val gameView: GameView,
    private val actorsQueries: ActorsQueries,
    private val zonesQueries: ZonesQueries
) : SurvivorAction {

    @EventListener
    fun handleActionChosen(event: ActionChosen) {
        if (event.action !== Actions.MOVE) return

        val zones = actorsQueries.findCurrentSurvivorIdForTurn(event.turn)
                .flatMap { zonesQueries.findByActorId(it) }
                .map { zonesQueries.findConnectedZones(it) }
                .orElseThrow()
        gameView.fireEvent(AvailableZonesForSurvivorMoveDefined(event.turn, zones))
    }

    @EventListener
    fun handleZoneChosen(event: ZoneChosen) {
        actorsQueries.findCurrentSurvivorIdForTurn(event.turn)
                .flatMap { actorsQueries.findLivingSurvivorBy(it) }
                .ifPresent { gameView.fireEvent(SurvivorMoved(event.turn, it.id, event.position)) }
    }

    override fun getAction(): Actions {
        return Actions.MOVE
    }

    override fun isPossible(): Boolean {
        return actorsQueries.findCurrentSurvivorIdForTurn(gameView.currentTurn)
            .flatMap { zonesQueries.findByActorId(it) }
            .map { zonesQueries.findConnectedZones(it) }
            .orElseThrow()
            .isNotEmpty()
    }
}