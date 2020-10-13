package homeworks.hw3.server

import homeworks.hw3.simulation.CarEventType

internal class ParkingSystemTest : ParkingTest() {
    override fun handleEvents(parkingSystem: ParkingSystem, events: List<CarEventType>) {
        for (event in events) {
            handleEvent(parkingSystem, event)
        }
    }
}
