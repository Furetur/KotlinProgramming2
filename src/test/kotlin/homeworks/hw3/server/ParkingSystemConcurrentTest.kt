package homeworks.hw3.server

import homeworks.hw3.simulation.CarEventType
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking

internal class ParkingSystemConcurrentTest : ParkingTest() {
    override fun handleEvents(parkingSystem: ParkingSystem, events: List<CarEventType>) = runBlocking {
        for (event in events) {
            launch {
                handleEvent(parkingSystem, event)
            }
        }
    }
}
