package homeworks.hw3.server

import homeworks.hw3.Timeline
import kotlinx.coroutines.ObsoleteCoroutinesApi

class ServerApiSimulation(parkingSpacesCount: Int) {
    private val system = ParkingSystem(parkingSpacesCount)
    private val serverIo = ServerIOSimulation(system)

    val emptySpacesCount: Int
        get() = system.emptySpacesCount

    val occupiedSpacesCount: Int
        get() = system.occupiedSpacesCount

    @ObsoleteCoroutinesApi
    suspend fun handleEvent(event: Timeline.CarEvent) {
        serverIo.handleEvent(event)
    }
}
