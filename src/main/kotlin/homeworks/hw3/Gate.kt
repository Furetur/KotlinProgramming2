package homeworks.hw3

import homeworks.hw3.server.ServerApiSimulation
import kotlinx.coroutines.ObsoleteCoroutinesApi

class Gate(val id: Int, private val api: ServerApiSimulation) {
    @ObsoleteCoroutinesApi
    suspend fun sendEvent(event: Timeline.CarEvent) {
        api.handleEvent(event)
    }
}
