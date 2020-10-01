package homeworks.hw3.server

import homeworks.hw3.simulation.ParkingSimulation
import kotlinx.coroutines.ObsoleteCoroutinesApi
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking

internal class ParkingServerTest : ParkingEntityTest<ParkingServer>() {
    @ObsoleteCoroutinesApi
    suspend fun handleEvent(server: ParkingServer, event: ParkingSimulation.CarEvent) {
        when (event.type) {
            ParkingSimulation.CarEventType.ARRIVAL -> server.acceptCar(event.car)
            ParkingSimulation.CarEventType.DEPARTURE -> server.handleCarDeparture(event.car)
        }
    }

    @ObsoleteCoroutinesApi
    override fun handleEvents(parkingSystem: ParkingServer, events: List<ParkingSimulation.CarEvent>) = runBlocking {
        for (event in events) {
            launch {
                handleEvent(parkingSystem, event)
            }
        }
    }

    override fun getEmptySpacesCount(parkingSystem: ParkingServer): Int = parkingSystem.emptySpacesCount

    override fun getOccupiedSpacesCount(parkingSystem: ParkingServer): Int = parkingSystem.occupiedSpacesCount

    override fun createEntity(parkingSpacesCount: Int): ParkingServer = ParkingServer(parkingSpacesCount)
}
